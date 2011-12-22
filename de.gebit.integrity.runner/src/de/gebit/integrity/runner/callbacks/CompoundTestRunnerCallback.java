package de.gebit.integrity.runner.callbacks;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.gebit.integrity.dsl.Call;
import de.gebit.integrity.dsl.Suite;
import de.gebit.integrity.dsl.SuiteDefinition;
import de.gebit.integrity.dsl.TableTest;
import de.gebit.integrity.dsl.TableTestRow;
import de.gebit.integrity.dsl.Test;
import de.gebit.integrity.dsl.VariableEntity;
import de.gebit.integrity.runner.TestModel;
import de.gebit.integrity.runner.results.SuiteResult;
import de.gebit.integrity.runner.results.call.CallResult;
import de.gebit.integrity.runner.results.test.TestResult;
import de.gebit.integrity.runner.results.test.TestSubResult;

public class CompoundTestRunnerCallback implements TestRunnerCallback {

	private List<TestRunnerCallback> callbacks = new LinkedList<TestRunnerCallback>();

	public CompoundTestRunnerCallback(TestRunnerCallback... someCallbacks) {
		super();
		for (TestRunnerCallback tempCallback : someCallbacks) {
			addCallback(tempCallback);
		}
	}

	public void addCallback(TestRunnerCallback aCallback) {
		callbacks.add(aCallback);
	}

	public void removeCallback(TestRunnerCallback aCallback) {
		callbacks.remove(aCallback);
	}

	@Override
	public void onExecutionStart(TestModel aModel, Map<VariableEntity, Object> aVariableMap) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onExecutionStart(aModel, aVariableMap);
		}
	}

	@Override
	public void onSuiteStart(Suite aSuite) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onSuiteStart(aSuite);
		}
	}

	@Override
	public void onSetupStart(SuiteDefinition aSetupSuite) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onSetupStart(aSetupSuite);
		}
	}

	@Override
	public void onSetupFinish(SuiteDefinition aSetupSuite, SuiteResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onSetupFinish(aSetupSuite, aResult);
		}
	}

	@Override
	public void onTestStart(Test aTest) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTestStart(aTest);
		}
	}

	@Override
	public void onTestFinish(Test aTest, TestResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTestFinish(aTest, aResult);
		}
	}

	@Override
	public void onCallStart(Call aCall) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onCallStart(aCall);
		}
	}

	@Override
	public void onCallFinish(Call aCall, CallResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onCallFinish(aCall, aResult);
		}
	}

	@Override
	public void onTearDownStart(SuiteDefinition aTearDownSuite) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTearDownStart(aTearDownSuite);
		}
	}

	@Override
	public void onTearDownFinish(SuiteDefinition aTearDownSuite, SuiteResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTearDownFinish(aTearDownSuite, aResult);
		}
	}

	@Override
	public void onSuiteFinish(Suite aSuite, SuiteResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onSuiteFinish(aSuite, aResult);
		}
	}

	@Override
	public void onExecutionFinish(TestModel aModel, SuiteResult aResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onExecutionFinish(aModel, aResult);
		}
	}

	@Override
	public void onVariableDefinition(VariableEntity aDefinition, SuiteDefinition aSuite, Object anInitialValue) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onVariableDefinition(aDefinition, aSuite, anInitialValue);
		}
	}

	@Override
	public void onTableTestStart(TableTest aTableTest) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTableTestStart(aTableTest);
		}
	}

	@Override
	public void onTableTestFinish(TableTest aTableTest, TestResult someResults) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTableTestFinish(aTableTest, someResults);
		}
	}

	@Override
	public void onTableTestRowStart(TableTest aTableTest, TableTestRow aRow) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTableTestRowStart(aTableTest, aRow);
		}
	}

	@Override
	public void onTableTestRowFinish(TableTest aTableTest, TableTestRow aRow, TestSubResult aSubResult) {
		for (TestRunnerCallback tempCallback : callbacks) {
			tempCallback.onTableTestRowFinish(aTableTest, aRow, aSubResult);
		}
	}

}