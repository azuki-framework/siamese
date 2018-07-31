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
package org.azkfw.siamese.browser.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.azkfw.siamese.browser.Browser;
import org.azkfw.siamese.browser.Window;
import org.azkfw.siamese.util.SiameseUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * このクラスは、Seleniumを用意てブラウザ機能を提供するクラスです。
 *
 * @author Kawakicchi
 */
public class SeleniumBrowser implements Browser {

	/** WebDriver */
	private WebDriver driver;

	public SeleniumBrowser() {
	}

	@Override
	public void open(final String url) {
		if (null == driver) {
			System.setProperty("webdriver.chrome.driver", "./driver/mac/chromedriver");
			final ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver(options);
			driver.navigate().to(url);
		}
	}

	@Override
	public void close() {
		if (null != driver) {
			driver.close();
			driver.quit();
			driver = null;
		}
	}

	@Override
	public List<Window> windows() {
		final List<Window> windows = new ArrayList<Window>();
		final Set<String> ids = driver.getWindowHandles();
		for (String id : ids) {
			final SeleniumWindow window = new SeleniumWindow(id, driver);
			windows.add(window);
		}
		return windows;
	}

	@Override
	public Window window(final String title) {
		for (Window w : windows()) {
			final String t = w.title();
			if (SiameseUtil.isEquals(title, t)) {
				return w;
			}
		}
		return null;
	}
}
