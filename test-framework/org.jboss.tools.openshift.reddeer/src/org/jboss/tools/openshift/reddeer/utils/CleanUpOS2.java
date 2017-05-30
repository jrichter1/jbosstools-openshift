/*******************************************************************************
 * Copyright (c) 2007-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.utils;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.exception.JFaceLayerException;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Connection;
import org.junit.After;
import org.junit.Test;

/** 
 * This "test" perform clean up. Clean up consists of deletion of domains 
 * with applications on the OpenShift v2 connections to let the connections 
 * be prepared for another run.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CleanUpOS2 {

	@Test
	public void test() {
		// NOTHING TO DO
	}
	
	@After
	public void cleanUp() {
		performCleanUp(DatastoreOS2.USERNAME);
		performCleanUp(DatastoreOS2.X_USERNAME);
	}
	
	public void performCleanUp(String username) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift2Connection connection = null;
		try {
			 connection = explorer.getOpenShift2Connection(username);
		} catch (JFaceLayerException ex) {
			// There is no connection with such username, nothing happens
		}
		
		if (connection != null) {
			connection.refresh();
			connection.expand();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			for (TreeItem domains: connection.getTreeItem().getItems()) {
				domains.select();
			
				new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_DOMAIN).select();
				
				new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.DELETE_DOMAIN));
				
				new DefaultShell(OpenShiftLabel.Shell.DELETE_DOMAIN).setFocus();
				if (!new CheckBox(0).isChecked()) {
					new CheckBox(0).click();
				}
				new OkButton().click();
				
				new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
			}
			
			connection.select();
			
			removeSSHKeys();
		}
	}
	
	private void removeSSHKeys() {
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_SSH_KEYS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		
		new PushButton(OpenShiftLabel.Button.REFRESH).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		while (new DefaultTable().getItems().size() != 0) {			
			new DefaultTable().getItem(0).select();
			
			new WaitUntil(new ControlIsEnabled(new PushButton(OpenShiftLabel.Button.REMOVE)));
			
			new PushButton(OpenShiftLabel.Button.REMOVE).click();
			
			new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.REMOVE_SSH_KEY));
			
			new DefaultShell(OpenShiftLabel.Shell.REMOVE_SSH_KEY);
			
			new OkButton().click();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
			
			new PushButton(OpenShiftLabel.Button.REFRESH).click();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			new DefaultShell(OpenShiftLabel.Shell.MANAGE_SSH_KEYS);
		}
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.MANAGE_SSH_KEYS), TimePeriod.LONG);
	}
}
