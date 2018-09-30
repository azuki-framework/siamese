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

import org.azkfw.siamese.browser.Window;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author Kawakicchi
 */
public class SeleniumWindow implements Window {

	private final String handleId;

	private final WebDriver driver;

	/**
	 * コンストラクタ
	 *
	 * @param handleId ハンドルID
	 * @param driver ドライバ
	 */
	public SeleniumWindow(final String handleId, final WebDriver driver) {
		this.handleId = handleId;
		this.driver = driver;
	}

	@Override
	public String title() {
		driver.switchTo().window(handleId);

		return driver.getTitle();
	}

	@Override
	public String source() {
		driver.switchTo().window(handleId);

		return driver.getPageSource();
	}

	@Override
	public void move(final int x, final int y) {
		driver.switchTo().window(handleId);

		driver.manage().window().setPosition(new Point(x, y));
	}

	@Override
	public void size(final int width, final int height) {
		driver.switchTo().window(handleId);

		driver.manage().window().setSize(new Dimension(width, height));
	}

	@Override
	public void input(final String name, final String value) {
		driver.switchTo().window(handleId);

		final WebElement e = driver.findElement(by(name));

		new Actions(driver).moveToElement(e).perform();

		// e.clear();

		new Actions(driver).sendKeys(e, value).perform();
	}

	@Override
	public void click(final String name) {
		driver.switchTo().window(handleId);

		final WebElement e = driver.findElement(by(name));

		new Actions(driver).moveToElement(e).click(e).perform();

	}

	private By by(final String name) {
		if (name.startsWith("id:")) {
			return By.id(name.substring(3));
		} else if (name.startsWith("name:")) {
			return By.name(name.substring(5));
		} else {
			return By.name(name);
		}
	}
}
