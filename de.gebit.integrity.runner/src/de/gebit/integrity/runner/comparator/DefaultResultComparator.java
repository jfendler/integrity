/*******************************************************************************
 * Copyright (c) 2013 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.runner.comparator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.inject.Inject;

import de.gebit.integrity.comparator.ComparisonResult;
import de.gebit.integrity.comparator.MapComparisonResult;
import de.gebit.integrity.comparator.SimpleComparisonResult;
import de.gebit.integrity.dsl.CustomOperation;
import de.gebit.integrity.dsl.DateValue;
import de.gebit.integrity.dsl.MethodReference;
import de.gebit.integrity.dsl.NestedObject;
import de.gebit.integrity.dsl.NullValue;
import de.gebit.integrity.dsl.TimeValue;
import de.gebit.integrity.dsl.TypedNestedObject;
import de.gebit.integrity.dsl.ValueOrEnumValueOrOperation;
import de.gebit.integrity.dsl.ValueOrEnumValueOrOperationCollection;
import de.gebit.integrity.dsl.Variable;
import de.gebit.integrity.dsl.VariableOrConstantEntity;
import de.gebit.integrity.fixtures.FixtureWrapper;
import de.gebit.integrity.operations.UnexecutableException;
import de.gebit.integrity.parameter.conversion.UnresolvableVariableHandling;
import de.gebit.integrity.parameter.conversion.ValueConverter;
import de.gebit.integrity.parameter.resolving.ParameterResolver;
import de.gebit.integrity.utils.DateUtil;
import de.gebit.integrity.utils.IntegrityDSLUtil;
import de.gebit.integrity.utils.ParameterUtil.UnresolvableVariableException;

/**
 * The standard result comparator component.
 * 
 * @author Rene Schneider - initial API and implementation
 * 
 */
public class DefaultResultComparator implements ResultComparator {

	/**
	 * The value converter to use.
	 */
	@Inject
	protected ValueConverter valueConverter;

	/**
	 * The parameter resolver to use.
	 */
	@Inject
	protected ParameterResolver parameterResolver;

