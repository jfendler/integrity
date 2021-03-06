/**
 */
package de.gebit.integrity.dsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Suite</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getInlined <em>Inlined</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getDefinition <em>Definition</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getParameters <em>Parameters</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getReturn <em>Return</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getFork <em>Fork</em>}</li>
 *   <li>{@link de.gebit.integrity.dsl.Suite#getVariants <em>Variants</em>}</li>
 * </ul>
 *
 * @see de.gebit.integrity.dsl.DslPackage#getSuite()
 * @model
 * @generated
 */
public interface Suite extends SuiteStatementWithResult
{
  /**
   * Returns the value of the '<em><b>Inlined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inlined</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inlined</em>' attribute.
   * @see #setInlined(String)
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Inlined()
   * @model
   * @generated
   */
  String getInlined();

  /**
   * Sets the value of the '{@link de.gebit.integrity.dsl.Suite#getInlined <em>Inlined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Inlined</em>' attribute.
   * @see #getInlined()
   * @generated
   */
  void setInlined(String value);

  /**
   * Returns the value of the '<em><b>Multiplier</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Multiplier</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Multiplier</em>' containment reference.
   * @see #setMultiplier(ExecutionMultiplier)
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Multiplier()
   * @model containment="true"
   * @generated
   */
  ExecutionMultiplier getMultiplier();

  /**
   * Sets the value of the '{@link de.gebit.integrity.dsl.Suite#getMultiplier <em>Multiplier</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Multiplier</em>' containment reference.
   * @see #getMultiplier()
   * @generated
   */
  void setMultiplier(ExecutionMultiplier value);

  /**
   * Returns the value of the '<em><b>Definition</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Definition</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Definition</em>' reference.
   * @see #setDefinition(SuiteDefinition)
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Definition()
   * @model
   * @generated
   */
  SuiteDefinition getDefinition();

  /**
   * Sets the value of the '{@link de.gebit.integrity.dsl.Suite#getDefinition <em>Definition</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Definition</em>' reference.
   * @see #getDefinition()
   * @generated
   */
  void setDefinition(SuiteDefinition value);

  /**
   * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
   * The list contents are of type {@link de.gebit.integrity.dsl.SuiteParameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameters</em>' containment reference list.
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Parameters()
   * @model containment="true"
   * @generated
   */
  EList<SuiteParameter> getParameters();

  /**
   * Returns the value of the '<em><b>Return</b></em>' containment reference list.
   * The list contents are of type {@link de.gebit.integrity.dsl.SuiteReturn}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Return</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Return</em>' containment reference list.
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Return()
   * @model containment="true"
   * @generated
   */
  EList<SuiteReturn> getReturn();

  /**
   * Returns the value of the '<em><b>Fork</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Fork</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fork</em>' reference.
   * @see #setFork(ForkDefinition)
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Fork()
   * @model
   * @generated
   */
  ForkDefinition getFork();

  /**
   * Sets the value of the '{@link de.gebit.integrity.dsl.Suite#getFork <em>Fork</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fork</em>' reference.
   * @see #getFork()
   * @generated
   */
  void setFork(ForkDefinition value);

  /**
   * Returns the value of the '<em><b>Variants</b></em>' reference list.
   * The list contents are of type {@link de.gebit.integrity.dsl.VariantDefinition}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variants</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variants</em>' reference list.
   * @see de.gebit.integrity.dsl.DslPackage#getSuite_Variants()
   * @model
   * @generated
   */
  EList<VariantDefinition> getVariants();

} // Suite
