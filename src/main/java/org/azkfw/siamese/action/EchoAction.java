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
package org.azkfw.siamese.action;

/**
 *
 * @author Kawakicchi
 */
public class EchoAction extends AbstractAction {

	/** メッセージ */
	private String message;

	public EchoAction() {

	}

	public EchoAction(final String message) {
		this.message = message;
	}

	/**
	 * メッセージを設定する。
	 *
	 * @param message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	protected void doExecute() {

		final String dMessage = decoration(message);

		System.out.println(dMessage);
	}

}