	@Override
	public ComparisonResult compareResult(Object aFixtureResult, ValueOrEnumValueOrOperationCollection anExpectedResult,
			FixtureWrapper<?> aFixtureInstance, MethodReference aFixtureMethod, String aPropertyName)
			throws ClassNotFoundException, UnexecutableException, InstantiationException {
		if (anExpectedResult != null) {
			if (aFixtureResult == null) {
				if (anExpectedResult.getMoreValues().size() > 0) {
					// if there's more than one value expected, this can never equal a single null value
					return SimpleComparisonResult.NOT_EQUAL;
				} else {
					boolean tempIsNull = false;
					// This is only true if the expected result is also null. That could be directly...
					if (anExpectedResult.getValue() instanceof NullValue) {
						tempIsNull = true;
					} else {
						// ...or indirectly by the value being a variable/constant that resolves to a null value
						VariableOrConstantEntity tempEntity = IntegrityDSLUtil
								.extractVariableOrConstantEntity(anExpectedResult.getValue());
						if (tempEntity != null) {
							Object tempResult = parameterResolver.resolveParameterValue(anExpectedResult,
									UnresolvableVariableHandling.RESOLVE_TO_NULL_VALUE);
							tempIsNull = (tempResult == null || (tempResult instanceof NullValue));
						}
					}

					return SimpleComparisonResult.valueOf(tempIsNull);
				}
			} else {
				if (aFixtureInstance.isCustomComparatorFixture()) {
					// Custom comparators will get whole arrays at once if arrays are used
					String tempMethodName = aFixtureMethod.getMethod().getSimpleName();
					Object tempConvertedResult = null;
					Class<?> tempConversionTargetType = null;

					if (aFixtureInstance.isCustomComparatorAndConversionFixture()) {
						tempConversionTargetType = aFixtureInstance.determineCustomConversionTargetType(aFixtureResult,
								tempMethodName, aPropertyName);
					} else {
						tempConversionTargetType = aFixtureResult.getClass().isArray()
								? aFixtureResult.getClass().getComponentType() : aFixtureResult.getClass();
					}

					if (anExpectedResult.getMoreValues().size() > 0) {
						// multiple result values given -> we're going to put them into an array of the same type
						// as the fixture result
						Class<?> tempArrayType = (tempConversionTargetType == null) ? Object.class
								: tempConversionTargetType;
						tempConvertedResult = Array.newInstance(tempArrayType,
								anExpectedResult.getMoreValues().size() + 1);
						for (int i = 0; i < Array.getLength(tempConvertedResult); i++) {
							ValueOrEnumValueOrOperation tempSingleExpectedResult = (i == 0 ? anExpectedResult.getValue()
									: anExpectedResult.getMoreValues().get(i - 1));
							Array.set(tempConvertedResult, i, valueConverter.convertValue(tempConversionTargetType,
									tempSingleExpectedResult, null));
						}
					} else {
						tempConvertedResult = valueConverter.convertValue(tempConversionTargetType,
								anExpectedResult.getValue(), null);
					}

					return aFixtureInstance.performCustomComparation(tempConvertedResult, aFixtureResult,
							tempMethodName, aPropertyName);
				} else {
					// Standard comparation compares each value for itself in case of arrays
					if (anExpectedResult.getMoreValues().size() > 0) {
						// multiple result values were given -> fixture result must be an array of same size
						if (!(aFixtureResult.getClass().isArray()
								&& Array.getLength(aFixtureResult) == anExpectedResult.getMoreValues().size() + 1)) {
							return SimpleComparisonResult.NOT_EQUAL;
						}
						// now compare all values
						for (int i = 0; i < Array.getLength(aFixtureResult); i++) {
							Object tempSingleFixtureResult = Array.get(aFixtureResult, i);
							ValueOrEnumValueOrOperation tempSingleExpectedResult = (i == 0 ? anExpectedResult.getValue()
									: anExpectedResult.getMoreValues().get(i - 1));
							if (tempSingleFixtureResult == null) {
								// The fixture returned a null, we need to expect a null
								if (!(tempSingleExpectedResult instanceof NullValue)) {
									return SimpleComparisonResult.NOT_EQUAL;
								}
							} else {
								if (!convertAndPerformEqualityCheck(tempSingleFixtureResult, tempSingleExpectedResult,
										tempSingleFixtureResult.getClass()).isSuccessful()) {
									return SimpleComparisonResult.NOT_EQUAL;
								}
							}
						}
						return SimpleComparisonResult.EQUAL;
					} else {
						// If we arrive here, the expected result is a simple, single value.
						ValueOrEnumValueOrOperation tempSingleExpectedResult = anExpectedResult.getValue();
						Object tempSingleFixtureResult = aFixtureResult;

						// First see if we have the special case of byte arrays (issue #66). Those must be handled
						// separately.
						if (tempSingleFixtureResult instanceof byte[]) {
							byte[] tempConvertedExpectedResult = (byte[]) valueConverter.convertValue(byte[].class,
									tempSingleExpectedResult, null);
							return SimpleComparisonResult.valueOf(
									Arrays.equals((byte[]) tempSingleFixtureResult, tempConvertedExpectedResult));
						} else if (tempSingleFixtureResult instanceof Byte[]) {
							Byte[] tempConvertedExpectedResult = (Byte[]) valueConverter.convertValue(Byte[].class,
									tempSingleExpectedResult, null);
							return SimpleComparisonResult.valueOf(
									Arrays.equals((Byte[]) tempSingleFixtureResult, tempConvertedExpectedResult));
						}

						// The fixture might still have returned an array.
						// If the expected type is an array, we don't want to convert to that array, but to the
						// component type, of course
						Class<?> tempConversionTargetType = tempSingleFixtureResult.getClass();
						if (tempSingleFixtureResult.getClass().isArray()) {
							tempConversionTargetType = tempSingleFixtureResult.getClass().getComponentType();
							if (tempConversionTargetType == Object.class) {
								// Object arrays are bad target types; in this case we try to deduct a target type from
								// the values within the array
								tempConversionTargetType = null;
								for (int i = 0; i < Array.getLength(tempSingleFixtureResult); i++) {
									Object tempArrayValue = Array.get(tempSingleFixtureResult, i);
									if (tempArrayValue != null) {
										if (tempConversionTargetType == null) {
											tempConversionTargetType = tempArrayValue.getClass();
										} else {
											if (tempConversionTargetType.isAssignableFrom(tempArrayValue.getClass())) {
												// current value type is a subtype of the current target type -> good!
											} else {
												// the types in the array don't match at all -> bad! Use standard
												// conversion.
												tempConversionTargetType = null;
												break;
											}
										}
									}
								}
							}
						}

						return convertAndPerformEqualityCheck(tempSingleFixtureResult, tempSingleExpectedResult,
								tempConversionTargetType);
					}
				}
			}
		} else {
			if (aFixtureInstance.isCustomComparatorFixture()) {
				return aFixtureInstance.performCustomComparation(null, aFixtureResult,
						aFixtureMethod.getMethod().getSimpleName(), aPropertyName);
			} else {
				if (aFixtureResult instanceof Boolean) {
					return SimpleComparisonResult.valueOf((Boolean) aFixtureResult);
				} else {
					throw new IllegalArgumentException(
							"If no expected test result is given and the fixture is not a CustomComparatorFixture, "
									+ "the test fixture must return a boolean result!");
				}
			}
		}
	}

