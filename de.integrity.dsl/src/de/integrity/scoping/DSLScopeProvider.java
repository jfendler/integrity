/*
 * generated by Xtext
 */
package de.integrity.scoping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmEnumerationLiteral;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.SimpleScope;

import de.integrity.dsl.Call;
import de.integrity.dsl.FixedParameterName;
import de.integrity.dsl.MethodReference;
import de.integrity.dsl.Parameter;
import de.integrity.dsl.ParameterName;
import de.integrity.dsl.ParameterTableHeader;
import de.integrity.dsl.Suite;
import de.integrity.dsl.SuiteDefinition;
import de.integrity.dsl.SuiteParameter;
import de.integrity.dsl.TableTest;
import de.integrity.dsl.Test;
import de.integrity.dsl.VariableEntity;
import de.integrity.fixtures.FixtureMethod;
import de.integrity.utils.IntegrityDSLUtil;
import de.integrity.utils.ParamAnnotationTuple;

/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping on
 * how and when to use it
 * 
 */
public class DSLScopeProvider extends AbstractDeclarativeScopeProvider {

	public IScope scope_FixedParameterName_annotation(FixedParameterName aParameterName, EReference aRef) {
		MethodReference tempMethodRef = null;
		if (aParameterName.eContainer() instanceof Parameter) {
			Parameter tempParameter = (Parameter) aParameterName.eContainer();

			if (tempParameter.eContainer() instanceof Test) {
				tempMethodRef = ((Test) tempParameter.eContainer()).getDefinition().getFixtureMethod();
			} else if (tempParameter.eContainer() instanceof Call) {
				tempMethodRef = ((Call) tempParameter.eContainer()).getDefinition().getFixtureMethod();
			} else if (tempParameter.eContainer() instanceof TableTest) {
				tempMethodRef = ((TableTest) tempParameter.eContainer()).getDefinition().getFixtureMethod();
			}
		} else if (aParameterName.eContainer() instanceof ParameterTableHeader) {
			ParameterTableHeader tempParameterHeader = (ParameterTableHeader) aParameterName.eContainer();

			if (tempParameterHeader.eContainer() instanceof TableTest) {
				tempMethodRef = ((TableTest) tempParameterHeader.eContainer()).getDefinition().getFixtureMethod();
			}
		}

		if (tempMethodRef != null) {
			ArrayList<IEObjectDescription> tempList = new ArrayList<IEObjectDescription>();
			List<ParamAnnotationTuple> tempParamList = IntegrityDSLUtil
					.getAllParamNamesFromFixtureMethod(tempMethodRef);
			for (ParamAnnotationTuple tempParam : tempParamList) {
				tempList.add(EObjectDescription.create(tempParam.getParamName(), tempParam.getAnnotation()));
			}

			return new SimpleScope(tempList);
		}

		return IScope.NULLSCOPE;
	}

	public IScope scope_MethodReference_method(MethodReference aMethodRef, EReference ref) {
		JvmType tempType = aMethodRef.getType();
		List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();

		if (tempType instanceof JvmGenericType) {
			JvmGenericType tempGenericType = (JvmGenericType) tempType;
			for (JvmMember tempMember : tempGenericType.getMembers()) {
				if (tempMember instanceof JvmOperation
						&& ((JvmOperation) tempMember).getVisibility() == JvmVisibility.PUBLIC) {
					boolean tempIsFixtureMethod = false;
					for (JvmAnnotationReference tempAnnotation : tempMember.getAnnotations()) {
						if (FixtureMethod.class.getName().equals(tempAnnotation.getAnnotation().getQualifiedName())) {
							tempIsFixtureMethod = true;
							break;
						}
					}
					if (tempIsFixtureMethod) {
						descriptions.add(EObjectDescription.create(
								QualifiedName.create(((JvmOperation) tempMember).getSimpleName()), tempMember));
					}
				}
			}
		}
		return new SimpleScope(descriptions);
	}

	public IScope scope_SuiteParameter_name(SuiteParameter aParameter, EReference aRef) {
		SuiteDefinition tempSuiteDef = (SuiteDefinition) ((Suite) aParameter.eContainer()).getDefinition();

		if (tempSuiteDef != null) {
			ArrayList<IEObjectDescription> tempList = new ArrayList<IEObjectDescription>();
			for (VariableEntity tempParam : tempSuiteDef.getParameters()) {
				tempList.add(EObjectDescription.create(tempParam.getName(), tempParam));
			}

			return new SimpleScope(tempList);
		}

		return IScope.NULLSCOPE;
	}

	public IScope scope_EnumValue_enumValue(Parameter aParameter, EReference aRef) {
		MethodReference tempMethodRef = null;
		if (aParameter.eContainer() instanceof Test) {
			tempMethodRef = ((Test) aParameter.eContainer()).getDefinition().getFixtureMethod();
		} else if (aParameter.eContainer() instanceof Call) {
			tempMethodRef = ((Call) aParameter.eContainer()).getDefinition().getFixtureMethod();
		}

		ParameterName tempParamName = aParameter.getName();

		if (tempParamName instanceof FixedParameterName) {
			JvmAnnotationReference tempAnnotationRef = ((FixedParameterName) tempParamName).getAnnotation();

			if (tempMethodRef != null && tempAnnotationRef != null) {
				ArrayList<IEObjectDescription> tempList = new ArrayList<IEObjectDescription>();
				List<JvmEnumerationLiteral> tempLiteralList = IntegrityDSLUtil
						.getAllEnumLiteralsFromFixtureMethodParam(tempMethodRef, tempAnnotationRef);
				if (tempLiteralList != null) {
					for (JvmEnumerationLiteral tempLiteral : tempLiteralList) {
						tempList.add(EObjectDescription.create(tempLiteral.getSimpleName(), tempLiteral));
					}

					return new SimpleScope(tempList);
				}
			}
		} else {
			// TODO add variable parameter name path
		}

		return IScope.NULLSCOPE;
	}

	public IScope scope_EnumValue_enumValue(Test aTest, EReference aRef) {
		MethodReference tempMethodRef = aTest.getDefinition().getFixtureMethod();

		if (tempMethodRef != null) {

			ArrayList<IEObjectDescription> tempList = new ArrayList<IEObjectDescription>();
			List<JvmEnumerationLiteral> tempLiteralList = IntegrityDSLUtil
					.getAllEnumLiteralsFromJvmTypeReference(tempMethodRef.getMethod().getReturnType());
			if (tempLiteralList != null) {
				for (JvmEnumerationLiteral tempLiteral : tempLiteralList) {
					tempList.add(EObjectDescription.create(tempLiteral.getSimpleName(), tempLiteral));
				}

				return new SimpleScope(tempList);
			}
		}

		return IScope.NULLSCOPE;
	}

}
