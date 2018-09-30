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

/**
 *
 * @author Kawakicchi
 */
public class BrowserInputAction extends AbstractBrowserAction {

	/** 名前 */
	private String name;
	/** 値 */
	private String value;

	/**
	 * 名前 を設定する。
	 *
	 * @param name 名前
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 値 を設定する。
	 *
	 * @param value 値
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public final void doExecute() {
		final String dName = decoration(name);
		final String dValue = decoration(value);

		getBrowser().window().input(dName, dValue);
	}
}
