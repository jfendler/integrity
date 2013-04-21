/*******************************************************************************
 * Copyright (c) 2013 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.parameter.conversion;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.gebit.integrity.dsl.Constant;
import de.gebit.integrity.dsl.ConstantDefinition;
import de.gebit.integrity.dsl.ConstantValue;
import de.gebit.integrity.dsl.CustomOperation;
import de.gebit.integrity.dsl.StandardOperation;
import de.gebit.integrity.dsl.ValueOrEnumValueOrOperation;
import de.gebit.integrity.dsl.ValueOrEnumValueOrOperationCollection;
import de.gebit.integrity.dsl.Variable;
import de.gebit.integrity.operations.UnexecutableException;
import de.gebit.integrity.operations.custom.CustomOperationWrapper;
import de.gebit.integrity.operations.standard.StandardOperationProcessor;
import de.gebit.integrity.parameter.conversion.Conversion.Priority;
import de.gebit.integrity.parameter.resolving.ParameterResolver;
import de.gebit.integrity.parameter.variables.VariableManager;
import de.gebit.integrity.string.FormattedString;
import de.gebit.integrity.string.FormattedStringElement;
import de.gebit.integrity.utils.JavaTypeUtil;
import de.gebit.integrity.utils.ParameterUtil.UnresolvableVariableException;
import de.gebit.integrity.wrapper.WrapperFactory;

/**
 * Abstract base class for a value converter which uses conversion classes to determine how a given value is to be
 * converted into a desired form. This modularity makes it easy to extend the converter with additional conversions.
 * 
 * @author Rene Schneider - initial API and implementation
 * 
 */
public abstract class AbstractModularValueConverter implements ValueConverter {

	/**
	 * The parameter resolver.
	 */
	@Inject
	protected ParameterResolver parameterResolver;

	/**
	 * The variable manager.
	 */
	@Inject(optional = true)
	protected VariableManager variableManager;

	/**
	 * The wrapper factory.
	 */
	@Inject(optional = true)
	protected WrapperFactory wrapperFactory;

	/**
	 * The processor for standard operations.
	 */
	@Inject
	protected StandardOperationProcessor standardOperationProcessor;

	/**
	 * The Guice injector. Required to inject stuff into instances of conversions.
	 */
	@Inject
	protected Injector injector;

	/**
	 * All known conversions.
	 */
	private Map<ConversionKey, Class<? extends Conversion<?, ?>>> conversions = new HashMap<ConversionKey, Class<? extends Conversion<?, ?>>>();

	/**
	 * The default conversions for all known source types. These are the conversions with the highest priority from
	 * their respective source types' conversion pool.
	 */
	private Map<Class<?>, Class<? extends Conversion<?, ?>>> defaultConversions = new HashMap<Class<?>, Class<? extends Conversion<?, ?>>>();

	/**
	 * The current defaults' priority. Used to fill the {@link #defaultConversions} map.
	 */
	private Map<Class<?>, Integer> conversionPriority = new HashMap<Class<?>, Integer>();

	/**
	 * Implement this method to initialize known conversions.
	 * 
	 */
	protected abstract void initializeConversions();

	/**
	 * Default constructor. Initializes all conversions.
	 */
	public AbstractModularValueConverter() {
		initializeConversions();
	}

	@Override
	public Object convertValue(Class<?> aTargetType, Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) throws UnresolvableVariableException,
			UnexecutableException {
		return convertValue(aTargetType, null, aValue, anUnresolvableVariableHandlingPolicy);
	}

	@Override
	public Object convertValue(Class<?> aTargetType, Class<?> aParameterizedType, Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) throws UnresolvableVariableException,
			UnexecutableException {
		return convertValue(aTargetType, aParameterizedType, aValue, anUnresolvableVariableHandlingPolicy,
				new HashSet<Object>());
	}

