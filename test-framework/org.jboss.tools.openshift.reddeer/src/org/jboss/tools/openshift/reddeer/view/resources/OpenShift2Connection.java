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
package org.jboss.tools.openshift.reddeer.view.resources;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

public class OpenShift2Connection extends AbstractOpenShiftConnection {

	public OpenShift2Connection(TreeItem connectionItem) {
		super(connectionItem);
	}
	
	/** 
	 * Gets domain with specified user name and domain name.
	 * 
	 * @param domain domain name
	 * @return OpenShift 2 domain
	 */
	public Domain getDomain(String domain) {
		activateOpenShiftExplorerView();
		item.select();
		item.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		return new Domain(treeViewerHandler.getTreeItem(item, domain));
	}
}
