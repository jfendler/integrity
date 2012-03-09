/**
 * 
 */
package de.gebit.integrity.runner.forking;

import java.io.InputStream;

/**
 * A forked process implementation that wraps a local process.
 * 
 * @author Rene Schneider
 * 
 */
public class LocalForkedProcess implements ForkedProcess {

	/**
	 * The OS process encapsulated.
	 */
	protected Process process;

	/**
	 * Creates a new instance.
	 * 
	 * @param aProcess
	 *            the process to wrap
	 */
	public LocalForkedProcess(Process aProcess) {
		process = aProcess;
	}

	@Override
	public boolean isAlive() {
		boolean tempIsAlive = false;
		try {
			process.exitValue();
		} catch (IllegalThreadStateException exc) {
			tempIsAlive = true;
		}
		return tempIsAlive;
	}

	@Override
	public void kill() {
		process.destroy();
	}

	@Override
	public InputStream getInputStream() {
		return process.getInputStream();
	}

	@Override
	public InputStream getErrorStream() {
		return process.getErrorStream();
	}

}
