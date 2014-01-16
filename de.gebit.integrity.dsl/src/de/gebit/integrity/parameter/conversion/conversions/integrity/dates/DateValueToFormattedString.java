/*******************************************************************************
 * Copyright (c) 2013 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.parameter.conversion.conversions.integrity.dates;

import de.gebit.integrity.dsl.DateValue;
import de.gebit.integrity.parameter.conversion.ConversionContext;
import de.gebit.integrity.parameter.conversion.ConversionFailedException;
import de.gebit.integrity.string.FormattedString;

/**
 * A default Integrity conversion.
 * 
 * @author Rene Schneider - initial API and implementation
 * 
 */
public class DateValueToFormattedString extends AbstractDateValueToString<FormattedString> {

	@Override
	public FormattedString convert(DateValue aSource, Class<? extends FormattedString> aTargetType,
			ConversionContext aConversionContext) throws ConversionFailedException {
		return convertToFormattedString(aSource, aConversionContext);
	}

}