	/**
	 * Converts a fixture result and an expected result value for comparison (usually by converting the expected result
	 * to match the fixture result, but nested objects are handled differently and converted to maps for comparison).
	 * The final results are then compared.
	 * 
	 * @param aSingleFixtureResult
	 *            the fixture result
	 * @param aSingleExpectedResult
	 *            the expected result
	 * @param aConversionTargetType
	 *            the target type for conversion
	 * @return true if both values are considered equal, false if not
	 * @throws UnresolvableVariableException
	 * @throws UnexecutableException
	 */
	protected ComparisonResult convertAndPerformEqualityCheck(Object aSingleFixtureResult,
			ValueOrEnumValueOrOperation aSingleExpectedResult, Class<?> aConversionTargetType)
			throws UnresolvableVariableException, UnexecutableException {
		Object tempConvertedExpectedResult;
		Object tempConvertedFixtureResult = aSingleFixtureResult;

		if (((aSingleExpectedResult instanceof NestedObject) || (aSingleExpectedResult instanceof TypedNestedObject))
				&& !(aSingleFixtureResult instanceof Map)) {
			// if the expected result is a (typed) nested object, and the fixture has NOT returned a
			// map, we assume the fixture result to be a bean class/instance. We'll convert both to maps
			// for comparison!
			NestedObject tempNestedObject;
			if (aSingleExpectedResult instanceof TypedNestedObject) {
				tempNestedObject = ((TypedNestedObject) aSingleExpectedResult).getNestedObject();
			} else {
				tempNestedObject = (NestedObject) aSingleExpectedResult;
			}

			tempConvertedFixtureResult = valueConverter.convertValue(Map.class, aSingleFixtureResult, null);
			tempConvertedExpectedResult = valueConverter.convertValue(Map.class, tempNestedObject, null);
		} else {
			// Two special bean-related cases still may apply: expected result may be a map, or a variable or custom
			// operation which results in a map when resolving. We now check for those.
			Object tempPossibleMapAsSingleExpectedResult = aSingleExpectedResult;
			if ((aSingleExpectedResult instanceof Variable) || (aSingleExpectedResult instanceof CustomOperation)) {
				try {
					tempPossibleMapAsSingleExpectedResult = parameterResolver.resolveSingleParameterValue(
							aSingleExpectedResult, UnresolvableVariableHandling.RESOLVE_TO_NULL_VALUE);
				} catch (InstantiationException exc) {
					throw new UnexecutableException("Failed to resolve an operation", exc);
				} catch (ClassNotFoundException exc) {
					throw new UnexecutableException("Failed to resolve an operation", exc);
				}
			}

			if (tempPossibleMapAsSingleExpectedResult instanceof Map && !(aSingleFixtureResult instanceof Map)) {
				// if the expected result is a map, and the fixture has NOT returned a map, we also assume the fixture
				// result to be a bean class/instance. But we only need to convert that to a map for comparison.
				tempConvertedFixtureResult = valueConverter.convertValue(Map.class, aSingleFixtureResult, null);
				tempConvertedExpectedResult = tempPossibleMapAsSingleExpectedResult;
			} else {
				// no special bean-related cases apply: convert the expected result to match the given fixture result
				tempConvertedExpectedResult = valueConverter.convertValue(aConversionTargetType, aSingleExpectedResult,
						null);
			}
		}

		return performEqualityCheck(tempConvertedFixtureResult, tempConvertedExpectedResult, aSingleExpectedResult);
	}

