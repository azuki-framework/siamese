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
package org.azkfw.siamese.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kawakicchi
 */
public abstract class AbstractScenarioRunner implements ScenarioRunner {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(AbstractScenarioRunner.class);

	public AbstractScenarioRunner() {
	}

	@Override
	public final void run(final String name) {
		doRunScenario(name);
	}

	@Override
	public final void run(final String... names) {
		for (String name : names) {
			doRunScenario(name);
		}
	}

	/**
	 * シナリオを実行する。
	 *
	 * @param name シナリオ名
	 */
	protected abstract void doRunScenario(final String name);
}
