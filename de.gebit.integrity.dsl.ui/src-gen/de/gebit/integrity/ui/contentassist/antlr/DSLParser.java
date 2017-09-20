/*
 * generated by Xtext
 */
package de.gebit.integrity.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import de.gebit.integrity.services.DSLGrammarAccess;

public class DSLParser extends AbstractContentAssistParser {
	
	@Inject
	private DSLGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected de.gebit.integrity.ui.contentassist.antlr.internal.InternalDSLParser createParser() {
		de.gebit.integrity.ui.contentassist.antlr.internal.InternalDSLParser result = new de.gebit.integrity.ui.contentassist.antlr.internal.InternalDSLParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getStatementAccess().getAlternatives(), "rule__Statement__Alternatives");
					put(grammarAccess.getVisibleCommentAccess().getAlternatives(), "rule__VisibleComment__Alternatives");
					put(grammarAccess.getVisibleSingleLineCommentAccess().getAlternatives(), "rule__VisibleSingleLineComment__Alternatives");
					put(grammarAccess.getVisibleMultiLineCommentAccess().getAlternatives(), "rule__VisibleMultiLineComment__Alternatives");
					put(grammarAccess.getPackageStatementAccess().getAlternatives(), "rule__PackageStatement__Alternatives");
					put(grammarAccess.getSuiteStatementAccess().getAlternatives(), "rule__SuiteStatement__Alternatives");
					put(grammarAccess.getSuiteStatementWithResultAccess().getAlternatives(), "rule__SuiteStatementWithResult__Alternatives");
					put(grammarAccess.getVariableOrConstantEntityAccess().getAlternatives(), "rule__VariableOrConstantEntity__Alternatives");
					put(grammarAccess.getResultNameAccess().getAlternatives(), "rule__ResultName__Alternatives");
					put(grammarAccess.getParameterNameAccess().getAlternatives(), "rule__ParameterName__Alternatives");
					put(grammarAccess.getArbitraryParameterOrResultNameAccess().getAlternatives_1(), "rule__ArbitraryParameterOrResultName__Alternatives_1");
					put(grammarAccess.getOperationAccess().getAlternatives(), "rule__Operation__Alternatives");
					put(grammarAccess.getStandardOperationAccess().getOperatorsAlternatives_4_0_0(), "rule__StandardOperation__OperatorsAlternatives_4_0_0");
					put(grammarAccess.getValueOrEnumValueOrOperationAccess().getAlternatives(), "rule__ValueOrEnumValueOrOperation__Alternatives");
					put(grammarAccess.getValueAccess().getAlternatives(), "rule__Value__Alternatives");
					put(grammarAccess.getConstantValueAccess().getAlternatives(), "rule__ConstantValue__Alternatives");
					put(grammarAccess.getStaticValueAccess().getAlternatives(), "rule__StaticValue__Alternatives");
					put(grammarAccess.getBooleanValueAccess().getBooleanValueAlternatives_0(), "rule__BooleanValue__BooleanValueAlternatives_0");
					put(grammarAccess.getDateValueAccess().getAlternatives(), "rule__DateValue__Alternatives");
					put(grammarAccess.getTimeValueAccess().getAlternatives(), "rule__TimeValue__Alternatives");
					put(grammarAccess.getDateAndTimeValueAccess().getAlternatives(), "rule__DateAndTimeValue__Alternatives");
					put(grammarAccess.getKeyValuePairAccess().getAlternatives_0(), "rule__KeyValuePair__Alternatives_0");
					put(grammarAccess.getModelAccess().getGroup(), "rule__Model__Group__0");
					put(grammarAccess.getVisibleSingleLineNormalCommentAccess().getGroup(), "rule__VisibleSingleLineNormalComment__Group__0");
					put(grammarAccess.getVisibleSingleLineTitleCommentAccess().getGroup(), "rule__VisibleSingleLineTitleComment__Group__0");
					put(grammarAccess.getVisibleMultiLineNormalCommentAccess().getGroup(), "rule__VisibleMultiLineNormalComment__Group__0");
					put(grammarAccess.getVisibleMultiLineTitleCommentAccess().getGroup(), "rule__VisibleMultiLineTitleComment__Group__0");
					put(grammarAccess.getVisibleDividerAccess().getGroup(), "rule__VisibleDivider__Group__0");
					put(grammarAccess.getPackageDefinitionAccess().getGroup(), "rule__PackageDefinition__Group__0");
					put(grammarAccess.getImportAccess().getGroup(), "rule__Import__Group__0");
					put(grammarAccess.getForkDefinitionAccess().getGroup(), "rule__ForkDefinition__Group__0");
					put(grammarAccess.getForkDefinitionAccess().getGroup_5(), "rule__ForkDefinition__Group_5__0");
					put(grammarAccess.getForkDefinitionAccess().getGroup_6(), "rule__ForkDefinition__Group_6__0");
					put(grammarAccess.getForkDefinitionAccess().getGroup_7(), "rule__ForkDefinition__Group_7__0");
					put(grammarAccess.getForkParameterAccess().getGroup(), "rule__ForkParameter__Group__0");
					put(grammarAccess.getVariantDefinitionAccess().getGroup(), "rule__VariantDefinition__Group__0");
					put(grammarAccess.getVariantDefinitionAccess().getGroup_4(), "rule__VariantDefinition__Group_4__0");
					put(grammarAccess.getTestDefinitionAccess().getGroup(), "rule__TestDefinition__Group__0");
					put(grammarAccess.getCallDefinitionAccess().getGroup(), "rule__CallDefinition__Group__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup(), "rule__SuiteDefinition__Group__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_8(), "rule__SuiteDefinition__Group_8__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_8_1(), "rule__SuiteDefinition__Group_8_1__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_9(), "rule__SuiteDefinition__Group_9__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_9_1(), "rule__SuiteDefinition__Group_9_1__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_10(), "rule__SuiteDefinition__Group_10__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_10_2(), "rule__SuiteDefinition__Group_10_2__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_11(), "rule__SuiteDefinition__Group_11__0");
					put(grammarAccess.getSuiteDefinitionAccess().getGroup_11_2(), "rule__SuiteDefinition__Group_11_2__0");
					put(grammarAccess.getSuiteParameterDefinitionAccess().getGroup(), "rule__SuiteParameterDefinition__Group__0");
					put(grammarAccess.getSuiteParameterDefinitionAccess().getGroup_1(), "rule__SuiteParameterDefinition__Group_1__0");
					put(grammarAccess.getOperationDefinitionAccess().getGroup(), "rule__OperationDefinition__Group__0");
					put(grammarAccess.getVariableDefinitionAccess().getGroup(), "rule__VariableDefinition__Group__0");
					put(grammarAccess.getVariableDefinitionAccess().getGroup_5(), "rule__VariableDefinition__Group_5__0");
					put(grammarAccess.getConstantDefinitionAccess().getGroup(), "rule__ConstantDefinition__Group__0");
					put(grammarAccess.getConstantDefinitionAccess().getGroup_5(), "rule__ConstantDefinition__Group_5__0");
					put(grammarAccess.getConstantDefinitionAccess().getGroup_5_0(), "rule__ConstantDefinition__Group_5_0__0");
					put(grammarAccess.getConstantDefinitionAccess().getGroup_5_1(), "rule__ConstantDefinition__Group_5_1__0");
					put(grammarAccess.getConstantDefinitionAccess().getGroup_6(), "rule__ConstantDefinition__Group_6__0");
					put(grammarAccess.getVariableAssignmentAccess().getGroup(), "rule__VariableAssignment__Group__0");
					put(grammarAccess.getVariantValueAccess().getGroup(), "rule__VariantValue__Group__0");
					put(grammarAccess.getVariantValueAccess().getGroup_2(), "rule__VariantValue__Group_2__0");
					put(grammarAccess.getTestAccess().getGroup(), "rule__Test__Group__0");
					put(grammarAccess.getTestAccess().getGroup_4(), "rule__Test__Group_4__0");
					put(grammarAccess.getTestAccess().getGroup_5(), "rule__Test__Group_5__0");
					put(grammarAccess.getTestAccess().getGroup_6(), "rule__Test__Group_6__0");
					put(grammarAccess.getTableTestAccess().getGroup(), "rule__TableTest__Group__0");
					put(grammarAccess.getTableTestAccess().getGroup_3(), "rule__TableTest__Group_3__0");
					put(grammarAccess.getTableTestAccess().getGroup_8(), "rule__TableTest__Group_8__0");
					put(grammarAccess.getTableTestAccess().getGroup_9(), "rule__TableTest__Group_9__0");
					put(grammarAccess.getTableTestRowAccess().getGroup(), "rule__TableTestRow__Group__0");
					put(grammarAccess.getParameterTableHeaderAccess().getGroup(), "rule__ParameterTableHeader__Group__0");
					put(grammarAccess.getResultTableHeaderAccess().getGroup(), "rule__ResultTableHeader__Group__0");
					put(grammarAccess.getParameterTableValueAccess().getGroup(), "rule__ParameterTableValue__Group__0");
					put(grammarAccess.getNamedResultAccess().getGroup(), "rule__NamedResult__Group__0");
					put(grammarAccess.getCallAccess().getGroup(), "rule__Call__Group__0");
					put(grammarAccess.getCallAccess().getGroup_2(), "rule__Call__Group_2__0");
					put(grammarAccess.getCallAccess().getGroup_4(), "rule__Call__Group_4__0");
					put(grammarAccess.getCallAccess().getGroup_5(), "rule__Call__Group_5__0");
					put(grammarAccess.getCallAccess().getGroup_6(), "rule__Call__Group_6__0");
					put(grammarAccess.getNamedCallResultAccess().getGroup(), "rule__NamedCallResult__Group__0");
					put(grammarAccess.getSuiteAccess().getGroup(), "rule__Suite__Group__0");
					put(grammarAccess.getSuiteAccess().getGroup_3(), "rule__Suite__Group_3__0");
					put(grammarAccess.getSuiteAccess().getGroup_6(), "rule__Suite__Group_6__0");
					put(grammarAccess.getSuiteAccess().getGroup_7(), "rule__Suite__Group_7__0");
					put(grammarAccess.getSuiteAccess().getGroup_8(), "rule__Suite__Group_8__0");
					put(grammarAccess.getSuiteAccess().getGroup_9(), "rule__Suite__Group_9__0");
					put(grammarAccess.getSuiteAccess().getGroup_9_2(), "rule__Suite__Group_9_2__0");
					put(grammarAccess.getSuiteParameterAccess().getGroup(), "rule__SuiteParameter__Group__0");
					put(grammarAccess.getSuiteReturnAccess().getGroup(), "rule__SuiteReturn__Group__0");
					put(grammarAccess.getParameterAccess().getGroup(), "rule__Parameter__Group__0");
					put(grammarAccess.getArbitraryParameterOrResultNameAccess().getGroup(), "rule__ArbitraryParameterOrResultName__Group__0");
					put(grammarAccess.getStandardOperationAccess().getGroup(), "rule__StandardOperation__Group__0");
					put(grammarAccess.getStandardOperationAccess().getGroup_4(), "rule__StandardOperation__Group_4__0");
					put(grammarAccess.getCustomOperationAccess().getGroup(), "rule__CustomOperation__Group__0");
					put(grammarAccess.getCustomOperationAccess().getGroup_2(), "rule__CustomOperation__Group_2__0");
					put(grammarAccess.getCustomOperationAccess().getGroup_4(), "rule__CustomOperation__Group_4__0");
					put(grammarAccess.getValueOrEnumValueOrOperationCollectionAccess().getGroup(), "rule__ValueOrEnumValueOrOperationCollection__Group__0");
					put(grammarAccess.getValueOrEnumValueOrOperationCollectionAccess().getGroup_1(), "rule__ValueOrEnumValueOrOperationCollection__Group_1__0");
					put(grammarAccess.getIsoDateAndTimeValueAccess().getGroup(), "rule__IsoDateAndTimeValue__Group__0");
					put(grammarAccess.getEuropeanDateAnd24HrsTimeValueAccess().getGroup(), "rule__EuropeanDateAnd24HrsTimeValue__Group__0");
					put(grammarAccess.getEuropeanDateAnd12HrsTimeValueAccess().getGroup(), "rule__EuropeanDateAnd12HrsTimeValue__Group__0");
					put(grammarAccess.getUSDateAnd12HrsTimeValueAccess().getGroup(), "rule__USDateAnd12HrsTimeValue__Group__0");
					put(grammarAccess.getJavaConstantValueAccess().getGroup(), "rule__JavaConstantValue__Group__0");
					put(grammarAccess.getVariableAccess().getGroup(), "rule__Variable__Group__0");
					put(grammarAccess.getVariableAccess().getGroup_1(), "rule__Variable__Group_1__0");
					put(grammarAccess.getNullValueAccess().getGroup(), "rule__NullValue__Group__0");
					put(grammarAccess.getNestedObjectAccess().getGroup(), "rule__NestedObject__Group__0");
					put(grammarAccess.getNestedObjectAccess().getGroup_2(), "rule__NestedObject__Group_2__0");
					put(grammarAccess.getTypedNestedObjectAccess().getGroup(), "rule__TypedNestedObject__Group__0");
					put(grammarAccess.getKeyValuePairAccess().getGroup(), "rule__KeyValuePair__Group__0");
					put(grammarAccess.getMethodReferenceAccess().getGroup(), "rule__MethodReference__Group__0");
					put(grammarAccess.getJavaConstantReferenceAccess().getGroup(), "rule__JavaConstantReference__Group__0");
					put(grammarAccess.getExecutionMultiplierAccess().getGroup(), "rule__ExecutionMultiplier__Group__0");
					put(grammarAccess.getQualifiedNameAccess().getGroup(), "rule__QualifiedName__Group__0");
					put(grammarAccess.getQualifiedNameAccess().getGroup_1(), "rule__QualifiedName__Group_1__0");
					put(grammarAccess.getQualifiedJavaClassNameAccess().getGroup(), "rule__QualifiedJavaClassName__Group__0");
					put(grammarAccess.getQualifiedNameWithWildcardAccess().getGroup(), "rule__QualifiedNameWithWildcard__Group__0");
					put(grammarAccess.getDocumentationCommentAccess().getGroup(), "rule__DocumentationComment__Group__0");
					put(grammarAccess.getNLAccess().getGroup(), "rule__NL__Group__0");
					put(grammarAccess.getNLFORCEDAccess().getGroup(), "rule__NLFORCED__Group__0");
					put(grammarAccess.getModelAccess().getStatementsAssignment_2(), "rule__Model__StatementsAssignment_2");
					put(grammarAccess.getVisibleSingleLineNormalCommentAccess().getContentAssignment_0(), "rule__VisibleSingleLineNormalComment__ContentAssignment_0");
					put(grammarAccess.getVisibleSingleLineTitleCommentAccess().getContentAssignment_0(), "rule__VisibleSingleLineTitleComment__ContentAssignment_0");
					put(grammarAccess.getVisibleMultiLineNormalCommentAccess().getContentAssignment_0(), "rule__VisibleMultiLineNormalComment__ContentAssignment_0");
					put(grammarAccess.getVisibleMultiLineTitleCommentAccess().getContentAssignment_0(), "rule__VisibleMultiLineTitleComment__ContentAssignment_0");
					put(grammarAccess.getVisibleDividerAccess().getContentAssignment_0(), "rule__VisibleDivider__ContentAssignment_0");
					put(grammarAccess.getPackageDefinitionAccess().getNameAssignment_2(), "rule__PackageDefinition__NameAssignment_2");
					put(grammarAccess.getPackageDefinitionAccess().getStatementsAssignment_5(), "rule__PackageDefinition__StatementsAssignment_5");
					put(grammarAccess.getImportAccess().getImportedNamespaceAssignment_2(), "rule__Import__ImportedNamespaceAssignment_2");
					put(grammarAccess.getForkDefinitionAccess().getPrivateAssignment_0(), "rule__ForkDefinition__PrivateAssignment_0");
					put(grammarAccess.getForkDefinitionAccess().getNameAssignment_3(), "rule__ForkDefinition__NameAssignment_3");
					put(grammarAccess.getForkDefinitionAccess().getDescriptionAssignment_5_0(), "rule__ForkDefinition__DescriptionAssignment_5_0");
					put(grammarAccess.getForkDefinitionAccess().getForkerClassAssignment_6_2(), "rule__ForkDefinition__ForkerClassAssignment_6_2");
					put(grammarAccess.getForkDefinitionAccess().getParametersAssignment_7_0(), "rule__ForkDefinition__ParametersAssignment_7_0");
					put(grammarAccess.getForkParameterAccess().getNameAssignment_0(), "rule__ForkParameter__NameAssignment_0");
					put(grammarAccess.getForkParameterAccess().getValueAssignment_4(), "rule__ForkParameter__ValueAssignment_4");
					put(grammarAccess.getVariantDefinitionAccess().getNameAssignment_2(), "rule__VariantDefinition__NameAssignment_2");
					put(grammarAccess.getVariantDefinitionAccess().getDescriptionAssignment_4_0(), "rule__VariantDefinition__DescriptionAssignment_4_0");
					put(grammarAccess.getTestDefinitionAccess().getNameAssignment_2(), "rule__TestDefinition__NameAssignment_2");
					put(grammarAccess.getTestDefinitionAccess().getFixtureMethodAssignment_6(), "rule__TestDefinition__FixtureMethodAssignment_6");
					put(grammarAccess.getCallDefinitionAccess().getNameAssignment_2(), "rule__CallDefinition__NameAssignment_2");
					put(grammarAccess.getCallDefinitionAccess().getFixtureMethodAssignment_6(), "rule__CallDefinition__FixtureMethodAssignment_6");
					put(grammarAccess.getSuiteDefinitionAccess().getDocumentationAssignment_0(), "rule__SuiteDefinition__DocumentationAssignment_0");
					put(grammarAccess.getSuiteDefinitionAccess().getPrivateAssignment_1(), "rule__SuiteDefinition__PrivateAssignment_1");
					put(grammarAccess.getSuiteDefinitionAccess().getSingleRunAssignment_2(), "rule__SuiteDefinition__SingleRunAssignment_2");
					put(grammarAccess.getSuiteDefinitionAccess().getInlinedAssignment_3(), "rule__SuiteDefinition__InlinedAssignment_3");
					put(grammarAccess.getSuiteDefinitionAccess().getNameAssignment_6(), "rule__SuiteDefinition__NameAssignment_6");
					put(grammarAccess.getSuiteDefinitionAccess().getParametersAssignment_8_1_0(), "rule__SuiteDefinition__ParametersAssignment_8_1_0");
					put(grammarAccess.getSuiteDefinitionAccess().getReturnAssignment_9_1_0(), "rule__SuiteDefinition__ReturnAssignment_9_1_0");
					put(grammarAccess.getSuiteDefinitionAccess().getDependenciesAssignment_10_2_0(), "rule__SuiteDefinition__DependenciesAssignment_10_2_0");
					put(grammarAccess.getSuiteDefinitionAccess().getFinalizersAssignment_11_2_0(), "rule__SuiteDefinition__FinalizersAssignment_11_2_0");
					put(grammarAccess.getSuiteDefinitionAccess().getStatementsAssignment_14(), "rule__SuiteDefinition__StatementsAssignment_14");
					put(grammarAccess.getSuiteParameterDefinitionAccess().getNameAssignment_0(), "rule__SuiteParameterDefinition__NameAssignment_0");
					put(grammarAccess.getSuiteParameterDefinitionAccess().getDefaultAssignment_1_2(), "rule__SuiteParameterDefinition__DefaultAssignment_1_2");
					put(grammarAccess.getSuiteReturnDefinitionAccess().getNameAssignment(), "rule__SuiteReturnDefinition__NameAssignment");
					put(grammarAccess.getOperationDefinitionAccess().getNameAssignment_2(), "rule__OperationDefinition__NameAssignment_2");
					put(grammarAccess.getOperationDefinitionAccess().getOperationTypeAssignment_6(), "rule__OperationDefinition__OperationTypeAssignment_6");
					put(grammarAccess.getVariableDefinitionAccess().getPrivateAssignment_0(), "rule__VariableDefinition__PrivateAssignment_0");
					put(grammarAccess.getVariableDefinitionAccess().getNameAssignment_3(), "rule__VariableDefinition__NameAssignment_3");
					put(grammarAccess.getVariableDefinitionAccess().getInitialValueAssignment_5_2(), "rule__VariableDefinition__InitialValueAssignment_5_2");
					put(grammarAccess.getConstantDefinitionAccess().getPrivateAssignment_0(), "rule__ConstantDefinition__PrivateAssignment_0");
					put(grammarAccess.getConstantDefinitionAccess().getNameAssignment_3(), "rule__ConstantDefinition__NameAssignment_3");
					put(grammarAccess.getConstantDefinitionAccess().getValueAssignment_5_0_0(), "rule__ConstantDefinition__ValueAssignment_5_0_0");
					put(grammarAccess.getConstantDefinitionAccess().getVariantValuesAssignment_5_1_0(), "rule__ConstantDefinition__VariantValuesAssignment_5_1_0");
					put(grammarAccess.getConstantDefinitionAccess().getParameterizedAssignment_6_0(), "rule__ConstantDefinition__ParameterizedAssignment_6_0");
					put(grammarAccess.getVariableAssignmentAccess().getValueAssignment_2(), "rule__VariableAssignment__ValueAssignment_2");
					put(grammarAccess.getVariableAssignmentAccess().getTargetAssignment_6(), "rule__VariableAssignment__TargetAssignment_6");
					put(grammarAccess.getVariantValueAccess().getNamesAssignment_2_0(), "rule__VariantValue__NamesAssignment_2_0");
					put(grammarAccess.getVariantValueAccess().getValueAssignment_5(), "rule__VariantValue__ValueAssignment_5");
					put(grammarAccess.getVariableEntityAccess().getNameAssignment(), "rule__VariableEntity__NameAssignment");
					put(grammarAccess.getConstantEntityAccess().getNameAssignment(), "rule__ConstantEntity__NameAssignment");
					put(grammarAccess.getTestAccess().getCheckpointAssignment_0(), "rule__Test__CheckpointAssignment_0");
					put(grammarAccess.getTestAccess().getDefinitionAssignment_3(), "rule__Test__DefinitionAssignment_3");
					put(grammarAccess.getTestAccess().getParametersAssignment_4_1(), "rule__Test__ParametersAssignment_4_1");
					put(grammarAccess.getTestAccess().getResultsAssignment_5_1(), "rule__Test__ResultsAssignment_5_1");
					put(grammarAccess.getTestAccess().getResultAssignment_6_3(), "rule__Test__ResultAssignment_6_3");
					put(grammarAccess.getTableTestAccess().getDefinitionAssignment_2(), "rule__TableTest__DefinitionAssignment_2");
					put(grammarAccess.getTableTestAccess().getParametersAssignment_3_1(), "rule__TableTest__ParametersAssignment_3_1");
					put(grammarAccess.getTableTestAccess().getParameterHeadersAssignment_5(), "rule__TableTest__ParameterHeadersAssignment_5");
					put(grammarAccess.getTableTestAccess().getResultHeadersAssignment_6(), "rule__TableTest__ResultHeadersAssignment_6");
					put(grammarAccess.getTableTestAccess().getDefaultResultColumnAssignment_8_0(), "rule__TableTest__DefaultResultColumnAssignment_8_0");
					put(grammarAccess.getTableTestAccess().getRowsAssignment_9_1(), "rule__TableTest__RowsAssignment_9_1");
					put(grammarAccess.getTableTestRowAccess().getValuesAssignment_1(), "rule__TableTestRow__ValuesAssignment_1");
					put(grammarAccess.getParameterTableHeaderAccess().getNameAssignment_1(), "rule__ParameterTableHeader__NameAssignment_1");
					put(grammarAccess.getResultTableHeaderAccess().getNameAssignment_1(), "rule__ResultTableHeader__NameAssignment_1");
					put(grammarAccess.getParameterTableValueAccess().getValueAssignment_1(), "rule__ParameterTableValue__ValueAssignment_1");
					put(grammarAccess.getNamedResultAccess().getNameAssignment_0(), "rule__NamedResult__NameAssignment_0");
					put(grammarAccess.getNamedResultAccess().getValueAssignment_4(), "rule__NamedResult__ValueAssignment_4");
					put(grammarAccess.getFixedResultNameAccess().getFieldAssignment(), "rule__FixedResultName__FieldAssignment");
					put(grammarAccess.getCallAccess().getMultiplierAssignment_2_0(), "rule__Call__MultiplierAssignment_2_0");
					put(grammarAccess.getCallAccess().getDefinitionAssignment_3(), "rule__Call__DefinitionAssignment_3");
					put(grammarAccess.getCallAccess().getParametersAssignment_4_1(), "rule__Call__ParametersAssignment_4_1");
					put(grammarAccess.getCallAccess().getResultsAssignment_5_1(), "rule__Call__ResultsAssignment_5_1");
					put(grammarAccess.getCallAccess().getResultAssignment_6_3(), "rule__Call__ResultAssignment_6_3");
					put(grammarAccess.getNamedCallResultAccess().getNameAssignment_0(), "rule__NamedCallResult__NameAssignment_0");
					put(grammarAccess.getNamedCallResultAccess().getTargetAssignment_4(), "rule__NamedCallResult__TargetAssignment_4");
					put(grammarAccess.getSuiteAccess().getInlinedAssignment_0(), "rule__Suite__InlinedAssignment_0");
					put(grammarAccess.getSuiteAccess().getMultiplierAssignment_3_0(), "rule__Suite__MultiplierAssignment_3_0");
					put(grammarAccess.getSuiteAccess().getDefinitionAssignment_4(), "rule__Suite__DefinitionAssignment_4");
					put(grammarAccess.getSuiteAccess().getParametersAssignment_6_0(), "rule__Suite__ParametersAssignment_6_0");
					put(grammarAccess.getSuiteAccess().getReturnAssignment_7_0(), "rule__Suite__ReturnAssignment_7_0");
					put(grammarAccess.getSuiteAccess().getForkAssignment_8_2(), "rule__Suite__ForkAssignment_8_2");
					put(grammarAccess.getSuiteAccess().getVariantsAssignment_9_2_0(), "rule__Suite__VariantsAssignment_9_2_0");
					put(grammarAccess.getSuiteParameterAccess().getNameAssignment_0(), "rule__SuiteParameter__NameAssignment_0");
					put(grammarAccess.getSuiteParameterAccess().getValueAssignment_4(), "rule__SuiteParameter__ValueAssignment_4");
					put(grammarAccess.getSuiteReturnAccess().getNameAssignment_0(), "rule__SuiteReturn__NameAssignment_0");
					put(grammarAccess.getSuiteReturnAccess().getTargetAssignment_4(), "rule__SuiteReturn__TargetAssignment_4");
					put(grammarAccess.getParameterAccess().getNameAssignment_0(), "rule__Parameter__NameAssignment_0");
					put(grammarAccess.getParameterAccess().getValueAssignment_4(), "rule__Parameter__ValueAssignment_4");
					put(grammarAccess.getFixedParameterNameAccess().getAnnotationAssignment(), "rule__FixedParameterName__AnnotationAssignment");
					put(grammarAccess.getArbitraryParameterOrResultNameAccess().getIdentifierAssignment_1_0(), "rule__ArbitraryParameterOrResultName__IdentifierAssignment_1_0");
					put(grammarAccess.getArbitraryParameterOrResultNameAccess().getStringIdentifierAssignment_1_1(), "rule__ArbitraryParameterOrResultName__StringIdentifierAssignment_1_1");
					put(grammarAccess.getStandardOperationAccess().getFirstOperandAssignment_2(), "rule__StandardOperation__FirstOperandAssignment_2");
					put(grammarAccess.getStandardOperationAccess().getOperatorsAssignment_4_0(), "rule__StandardOperation__OperatorsAssignment_4_0");
					put(grammarAccess.getStandardOperationAccess().getMoreOperandsAssignment_4_2(), "rule__StandardOperation__MoreOperandsAssignment_4_2");
					put(grammarAccess.getCustomOperationAccess().getPrefixOperandAssignment_2_0(), "rule__CustomOperation__PrefixOperandAssignment_2_0");
					put(grammarAccess.getCustomOperationAccess().getDefinitionAssignment_3(), "rule__CustomOperation__DefinitionAssignment_3");
					put(grammarAccess.getCustomOperationAccess().getPostfixOperandAssignment_4_2(), "rule__CustomOperation__PostfixOperandAssignment_4_2");
					put(grammarAccess.getValueOrEnumValueOrOperationCollectionAccess().getValueAssignment_0(), "rule__ValueOrEnumValueOrOperationCollection__ValueAssignment_0");
					put(grammarAccess.getValueOrEnumValueOrOperationCollectionAccess().getMoreValuesAssignment_1_3(), "rule__ValueOrEnumValueOrOperationCollection__MoreValuesAssignment_1_3");
					put(grammarAccess.getIntegerValueAccess().getIntegerValueAssignment(), "rule__IntegerValue__IntegerValueAssignment");
					put(grammarAccess.getDecimalValueAccess().getDecimalValueAssignment(), "rule__DecimalValue__DecimalValueAssignment");
					put(grammarAccess.getStringValueAccess().getStringValueAssignment(), "rule__StringValue__StringValueAssignment");
					put(grammarAccess.getBooleanValueAccess().getBooleanValueAssignment(), "rule__BooleanValue__BooleanValueAssignment");
					put(grammarAccess.getIsoDateValueAccess().getDateValueAssignment(), "rule__IsoDateValue__DateValueAssignment");
					put(grammarAccess.getEuropeanDateValueAccess().getDateValueAssignment(), "rule__EuropeanDateValue__DateValueAssignment");
					put(grammarAccess.getUSDateValueAccess().getDateValueAssignment(), "rule__USDateValue__DateValueAssignment");
					put(grammarAccess.getIsoTimeValueAccess().getTimeValueAssignment(), "rule__IsoTimeValue__TimeValueAssignment");
					put(grammarAccess.getSimple24HrsTimeValueAccess().getTimeValueAssignment(), "rule__Simple24HrsTimeValue__TimeValueAssignment");
					put(grammarAccess.getSimple12HrsTimeValueAccess().getTimeValueAssignment(), "rule__Simple12HrsTimeValue__TimeValueAssignment");
					put(grammarAccess.getIsoDateAndTimeValueAccess().getDateValueAssignment_0(), "rule__IsoDateAndTimeValue__DateValueAssignment_0");
					put(grammarAccess.getIsoDateAndTimeValueAccess().getTimeValueAssignment_1(), "rule__IsoDateAndTimeValue__TimeValueAssignment_1");
					put(grammarAccess.getEuropeanDateAnd24HrsTimeValueAccess().getDateValueAssignment_0(), "rule__EuropeanDateAnd24HrsTimeValue__DateValueAssignment_0");
					put(grammarAccess.getEuropeanDateAnd24HrsTimeValueAccess().getTimeValueAssignment_2(), "rule__EuropeanDateAnd24HrsTimeValue__TimeValueAssignment_2");
					put(grammarAccess.getEuropeanDateAnd12HrsTimeValueAccess().getDateValueAssignment_0(), "rule__EuropeanDateAnd12HrsTimeValue__DateValueAssignment_0");
					put(grammarAccess.getEuropeanDateAnd12HrsTimeValueAccess().getTimeValueAssignment_2(), "rule__EuropeanDateAnd12HrsTimeValue__TimeValueAssignment_2");
					put(grammarAccess.getUSDateAnd12HrsTimeValueAccess().getDateValueAssignment_0(), "rule__USDateAnd12HrsTimeValue__DateValueAssignment_0");
					put(grammarAccess.getUSDateAnd12HrsTimeValueAccess().getTimeValueAssignment_2(), "rule__USDateAnd12HrsTimeValue__TimeValueAssignment_2");
					put(grammarAccess.getJavaConstantValueAccess().getConstantAssignment_1(), "rule__JavaConstantValue__ConstantAssignment_1");
					put(grammarAccess.getVariableAccess().getNameAssignment_0(), "rule__Variable__NameAssignment_0");
					put(grammarAccess.getVariableAccess().getAttributeAssignment_1_1(), "rule__Variable__AttributeAssignment_1_1");
					put(grammarAccess.getVariableVariableAccess().getNameAssignment(), "rule__VariableVariable__NameAssignment");
					put(grammarAccess.getConstantAccess().getNameAssignment(), "rule__Constant__NameAssignment");
					put(grammarAccess.getEnumValueAccess().getEnumValueAssignment(), "rule__EnumValue__EnumValueAssignment");
					put(grammarAccess.getNestedObjectAccess().getAttributesAssignment_2_0(), "rule__NestedObject__AttributesAssignment_2_0");
					put(grammarAccess.getTypedNestedObjectAccess().getTypeAssignment_1(), "rule__TypedNestedObject__TypeAssignment_1");
					put(grammarAccess.getTypedNestedObjectAccess().getNestedObjectAssignment_4(), "rule__TypedNestedObject__NestedObjectAssignment_4");
					put(grammarAccess.getKeyValuePairAccess().getIdentifierAssignment_0_0(), "rule__KeyValuePair__IdentifierAssignment_0_0");
					put(grammarAccess.getKeyValuePairAccess().getStringIdentifierAssignment_0_1(), "rule__KeyValuePair__StringIdentifierAssignment_0_1");
					put(grammarAccess.getKeyValuePairAccess().getValueAssignment_4(), "rule__KeyValuePair__ValueAssignment_4");
					put(grammarAccess.getJavaClassReferenceAccess().getTypeAssignment(), "rule__JavaClassReference__TypeAssignment");
					put(grammarAccess.getMethodReferenceAccess().getTypeAssignment_0(), "rule__MethodReference__TypeAssignment_0");
					put(grammarAccess.getMethodReferenceAccess().getMethodAssignment_2(), "rule__MethodReference__MethodAssignment_2");
					put(grammarAccess.getJavaConstantReferenceAccess().getTypeAssignment_0(), "rule__JavaConstantReference__TypeAssignment_0");
					put(grammarAccess.getJavaConstantReferenceAccess().getConstantAssignment_2(), "rule__JavaConstantReference__ConstantAssignment_2");
					put(grammarAccess.getExecutionMultiplierAccess().getCountAssignment_0(), "rule__ExecutionMultiplier__CountAssignment_0");
					put(grammarAccess.getDocumentationCommentAccess().getContentAssignment_0(), "rule__DocumentationComment__ContentAssignment_0");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			de.gebit.integrity.ui.contentassist.antlr.internal.InternalDSLParser typedParser = (de.gebit.integrity.ui.contentassist.antlr.internal.InternalDSLParser) parser;
			typedParser.entryRuleModel();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public DSLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(DSLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
