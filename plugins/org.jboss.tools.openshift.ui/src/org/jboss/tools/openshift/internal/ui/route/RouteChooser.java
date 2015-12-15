/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.internal.ui.route;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.openshift.core.IRouteChooser;
import org.jboss.tools.openshift.internal.ui.dialog.SelectRouteDialog;

import com.openshift.restclient.model.route.IRoute;

/**
 * @author Andre Dietisheim
 */
public class RouteChooser implements IRouteChooser {

	private static final String NO_ROUTE_MSG = "Could not find a route that points to an url to show in a browser.";

	private Shell shell;

	public RouteChooser() {
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	}

	public RouteChooser(Shell shell) {
		this.shell = shell;
	}

	@Override
	public IRoute chooseRoute(List<IRoute> routes) {
		SelectRouteDialog routeDialog = new SelectRouteDialog(routes, shell);
		if (Dialog.OK == routeDialog.open()) {
			return routeDialog.getSelectedRoute();
		} else {
			return null;
		}
	}

	@Override
	public void noRouteErrorDialog() {
		MessageDialog.openWarning(shell, "No route to open", NO_ROUTE_MSG);
	}

}