/**
 * 
 */
package de.gebit.integrity.eclipse.running;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import de.gebit.integrity.eclipse.Activator;

/**
 * The configuration dialog for the "launch-and-connect" feature.
 * 
 * @author Rene Schneider
 * 
 */
public class TestActionConfigurationDialog extends Dialog {

	/**
	 * The launch configurations found in the workspace.
	 */
	private java.util.List<ILaunchConfiguration> launchConfigurations = new ArrayList<ILaunchConfiguration>();

	/**
	 * The list that displays all launch configurations.
	 */
	private List launchConfigList;

	/**
	 * The selected launch config.
	 */
	private ILaunchConfiguration selectedConfiguration;

	/**
	 * Creates a new instance.
	 * 
	 * @param parentShell
	 */
	protected TestActionConfigurationDialog(IShellProvider aParentShell) {
		super(aParentShell);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param aParentShell
	 */
	public TestActionConfigurationDialog(Shell aParentShell) {
		super(aParentShell);
	}

	@Override
	public void create() {
		setShellStyle(getShellStyle() | SWT.RESIZE);
		super.create();
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();

		IDialogSettings tempSettings = Activator.getDefault().getDialogSettings()
				.getSection(TestActionConfigurationDialog.class.getName());
		int tempWidth = 400;
		int tempHeight = 500;
		if (tempSettings != null) {
			try {
				tempWidth = tempSettings.getInt("width");
				tempHeight = tempSettings.getInt("height");
			} catch (NumberFormatException exc) {
				exc.printStackTrace();
				tempWidth = 400;
				tempHeight = 500;
			}
		} else {
			tempSettings = Activator.getDefault().getDialogSettings()
					.addNewSection(TestActionConfigurationDialog.class.getName());
			tempSettings.put("width", tempWidth);
			tempSettings.put("height", tempHeight);
		}

		final Shell tempShell = getShell();
		tempShell.setSize(tempWidth, tempHeight);
		Monitor tempMonitor = tempShell.getMonitor();
		Rectangle tempBounds = tempMonitor.getBounds();
		Rectangle tempRect = tempShell.getBounds();
		int tempX = tempBounds.x + (tempBounds.width - tempRect.width) / 2;
		int tempY = tempBounds.y + (tempBounds.height - tempRect.height) / 2;
		tempShell.setLocation(tempX, tempY);

		final IDialogSettings tempFinalSettings = tempSettings;
		tempShell.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent anEvent) {
				tempFinalSettings.put("width", tempShell.getSize().x);
				tempFinalSettings.put("height", tempShell.getSize().y);
			}

			@Override
			public void controlMoved(ControlEvent anEvent) {

			}
		});
	}

	@Override
	protected void configureShell(Shell aShell) {
		super.configureShell(aShell);
		aShell.setText("Launch Configuration to run");
	}

	@Override
	protected Layout getLayout() {
		GridLayout tempLayout = new GridLayout();
		tempLayout.marginLeft = 2;
		tempLayout.marginRight = 2;
		tempLayout.marginTop = 2;
		tempLayout.marginBottom = 2;

		return tempLayout;
	}

	@Override
	protected Control createDialogArea(Composite aParent) {
		launchConfigList = new List(aParent, SWT.BORDER | SWT.V_SCROLL);
		GridData tempGrid = new GridData();
		tempGrid.grabExcessHorizontalSpace = true;
		tempGrid.horizontalAlignment = GridData.FILL;
		tempGrid.grabExcessVerticalSpace = true;
		tempGrid.verticalAlignment = GridData.FILL;
		launchConfigList.setLayoutData(tempGrid);

		DebugPlugin tempDebugPlugin = DebugPlugin.getDefault();
		ILaunchManager tempLaunchManager = tempDebugPlugin.getLaunchManager();
		ILaunchConfigurationType tempJavaLaunchConfigType = tempLaunchManager
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

		launchConfigurations.clear();
		try {
			for (ILaunchConfiguration tempLaunchConfig : tempLaunchManager.getLaunchConfigurations()) {
				if (tempLaunchConfig.getType() == tempJavaLaunchConfigType) {
					launchConfigList.add(tempLaunchConfig.getName());
					launchConfigurations.add(tempLaunchConfig);
				}
			}
		} catch (CoreException exc) {
			throw new RuntimeException(exc);
		}

		return aParent;
	}

	@Override
	protected void okPressed() {
		if (launchConfigList.getSelectionIndex() >= 0) {
			selectedConfiguration = launchConfigurations.get(launchConfigList.getSelectionIndex());
		} else {
			selectedConfiguration = null;
		}
		setReturnCode(OK);
		close();
	}

	public ILaunchConfiguration getSelectedConfiguration() {
		return selectedConfiguration;
	}

}
