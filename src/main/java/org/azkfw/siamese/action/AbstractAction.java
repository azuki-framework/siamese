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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.azkfw.siamese.action.parameter.ParameterSupport;
import org.azkfw.siamese.parameter.Parameter;

/**
 * アクション機能を定義する為の基底クラス
 * 
 * @author Kawakicchi
 */
public abstract class AbstractAction implements Action, ParameterSupport {

	/** パラメータ */
	private Parameter parameter;

	/**
	 * コンストラクタ
	 */
	public AbstractAction() {

	}

	@Override
	public final void setParameter(final Parameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * パラメータを取得する。
	 *
	 * @return パラメータ
	 */
	protected final Parameter getParameter() {
		return parameter;
	}

	protected final void setParameter(final String name, final Object value) {
		parameter.put(name, value);
	}

	protected final String decoration(final String str) {
		final Pattern p = Pattern.compile("(\\$\\{([^\\}]*)\\})");

		final Matcher m = p.matcher(str);
		final StringBuilder s = new StringBuilder();
		int i = 0;
		while (m.find(i)) {
			s.append(str.substring(i, m.start(1)));

			String r = parameter.getString(m.group(2));
			if (null != r) {
				s.append(r);
			} else {
				s.append(m.group(1));
			}

			i = m.end(1);
		}
		s.append(str.substring(i));
		return s.toString();
	}

	@Override
	public final void execute() {
		doExecute();
	}

	/**
	 * アクションを実行する。
	 */
	protected abstract void doExecute();
}