	/**
	 * Extended version of #convertValue(Class, Class, Object, UnresolvableVariableHandling).
	 * 
	 * @param aTargetType
	 * @param aParameterizedType
	 * @param aValue
	 * @param anUnresolvableVariableHandlingPolicy
	 * @param someVisitedObjects
	 * @return
	 * @throws UnresolvableVariableException
	 * @throws ClassNotFoundException
	 * @throws UnexecutableException
	 * @throws InstantiationException
	 */
	public Object convertValue(Class<?> aTargetType, Class<?> aParameterizedType, Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy, Set<Object> someVisitedObjects)
			throws UnresolvableVariableException, UnexecutableException {
		if (someVisitedObjects.contains(aValue)) {
			// endless loop protection
			return null;
		} else {
			try {
				someVisitedObjects.add(aValue);
				if (aValue instanceof ValueOrEnumValueOrOperationCollection) {
					return convertEncapsulatedValueCollectionToTargetType(aTargetType, aParameterizedType,
							(ValueOrEnumValueOrOperationCollection) aValue, anUnresolvableVariableHandlingPolicy,
							someVisitedObjects);
				} else if (aValue instanceof ValueOrEnumValueOrOperation) {
					return convertEncapsulatedValueToTargetType(aTargetType, aParameterizedType,
							(ValueOrEnumValueOrOperation) aValue, anUnresolvableVariableHandlingPolicy,
							someVisitedObjects);
				} else if (aValue instanceof ConstantValue) {
					return convertEncapsulatedConstantValueToTargetType(aTargetType, aParameterizedType,
							(ConstantValue) aValue, anUnresolvableVariableHandlingPolicy, someVisitedObjects);
				} else {
					return convertPlainValueToTargetType(aTargetType, aParameterizedType, aValue,
							anUnresolvableVariableHandlingPolicy, someVisitedObjects);
				}
			} finally {
				someVisitedObjects.remove(aValue);
			}
		}
	}