	/**
	 * Perform the actual equality check between a real result returned from a fixture and a converted result gathered
	 * from the test scripts. A few special cases are handled here, but if no special case applies, this just runs a
	 * standard equals() comparison.
	 * 
	 * @param aConvertedResult
	 *            the actual result
	 * @param aConvertedExpectedResult
	 *            the expected result from the scripts, converted to the same type as the actual result
	 * @param aRawExpectedResult
	 *            the raw expected result object from the scripts
	 * @return true if equal, false otherwise
	 */
	protected ComparisonResult performEqualityCheck(Object aConvertedResult, Object aConvertedExpectedResult,
			ValueOrEnumValueOrOperation aRawExpectedResult) {
		if (aConvertedResult == null) {
			return SimpleComparisonResult.valueOf(aConvertedExpectedResult == null
					|| (aConvertedExpectedResult.getClass().isArray() && Array.getLength(aConvertedExpectedResult) == 1
							&& Array.get(aConvertedExpectedResult, 0) == null));
		} else {
			if (aConvertedResult instanceof Date && aConvertedExpectedResult instanceof Date) {
				return performEqualityCheckForDates((Date) aConvertedResult, (Date) aConvertedExpectedResult,
						aRawExpectedResult);
			} else if (aConvertedResult instanceof Map && aConvertedExpectedResult instanceof Map) {
				return performEqualityCheckForMaps((Map<?, ?>) aConvertedResult, (Map<?, ?>) aConvertedExpectedResult,
						aRawExpectedResult);
			} else if (aConvertedResult.getClass().isArray()) {
				if (aConvertedExpectedResult == null) {
					// the fixture may still be returning an array that has to be unpacked
					if (Array.getLength(aConvertedResult) != 1) {
						return SimpleComparisonResult.NOT_EQUAL;
					}
					return SimpleComparisonResult.valueOf(Array.get(aConvertedResult, 0) == null);
				} else {
					if (!aConvertedExpectedResult.getClass().isArray()) {
						// the fixture may be returning an array that has to be unpacked
						if (Array.getLength(aConvertedResult) != 1) {
							return SimpleComparisonResult.NOT_EQUAL;
						}
						return performEqualityCheck(Array.get(aConvertedResult, 0), aConvertedExpectedResult,
								aRawExpectedResult);
					} else {
						if (Array.getLength(aConvertedResult) != Array.getLength(aConvertedExpectedResult)) {
							return SimpleComparisonResult.NOT_EQUAL;
						}
						// both are converted arrays -> compare all values!
						for (int i = 0; i < Array.getLength(aConvertedResult); i++) {
							ComparisonResult tempResult = performEqualityCheck(Array.get(aConvertedResult, i),
									Array.get(aConvertedExpectedResult, i), aRawExpectedResult);
							if (!tempResult.isSuccessful()) {
								return SimpleComparisonResult.NOT_EQUAL;
							}
						}
						return SimpleComparisonResult.EQUAL;
					}
				}
			} else {
				// This is the super-simple case where we basically have only one value to compare
				if (aConvertedExpectedResult == null) {
					// we have validated convertedResult to be non-null before
					return SimpleComparisonResult.NOT_EQUAL;
				} else {
					if (aConvertedExpectedResult.getClass().isArray()) {
						// the converted result may still be an array
						if (Array.getLength(aConvertedExpectedResult) != 1) {
							return SimpleComparisonResult.EQUAL;
						}
						return performEqualityCheck(aConvertedResult, Array.get(aConvertedExpectedResult, 0),
								aRawExpectedResult);
					} else {
						// If no special cases apply, perform standard equals comparison
						return performEqualityCheckForObjects(aConvertedResult, aConvertedExpectedResult,
								aRawExpectedResult);
					}
				}
			}
		}
	}

