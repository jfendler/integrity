/*******************************************************************************
 * Copyright (c) 2013 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.parameter.conversion.conversions.integrity.strings;

import java.math.BigInteger;

import de.gebit.integrity.dsl.StringValue;
import de.gebit.integrity.parameter.conversion.Conversion;
import de.gebit.integrity.parameter.conversion.ConversionContext;
import de.gebit.integrity.parameter.conversion.ConversionFailedException;

/**
 * A default Integrity conversion.
 * 
 * @author Rene Schneider - initial API and implementation
 * 
 */
public class StringValueToBigInteger extends Conversion<StringValue, BigInteger> {

	@Override
	public BigInteger convert(StringValue aSource, Class<? extends BigInteger> aTargetType,
			ConversionContext aConversionContext) throws ConversionFailedException {
		try {
			return new BigInteger(aSource.getStringValue());
		} catch (NumberFormatException exc) {
			throw new ConversionFailedException(aSource.getClass(), aTargetType, null, exc);
		}
	}

}
