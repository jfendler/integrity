package de.gebit.integrity.experiments.fixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.gebit.integrity.fixtures.CustomProposalProvider;
import de.gebit.integrity.fixtures.CustomProposalProvider.CustomProposalFixtureLink;

@CustomProposalFixtureLink(CustomProposalTestFixture.class)
public class CustomProposalTestProvider implements CustomProposalProvider {

	@Override
	public List<CustomProposalDefinition> defineParameterProposals(String aFixtureMethodName, String aParameterName,
			Map<String, Object> someParameterValues) {
		List<CustomProposalDefinition> tempResults = new ArrayList<CustomProposalDefinition>();
		tempResults.add(new CustomProposalDefinition("blahblub", aFixtureMethodName + "|" + aParameterName,
				someParameterValues.toString(), null, 0, false));

		return tempResults;
	}

	@Override
	public List<CustomProposalDefinition> defineResultProposals(String aFixtureMethodName, String aResultName,
			Object aResultValue, Map<String, Object> someParameterValues) {
		List<CustomProposalDefinition> tempResults = new ArrayList<CustomProposalDefinition>();
		tempResults.add(new CustomProposalDefinition("\"blahblubresult\"", aFixtureMethodName + "|" + aResultName + "|"
				+ aResultValue, someParameterValues.toString(), null, 0, false));

		return tempResults;
	}

}
