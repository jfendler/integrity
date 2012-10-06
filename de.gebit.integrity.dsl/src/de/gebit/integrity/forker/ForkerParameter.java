/**
 * 
 */
package de.gebit.integrity.forker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author Rene Schneider
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ForkerParameter {

	/**
	 * The name of the parameter. Required, because the parameter names used in the method signature aren't available at
	 * runtime, thus they cannot be used as any kind of default.
	 * 
	 * @return
	 */
	String name();

	/**
	 * Whether the parameter must be provided or is optional.
	 * 
	 * @return
	 */
	boolean optional() default false;

}