	/**
	 * Converts a given plain value (no instance of {@link ValueOrEnumValueOrOperation} or
	 * {@link ValueOrEnumValueOrOperationCollection}) to a given Java type class, if possible.
	 * 
	 * @param aTargetType
	 *            the target type
	 * @param aParameterizedType
	 *            the parameterized (via generics) type attached to the given target type, if applicable - for example
	 *            if a conversion to List<Integer> is desired, the target type is List, and the parameterized type is
	 *            Integer
	 * @param aValue
	 *            the value
	 * @param anUnresolvableVariableHandlingPolicy
	 *            Defines the policy how unresolvable variable references (no variable given or no
	 *            {@link de.gebit.integrity.parameter.variables.VariableManager} available) shall be treated
	 * @return the converted value
	 */
	protected Object convertPlainValueToTargetType(Class<?> aTargetType, Class<?> aParameterizedType, Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy, Set<Object> someVisitedValues) {
		if (aValue == null) {
			return null;
		}

		if (aTargetType != null && aTargetType.isArray()) {
			Class<?> tempActualParamType = aTargetType.getComponentType();
			Object tempResultArray;
			if (aValue.getClass().isArray()) {
				// both are arrays
				tempResultArray = Array.newInstance(tempActualParamType, Array.getLength(aValue));
				for (int i = 0; i < Array.getLength(aValue); i++) {
					Array.set(
							tempResultArray,
							i,
							convertPlainValueToTargetType(tempActualParamType, aParameterizedType,
									Array.get(aValue, i), anUnresolvableVariableHandlingPolicy, someVisitedValues));
				}
			} else {
				// target is an array, but value is a single value
				tempResultArray = Array.newInstance(tempActualParamType, 1);
				Array.set(
						tempResultArray,
						0,
						convertPlainValueToTargetType(tempActualParamType, aParameterizedType, aValue,
								anUnresolvableVariableHandlingPolicy, someVisitedValues));
			}
			return tempResultArray;
		} else {
			if (aValue.getClass().isArray()) {
				// this is not convertible, but since this method does not guarantee any conversion...
				return aValue;
			} else {
				// unresolvable variables can't happen here, since variable values should have gone down the other path
				return convertSingleValueToTargetType(aTargetType, aParameterizedType, aValue,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Object convertSingleValueToTargetType(Class<?> aTargetType, Class<?> aParameterizedType, Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy, Set<Object> someVisitedValues) {
		if (aValue == null) {
			return null;
		}

		Class<?> tempTargetType = transformPrimitiveTypes(aTargetType);
		Class<?> tempSourceType = transformPrimitiveTypes(aValue.getClass());

		if (tempTargetType != null && tempTargetType.isAssignableFrom(tempSourceType)) {
			// No conversion necessary
			return aValue;
		}

		if (tempTargetType == null && tempSourceType.getName().startsWith("java.")) {
			// Java types generally have themselves as "default type" and don't need to be converted to anything
			return aValue;
		}

		try {
			@SuppressWarnings("rawtypes")
			Conversion tempConversion = findConversion(tempSourceType, tempTargetType, someVisitedValues);
			if (tempConversion != null) {
				return tempConversion.convert(aValue, tempTargetType, anUnresolvableVariableHandlingPolicy);
			}

			throw new ConversionUnsupportedException(aValue.getClass(), aTargetType,
					"Could not find a matching conversion");
		} catch (InstantiationException exc) {
			throw new ConversionFailedException(aValue.getClass(), tempTargetType, "Failed to instantiate conversion",
					exc);
		} catch (IllegalAccessException exc) {
			throw new ConversionFailedException(aValue.getClass(), tempTargetType, "Failed to instantiate conversion",
					exc);
		} catch (ConversionUnsupportedException exc) {
			throw exc;
			// SUPPRESS CHECKSTYLE IllegalCatch
		} catch (Throwable exc) {
			throw new ConversionFailedException(aValue.getClass(), tempTargetType,
					"Unexpected error during conversion", exc);
		}
	}

	private Class<?> transformPrimitiveTypes(Class<?> aType) {
		if (int.class.equals(aType)) {
			return Integer.class;
		} else if (long.class.equals(aType)) {
			return Long.class;
		} else if (short.class.equals(aType)) {
			return Short.class;
		} else if (byte.class.equals(aType)) {
			return Byte.class;
		} else if (float.class.equals(aType)) {
			return Float.class;
		} else if (double.class.equals(aType)) {
			return Double.class;
		} else if (char.class.equals(aType)) {
			return Character.class;
		} else if (boolean.class.equals(aType)) {
			return Boolean.class;
		} else {
			return aType;
		}
	}

	/**
	 * Converts a given {@link ValueOrEnumValueOrOperation} to a given Java type class, if possible.
	 * 
	 * @param aTargetType
	 *            the target type
	 * @param aParameterizedType
	 *            the parameterized (via generics) type attached to the given target type, if applicable - for example
	 *            if a conversion to List<Integer> is desired, the target type is List, and the parameterized type is
	 *            Integer
	 * @param aValue
	 *            the value
	 * @param anUnresolvableVariableHandlingPolicy
	 *            Defines the policy how unresolvable variable references (no variable given or no
	 *            {@link de.gebit.integrity.parameter.variables.VariableManager} available) shall be treated
	 * @return the converted value
	 * @throws UnresolvableVariableException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws UnexecutableException
	 */
	protected Object convertEncapsulatedValueToTargetType(Class<?> aTargetType, Class<?> aParameterizedType,
			ValueOrEnumValueOrOperation aValue, UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy,
			Set<Object> someVisitedValues) throws UnresolvableVariableException, UnexecutableException {
		if (aValue == null) {
			return null;
		}

		try {
			if (aValue instanceof StandardOperation) {
				Object tempResult = standardOperationProcessor.executeOperation((StandardOperation) aValue);
				return convertPlainValueToTargetType(aTargetType, aParameterizedType, tempResult,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			} else if (aValue instanceof CustomOperation) {
				if (wrapperFactory == null) {
					// cannot execute operations without the ability to load them
					return null;
				} else {
					CustomOperationWrapper tempWrapper = wrapperFactory
							.newCustomOperationWrapper((CustomOperation) aValue);
					Object tempResult = tempWrapper.executeOperation();
					return convertPlainValueToTargetType(aTargetType, aParameterizedType, tempResult,
							anUnresolvableVariableHandlingPolicy, someVisitedValues);
				}
			} else if (aValue instanceof Variable) {
				Object tempResult = parameterResolver.resolveSingleParameterValue(aValue,
						anUnresolvableVariableHandlingPolicy);
				return convertSingleValueToTargetType(aTargetType, aParameterizedType, tempResult,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			} else {
				return convertPlainValueToTargetType(aTargetType, aParameterizedType, aValue,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			}
		} catch (ClassNotFoundException exc) {
			throw new ConversionFailedException(null, aTargetType, exc.getMessage(), exc);
		} catch (InstantiationException exc) {
			throw new ConversionFailedException(null, aTargetType, exc.getMessage(), exc);
		}
	}

	/**
	 * Converts a given {@link ValueOrEnumValueOrOperation} to a given Java type class, if possible.
	 * 
	 * @param aTargetType
	 *            the target type
	 * @param aParameterizedType
	 *            the parameterized (via generics) type attached to the given target type, if applicable - for example
	 *            if a conversion to List<Integer> is desired, the target type is List, and the parameterized type is
	 *            Integer
	 * @param aValue
	 *            the value
	 * @param anUnresolvableVariableHandlingPolicy
	 *            Defines the policy how unresolvable variable references (no variable given or no
	 *            {@link de.gebit.integrity.parameter.variables.VariableManager} available) shall be treated
	 * @return the converted value
	 * @throws UnresolvableVariableException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws UnexecutableException
	 */
	protected Object convertEncapsulatedConstantValueToTargetType(Class<?> aTargetType, Class<?> aParameterizedType,
			ConstantValue aValue, UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy,
			Set<Object> someVisitedValues) throws UnresolvableVariableException, UnexecutableException {
		if (aValue == null) {
			return null;
		}

		if (aValue instanceof Constant) {
			if (variableManager != null) {
				// Constants need to be "constantly" defined in the variable manager at runtime, so we can ask it
				// directly.
				Object tempResult = variableManager.get(((Constant) aValue).getName());
				return convertSingleValueToTargetType(aTargetType, aParameterizedType, tempResult,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			} else if (((Constant) aValue).getName().eContainer() instanceof ConstantDefinition) {
				// Without the variable manager, we can still attempt to resolve statically.
				try {
					return parameterResolver.resolveStatically((ConstantDefinition) ((Constant) aValue).getName()
							.eContainer(), null);
				} catch (ClassNotFoundException exc) {
					exc.printStackTrace();
				} catch (InstantiationException exc) {
					exc.printStackTrace();
				}
			}
			return null;
		} else {
			return convertPlainValueToTargetType(aTargetType, aParameterizedType, aValue,
					anUnresolvableVariableHandlingPolicy, someVisitedValues);
		}
	}

	/**
	 * Converts a given value collection to a given Java type class, if possible. Will return an array if the collection
	 * contains more than one item.
	 * 
	 * @param aTargetType
	 *            the target type
	 * @param aParameterizedType
	 *            the parameterized (via generics) type attached to the given target type, if applicable - for example
	 *            if a conversion to List<Integer> is desired, the target type is List, and the parameterized type is
	 *            Integer
	 * @param aCollection
	 *            the value collection
	 * @param anUnresolvableVariableHandlingPolicy
	 *            Defines the policy how unresolvable variable references (no variable given or no
	 *            {@link de.gebit.integrity.parameter.variables.VariableManager} available) shall be treated
	 * @return the converted value
	 * @throws UnresolvableVariableException
	 * @throws ClassNotFoundException
	 * @throws UnexecutableException
	 * @throws InstantiationException
	 */
	@SuppressWarnings({ "rawtypes" })
	protected Object convertEncapsulatedValueCollectionToTargetType(Class<?> aTargetType, Class<?> aParameterizedType,
			ValueOrEnumValueOrOperationCollection aCollection,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy, Set<Object> someVisitedValues)
			throws UnresolvableVariableException, UnexecutableException {

		Class<?> tempTargetType = null;
		Class<? extends Collection> tempCollectionType = null;
		if (aTargetType != null) {
			if (aTargetType.isArray()) {
				tempTargetType = aTargetType.getComponentType();
			} else if (aTargetType.isAssignableFrom(ArrayList.class)) {
				tempCollectionType = ArrayList.class;
			} else if (aTargetType.isAssignableFrom(HashSet.class)) {
				tempCollectionType = HashSet.class;
			} else {
				tempTargetType = aTargetType;
			}
		}

		// Collections may specify a target type via a generics parameter
		if (tempCollectionType != null && aParameterizedType != null) {
			tempTargetType = aParameterizedType;
		}

		Class<?> tempTargetArrayType = tempTargetType;
		if (tempTargetArrayType == null) {
			tempTargetArrayType = Object.class;
		}

		if (aCollection.getMoreValues() != null && aCollection.getMoreValues().size() > 0) {
			// this is actually an array
			Object tempResultArray = Array.newInstance(tempTargetArrayType, aCollection.getMoreValues().size() + 1);
			for (int i = 0; i < aCollection.getMoreValues().size() + 1; i++) {
				ValueOrEnumValueOrOperation tempValue = (i == 0 ? aCollection.getValue() : aCollection.getMoreValues()
						.get(i - 1));
				Object tempResultValue = convertEncapsulatedValueToTargetType(tempTargetType, aParameterizedType,
						tempValue, anUnresolvableVariableHandlingPolicy, someVisitedValues);
				Array.set(tempResultArray, i, tempResultValue);
			}

			// now we need to see whether we're even allowed to return an array
			if (aTargetType == null) {
				return tempResultArray;
			} else if (aTargetType.isArray()) {
				return tempResultArray;
			} else if (tempCollectionType != null) {
				return wrapInCollection((Class<? extends Collection>) tempCollectionType, tempResultArray);
			} else {
				throw new IllegalArgumentException("Parameter type class " + aTargetType
						+ " is not an array, but more than one value was given for conversion.");
			}
		} else {
			// this is just a single value
			Object tempResult = convertEncapsulatedValueToTargetType(tempTargetType, aParameterizedType,
					aCollection.getValue(), anUnresolvableVariableHandlingPolicy, someVisitedValues);

			// but we might need to return this as an array with one element
			if (aTargetType == null) {
				return tempResult;
			} else if (aTargetType.isArray()) {
				Object tempResultArray = Array.newInstance(tempTargetArrayType, 1);
				Array.set(tempResultArray, 0, tempResult);
				return tempResultArray;
			} else if (tempCollectionType != null) {
				return wrapInCollection((Class<? extends Collection>) tempCollectionType, tempResult);
			} else {
				return tempResult;
			}
		}
	}

	/**
	 * Wraps a value (or an array of values) in a collection of the given type.
	 * 
	 * @param aCollectionType
	 *            the collection type
	 * @param anArrayOrSingleType
	 *            the array or value to wrap
	 * @return the collection
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T extends Collection> T wrapInCollection(Class<T> aCollectionType, Object anArrayOrSingleType) {
		T tempCollectionInstance;
		try {
			tempCollectionInstance = aCollectionType.newInstance();
		} catch (IllegalAccessException exc) {
			throw new RuntimeException("Failed to create collection instance", exc);
		} catch (InstantiationException exc) {
			throw new RuntimeException("Failed to create collection instance", exc);
		}
		if (anArrayOrSingleType.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(anArrayOrSingleType); i++) {
				Collections.addAll(tempCollectionInstance, Array.get(anArrayOrSingleType, i));
			}
		} else {
			Collections.addAll(tempCollectionInstance, anArrayOrSingleType);
		}
		return tempCollectionInstance;
	}

	@Override
	public String convertValueToString(Object aValue, boolean aForceIntermediateMapFlag,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) {
		return convertValueToFormattedString(aValue, aForceIntermediateMapFlag, anUnresolvableVariableHandlingPolicy)
				.toUnformattedString();
	}

	@Override
	public FormattedString convertValueToFormattedString(Object aValue, boolean aForceIntermediateMapFlag,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) {
		if (aForceIntermediateMapFlag) {
			try {
				Map<?, ?>[] tempIntermediateMap = (Map[]) convertValue(Map[].class, aValue,
						anUnresolvableVariableHandlingPolicy);
				return convertValueToFormattedString(tempIntermediateMap, false, anUnresolvableVariableHandlingPolicy);
			} catch (UnresolvableVariableException exc) {
				exc.printStackTrace();
				return new FormattedString("FAILURE");
			} catch (UnexecutableException exc) {
				exc.printStackTrace();
				return new FormattedString("FAILURE");
			} catch (ConversionException exc) {
				exc.printStackTrace();
				return new FormattedString("FAILURE");
			}
		}

		// always convert to an array, so array values will convert fine
		FormattedString[] tempResult = convertValueToFormattedStringArray(aValue, anUnresolvableVariableHandlingPolicy);

		FormattedString tempBuffer = new FormattedString();
		for (FormattedString tempSingleResult : tempResult) {
			if (tempBuffer.getElementCount() > 0) {
				tempBuffer.add(new FormattedStringElement(", "));
			}
			tempBuffer.add(tempSingleResult);
		}

		return tempBuffer;
	}

	@Override
	public String[] convertValueToStringArray(Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) {
		FormattedString[] tempFormattedStrings = convertValueToStringArray(aValue,
				anUnresolvableVariableHandlingPolicy, new HashSet<Object>());

		String[] tempStrings = new String[tempFormattedStrings.length];
		for (int i = 0; i < tempFormattedStrings.length; i++) {
			tempStrings[i] = tempFormattedStrings[i].toUnformattedString();
		}

		return tempStrings;
	}

	@Override
	public FormattedString[] convertValueToFormattedStringArray(Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy) {
		return convertValueToStringArray(aValue, anUnresolvableVariableHandlingPolicy, new HashSet<Object>());
	}

	/**
	 * Extended version of {@link #convertValueToStringArray(Object, UnresolvableVariableHandling)}.
	 * 
	 * @param aValue
	 * @param anUnresolvableVariableHandlingPolicy
	 * @param someVisitedValues
	 * @return
	 */
	public FormattedString[] convertValueToStringArray(Object aValue,
			UnresolvableVariableHandling anUnresolvableVariableHandlingPolicy, Set<Object> someVisitedValues) {
		FormattedString[] tempResult;
		try {
			if (aValue instanceof ValueOrEnumValueOrOperationCollection) {
				tempResult = (FormattedString[]) convertEncapsulatedValueCollectionToTargetType(
						FormattedString[].class, null, (ValueOrEnumValueOrOperationCollection) aValue,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			} else if (aValue instanceof ValueOrEnumValueOrOperation) {
				tempResult = (FormattedString[]) convertEncapsulatedValueToTargetType(FormattedString[].class, null,
						(ValueOrEnumValueOrOperation) aValue, anUnresolvableVariableHandlingPolicy, someVisitedValues);
			} else {
				tempResult = (FormattedString[]) convertPlainValueToTargetType(FormattedString[].class, null, aValue,
						anUnresolvableVariableHandlingPolicy, someVisitedValues);
			}
		} catch (UnexecutableException exc) {
			// this is expected to happen in some cases during dry run - but not a problem
			return new FormattedString[] { new FormattedString("???") };
		} catch (UnresolvableVariableException exc) {
			// This is expected to happen - for example in case of operations depending on undefined variables.
			return new FormattedString[] { new FormattedString("???") };
		} catch (ConversionException exc) {
			exc.printStackTrace();
			return new FormattedString[] { new FormattedString("FAILURE") };
		}

		if (tempResult == null) {
			return new FormattedString[] { new FormattedString("null") };
		} else {
			return tempResult;
		}
	}

	/**
	 * Adds the given conversion class to the map of available conversions.
	 * 
	 * @param aConversion
	 *            the conversion to add
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addConversion(Class<? extends Conversion> aConversion) {
		Class<? extends Conversion<?, ?>> tempConversion = (Class<? extends Conversion<?, ?>>) aConversion;
		ConversionKey tempConversionKey = new ConversionKey(tempConversion);

		// See whether the new conversion has a higher priority than the current default conversion for the given
		// source type
		Priority tempPriorityAnnotation = aConversion.getAnnotation(Priority.class);
		int tempNewPriority = Integer.MIN_VALUE;
		if (tempPriorityAnnotation != null) {
			tempNewPriority = tempPriorityAnnotation.value();
		}

		Integer tempCurrentPriority = conversionPriority.get(tempConversionKey.getSourceType());
		if (tempCurrentPriority == null || (tempNewPriority > tempCurrentPriority)) {
			defaultConversions.put(tempConversionKey.getSourceType(), tempConversion);
			conversionPriority.put(tempConversionKey.getSourceType(), tempNewPriority);
		}

		conversions.put(tempConversionKey, tempConversion);
	}

	/**
	 * This class defines a key for efficient searching for conversions in maps.
	 * 
	 * 
	 * @author Rene Schneider - initial API and implementation
	 * 
	 */
	protected static class ConversionKey {

		/**
		 * The source type.
		 */
		private Class<?> sourceType;

		/**
		 * The target type.
		 */
		private Class<?> targetType;

		/**
		 * Internally, a string is used to determine equality and hash code.
		 */
		private String internalKey;

		private void initializeInternalKey(Class<?> aSourceType, Class<?> aTargetType) {
			sourceType = aSourceType;
			targetType = aTargetType;
			internalKey = (aSourceType.getName() + " -> " + aTargetType.getName());
		}

		public Class<?> getSourceType() {
			return sourceType;
		}

		public Class<?> getTargetType() {
			return targetType;
		}

		/**
		 * Creates a new instance.
		 * 
		 * @param aSourceType
		 *            the source type
		 * @param aTargetType
		 *            the target type
		 */
		public ConversionKey(Class<?> aSourceType, Class<?> aTargetType) {
			initializeInternalKey(aSourceType, aTargetType);
		}

		/**
		 * Takes a {@link Conversion} implementation and determines the applicable conversion key.
		 * 
		 * @param aConversion
		 *            the conversion to look at
		 */
		public ConversionKey(Class<? extends Conversion<?, ?>> aConversion) {
			Class<?> tempClass = aConversion;

			Type tempType = JavaTypeUtil.findGenericInterfaceOrSuperType(tempClass, Conversion.class);
			if (tempType != null) {
				// Replacing one of the types (source OR target) in the Conversion superinterface with a variable is
				// supported. In that case, it is expected that the first generic superinterface in the whole class
				// hierarchy does define that variable.
				Class<?> tempSourceType = null;
				Object tempSourceTypeObj = ((ParameterizedType) tempType).getActualTypeArguments()[0];
				if (tempSourceTypeObj instanceof Class) {
					tempSourceType = (Class<?>) tempSourceTypeObj;
				} else if (tempSourceTypeObj instanceof TypeVariable) {
					Type tempSubType = JavaTypeUtil.findGenericInterfaceOrSuperType(tempClass, null);
					tempSourceType = (Class<?>) ((ParameterizedType) tempSubType).getActualTypeArguments()[0];
				}

				Class<?> tempTargetType = null;
				Object tempTargetTypeObj = ((ParameterizedType) tempType).getActualTypeArguments()[1];
				if (tempTargetTypeObj instanceof Class) {
					tempTargetType = (Class<?>) tempTargetTypeObj;
				} else if (tempTargetTypeObj instanceof TypeVariable) {
					Type tempSubType = JavaTypeUtil.findGenericInterfaceOrSuperType(tempClass, null);
					tempTargetType = (Class<?>) ((ParameterizedType) tempSubType).getActualTypeArguments()[0];
				}

				initializeInternalKey(tempSourceType, tempTargetType);
			} else {
				throw new IllegalArgumentException("Was unable to find valid generic Conversion superinterface");
			}
		}

		@Override
		public int hashCode() {
			return internalKey.hashCode();
		}

		@Override
		public boolean equals(Object anObject) {
			if (!(anObject instanceof ConversionKey)) {
				return false;
			} else {
				return internalKey.equals(((ConversionKey) anObject).internalKey);
			}
		}

		@Override
		public String toString() {
			return internalKey;
		}
	}

	/**
	 * Searches all known conversions for a match which is able to convert a given source type into a given target type.
	 * 
	 * @param aSourceType
	 *            the source type
	 * @param aTargetType
	 *            the target type
	 * @return a ready-to-use, instantiated conversion, or null if none was found
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected Conversion<?, ?> findConversion(Class<?> aSourceType, Class<?> aTargetType, Set<Object> someVisitedValues)
			throws InstantiationException, IllegalAccessException {
		Class<?> tempSourceTypeInFocus = aSourceType;
		while (tempSourceTypeInFocus != null) {
			Class<? extends Conversion<?, ?>> tempConversionClass = null;
			if (aTargetType == null) {
				// This is the default target type case
				tempConversionClass = defaultConversions.get(tempSourceTypeInFocus);
			} else {
				// We actually have a target type
				Class<?> tempTargetTypeInFocus = aTargetType;
				while (tempTargetTypeInFocus != null) {
					tempConversionClass = conversions.get(new ConversionKey(tempSourceTypeInFocus,
							tempTargetTypeInFocus));
					if (tempConversionClass != null) {
						break;
					}

					for (Class<?> tempTargetInterface : tempTargetTypeInFocus.getInterfaces()) {
						Conversion<?, ?> tempConversion = findConversion(tempSourceTypeInFocus, tempTargetInterface,
								someVisitedValues);
						if (tempConversion != null) {
							return tempConversion;
						}
					}

					tempTargetTypeInFocus = tempTargetTypeInFocus.getSuperclass();
				}
			}

			if (tempConversionClass != null) {
				return createConversionInstance(tempConversionClass, someVisitedValues);
			} else {
				for (Class<?> tempSourceInterface : tempSourceTypeInFocus.getInterfaces()) {
					if (aTargetType == null) {
						// This is the default target type case
						Conversion<?, ?> tempConversion = findConversion(tempSourceInterface, null, someVisitedValues);
						if (tempConversion != null) {
							return tempConversion;
						}
					} else {
						// We actually have a target type
						Class<?> tempTargetTypeInFocus = aTargetType;
						while (tempTargetTypeInFocus != null) {
							Conversion<?, ?> tempConversion = findConversion(tempSourceInterface,
									tempTargetTypeInFocus, someVisitedValues);
							if (tempConversion != null) {
								return tempConversion;
							}

							for (Class<?> tempTargetInterface : tempTargetTypeInFocus.getInterfaces()) {
								tempConversion = findConversion(tempSourceInterface, tempTargetInterface,
										someVisitedValues);
								if (tempConversion != null) {
									return tempConversion;
								}
							}

							tempTargetTypeInFocus = tempTargetTypeInFocus.getSuperclass();
						}
					}
				}

				tempSourceTypeInFocus = tempSourceTypeInFocus.getSuperclass();
			}
		}

		return null;
	}

	/**
	 * Creates an instance of the given conversion class. This also injects the Guice dependencies.
	 * 
	 * @param aConversionClass
	 *            the conversion
	 * @return the new instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <C extends Conversion> C createConversionInstance(Class<C> aConversionClass, Set<Object> someVisitedValues)
			throws InstantiationException, IllegalAccessException {
		C tempInstance = aConversionClass.newInstance();
		injector.injectMembers(tempInstance);
		tempInstance.setVisitedObjects(someVisitedValues);

		return tempInstance;
	}

}