	/**
	 * Compare two {@link Map}s for equality. Maps are considered equal if all the values in the expected result are
	 * found in the actual result (there may well be more keys in the actual result than expected!).
	 * 
	 * @param aResult
	 *            the result returned by the fixture
	 * @param anExpectedResult
	 *            the expected result as in the script, converted for comparison
	 * @param aRawExpectedResult
	 *            the raw expected result as in the script, before conversion
	 * @return true if equal, false otherwise
	 */
	protected MapComparisonResult performEqualityCheckForMaps(Map<?, ?> aResult, Map<?, ?> anExpectedResult,
			ValueOrEnumValueOrOperation aRawExpectedResult) {
		boolean tempSuccess = true;
		Set<String> tempCombinedFailedPaths = new HashSet<String>();

		for (Entry<?, ?> tempEntry : ((Map<?, ?>) anExpectedResult).entrySet()) {
			Object tempActualValue = ((Map<?, ?>) aResult).get(tempEntry.getKey());
			Object tempReferenceValue = tempEntry.getValue();

			Object tempConvertedReferenceValue = tempReferenceValue;
			if (!(tempActualValue instanceof Map && tempReferenceValue instanceof Map)) {
				// If the inner values aren't maps themselves, special handling is required.

				// First see if they are arrays (maybe of maps, even). This stuff fixes issue #124!
				if ((tempActualValue != null && tempActualValue.getClass().isArray())
						|| (tempReferenceValue != null && tempReferenceValue.getClass().isArray())) {
					// If one or both values is an array, things get more complicated...
					if (!(tempActualValue != null && tempActualValue.getClass().isArray())
							|| !(tempReferenceValue != null && tempReferenceValue.getClass().isArray())) {
						// If just one is an array, we automatically fail, since we have a different number of elements
						// - except if one of them is an array with one element and the other one is not an array. That
						// is good, we just package the other one in an array and continue with array comparison.
						if (tempActualValue != null && tempActualValue.getClass().isArray()) {
							tempReferenceValue = new Object[] { tempReferenceValue };
						} else if (tempReferenceValue != null && tempReferenceValue.getClass().isArray()) {
							tempActualValue = new Object[] { tempActualValue };
						} else {
							tempCombinedFailedPaths.add(tempEntry.getKey().toString());
							tempSuccess = false;
							continue;
						}
					}

					// Both are arrays -> check if length is equal, then check each entry
					if (Array.getLength(tempActualValue) != Array.getLength(tempReferenceValue)) {
						tempSuccess = false;
						tempCombinedFailedPaths.add(tempEntry.getKey().toString());
					} else {
						for (int i = 0; i < Array.getLength(tempActualValue); i++) {
							ComparisonResult tempInnerResult = performEqualityCheck(Array.get(tempActualValue, i),
									Array.get(tempReferenceValue, i), aRawExpectedResult);
							if (!tempInnerResult.isSuccessful()) {
								tempSuccess = false;

								// In case the sub-result is of a map comparison, we just add the failed paths to
								// ours, prepending them with the necessary prefix in the process
								if (tempInnerResult instanceof MapComparisonResult) {
									for (String tempSubPath : ((MapComparisonResult) tempInnerResult)
											.getFailedPaths()) {
										tempCombinedFailedPaths.add(tempEntry.getKey() + "." + tempSubPath);
									}
								} else {
									tempCombinedFailedPaths.add(tempEntry.getKey().toString());
								}
								break;
							}
						}

						continue;
					}
				}

				// Okay, not arrays. In this case we still have to ensure both values are of equal type first,
				// since even though both outer values are maps, their inner values have not been necessarily converted
				// to the same types.
				try {
					tempConvertedReferenceValue = (tempActualValue != null)
							? valueConverter.convertValue(tempActualValue.getClass(), tempReferenceValue, null)
							: tempReferenceValue;
				} catch (UnresolvableVariableException exc) {
					exc.printStackTrace();
				} catch (UnexecutableException exc) {
					exc.printStackTrace();
				}
			}

			ComparisonResult tempInnerResult = performEqualityCheck(tempActualValue, tempConvertedReferenceValue,
					(tempReferenceValue instanceof ValueOrEnumValueOrOperation)
							? (ValueOrEnumValueOrOperation) tempReferenceValue : null);

			if (!tempInnerResult.isSuccessful()) {
				tempSuccess = false;

				// In case the sub-result is of a map comparison, we just add the failed paths to ours, prepending them
				// with the necessary prefix in the process
				if (tempInnerResult instanceof MapComparisonResult) {
					for (String tempSubPath : ((MapComparisonResult) tempInnerResult).getFailedPaths()) {
						tempCombinedFailedPaths.add(tempEntry.getKey() + "." + tempSubPath);
					}
				} else {
					tempCombinedFailedPaths.add(tempEntry.getKey().toString());
				}
			}
		}

		return new MapComparisonResult(tempSuccess, tempCombinedFailedPaths);
	}

