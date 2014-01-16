/*******************************************************************************
 * Copyright (c) 2013 Rene Schneider, GEBIT Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.gebit.integrity.parameter.conversion.conversions.java.identity;

import java.math.BigDecimal;

import de.gebit.integrity.parameter.conversion.Conversion;
import de.gebit.integrity.parameter.conversion.ConversionContext;
import de.gebit.integrity.parameter.conversion.ConversionFailedException;

/**
 * A default Integrity conversion. This conversion actually doesn't convert anything - it exists solely to keep
 * BigDecimals as BigDecimals by default.
 * 
 * @author Rene Schneider - initial API and implementation
 * 
 */
@de.gebit.integrity.parameter.conversion.Conversion.Priority(0)
public class BigDecimalToBigDecimal extends Conversion<BigDecimal, BigDecimal> {

	@Override
	public BigDecimal convert(BigDecimal aSource, Class<? extends BigDecimal> aTargetType,
			ConversionContext aConversionContext) throws ConversionFailedException {
		return aSource;
	}

}
