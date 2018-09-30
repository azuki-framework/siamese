/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.siamese.action.browser;

import org.azkfw.siamese.action.Action;
import org.azkfw.siamese.browser.Browser;
import org.azkfw.siamese.browser.selenium.SeleniumBrowser;
import org.azkfw.siamese.plugin.AbstractPlugin;
import org.azkfw.siamese.scenario.Command;
import org.azkfw.siamese.util.SiameseUtil;

/**
 *
 * @author Kawakicchi
 */
public class BrowserPlugin extends AbstractPlugin {

	private Browser browser;

	@Override
	protected void doLoad() {
		browser = new SeleniumBrowser();
	}

	@Override
	protected void doInitialize() {

	}

	@Override
	protected void doTerminate() {
		browser.close();
	}

	@Override
	protected Action doGetAction(final Command command) {
		final String name = command.getName();

		if (SiameseUtil.isEquals(name, "BROWSER.INPUT")) {
			final BrowserInputAction a = new BrowserInputAction();
			a.setName(command.getArguments().get(0));
			a.setValue(command.getArguments().get(1));
			return a;
		} else if (SiameseUtil.isEquals(name, "BROWSER.CLICK")) {
			final BrowserClickAction a = new BrowserClickAction();
			a.setName(command.getArguments().get(0));
			return a;
		} else if (SiameseUtil.isEquals(name, "BROWSER.OPEN")) {
			final BrowserOpenAction a = new BrowserOpenAction();
			a.setUrl(command.getArguments().get(0));
			return a;
		} else if (SiameseUtil.isEquals(name, "BROWSER.CLOSE")) {
			final BrowserCloseAction a = new BrowserCloseAction();
			return a;
		} else if (SiameseUtil.isEquals(name, "BROWSER.WINDOW")) {
			final BrowserWindowAction a = new BrowserWindowAction();
			a.setTitle(command.getArguments().get(0));
			return a;
		}

		return null;
	}

	@Override
	protected void doSupport(final Action action) {
		if (action instanceof BrowserSupport) {
			((BrowserSupport) action).setBrowser(browser);
		}
	}
}