	/**
	 * Compare two {@link Date}s for equality.
	 * 
	 * @param aResult
	 *            the result returned by the fixture
	 * @param anExpectedResult
	 *            the expected result as in the script, converted for comparison
	 * @param aRawExpectedResult
	 *            the raw expected result as in the script, before conversion
	 * @return true if equal, false otherwise
	 */
	protected ComparisonResult performEqualityCheckForDates(Date aResult, Date anExpectedResult,
			Object aRawExpectedResult) {
		if (aRawExpectedResult instanceof DateValue) {
			// compare only the date part
			return SimpleComparisonResult.valueOf(DateUtil.stripTimeFromDate((Date) anExpectedResult)
					.equals(DateUtil.stripTimeFromDate((Date) aResult)));
		} else if (aRawExpectedResult instanceof TimeValue) {
			// compare only the time part
			return SimpleComparisonResult.valueOf(DateUtil.stripDateFromTime((Date) anExpectedResult)
					.equals(DateUtil.stripDateFromTime((Date) aResult)));
		} else {
			// compare both parts
			return SimpleComparisonResult.valueOf(anExpectedResult.equals(aResult));
		}
	}

	/**
	 * Compare two objects. At this point it is expected that the previous stages have done all conversion work,
	 * iteration through arrays etc.
	 * 
	 * @param aResult
	 *            the result returned by the fixture
	 * @param anExpectedResult
	 *            the expected result as in the script, converted for comparison
	 * @param aRawExpectedResult
	 *            the raw expected result as in the script, before conversion
	 * @return true if equal, false otherwise
	 */
	protected ComparisonResult performEqualityCheckForObjects(Object aResult, Object anExpectedResult,
			Object aRawExpectedResult) {
		return SimpleComparisonResult.valueOf(anExpectedResult.equals(aResult));
	}
}
