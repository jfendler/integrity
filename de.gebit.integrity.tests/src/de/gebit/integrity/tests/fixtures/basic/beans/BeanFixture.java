package de.gebit.integrity.tests.fixtures.basic.beans;

import java.math.BigDecimal;

import de.gebit.integrity.fixtures.FixtureMethod;
import de.gebit.integrity.fixtures.FixtureParameter;
import de.gebit.integrity.tests.fixtures.basic.beans.CollectionTestBean.InnerBean;

//SUPPRESS CHECKSTYLE LONG Javadoc
public class BeanFixture {

	@FixtureMethod(description = "checks the bean $bean$")
	public boolean checkSimpleBean(@FixtureParameter(name = "bean") SimpleTestBean aBean) {
		return "string".equals(aBean.getFirstParameter()) && aBean.getSecondParameter() == 100
				&& aBean.getThirdParameter().getInnerParameter().equals(new BigDecimal("1.99"));
	}

	@FixtureMethod(description = "gets the bean $bean$ and counts all entries in all lists")
	public int checkCollectionBean(@FixtureParameter(name = "bean") CollectionTestBean aBean) {
		int tempCount = 0;
		if (aBean.getArrayParameter() != null) {
			tempCount += aBean.getArrayParameter().length;
		}
		if (aBean.getParameterizedListParameter() != null) {
			// Verify these are of the right type by casting
			for (@SuppressWarnings("unused")
			Integer tempElement : aBean.getParameterizedListParameter()) {
				tempCount++;
			}
		}
		if (aBean.getPlainCollectionParameter() != null) {
			tempCount += aBean.getPlainCollectionParameter().size();
		}
		if (aBean.getPlainListParameter() != null) {
			tempCount += aBean.getPlainListParameter().size();
		}
		if (aBean.getSetParameterWithInnerBean() != null) {
			// Verify these are of the right type by casting
			for (@SuppressWarnings("unused")
			InnerBean tempElement : aBean.getSetParameterWithInnerBean()) {
				tempCount++;
			}
		}
		if (aBean.getLowerBoundParameterizedListParameter() != null) {
			// Verify these are of the right type by casting
			for (@SuppressWarnings("unused")
			Integer tempElement : aBean.getLowerBoundParameterizedListParameter()) {
				tempCount++;
			}
		}

		return tempCount;
	}

}
