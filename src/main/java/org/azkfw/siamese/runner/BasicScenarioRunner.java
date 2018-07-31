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

import org.azkfw.siamese.action.Action;
import org.azkfw.siamese.action.EchoAction;
import org.azkfw.siamese.action.parameter.ParameterAction;
import org.azkfw.siamese.action.parameter.ParameterSupport;
import org.azkfw.siamese.exception.CommandNotFoundException;
import org.azkfw.siamese.exception.ScenarioNotFoundException;
import org.azkfw.siamese.parameter.BasicParameter;
import org.azkfw.siamese.parameter.Parameter;
import org.azkfw.siamese.plugin.Plugin;
import org.azkfw.siamese.scenario.Command;
import org.azkfw.siamese.scenario.Scenario;
import org.azkfw.siamese.scenario.ScenarioSet;
import org.azkfw.siamese.util.SiameseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kawakicchi
 */
public class BasicScenarioRunner extends AbstractScenarioRunner {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(BasicScenarioRunner.class);

	private final ScenarioSet scenarioSet;

	private Parameter parameter;

	/**
	 * コンストラクタ
	 *
	 * @param scenarioSet シナリオセット
	 */
	private BasicScenarioRunner(final ScenarioSet scenarioSet) {
		this.scenarioSet = scenarioSet;
	}

	@Override
	protected void doRunScenario(final String name) {

		parameter = new BasicParameter();
		parameter.put("SCENARIO.NAME", name);

		try {
			logger.debug("run scenario.[{}]", name);
			final Scenario scenario = scenarioSet.getScenario(name);

			runScenario(scenario, scenario);

		} catch (ScenarioNotFoundException ex) {
			logger.error("Not found scenaio.[{}]", name);
		}
	}

	private void runSubScenario(final String name, final Scenario topScenario) {
		try {
			logger.debug("run sub scenario.[{}]", name);
			final Scenario scenario = scenarioSet.getScenario(name);

			runScenario(scenario, topScenario);

		} catch (ScenarioNotFoundException ex) {
			logger.error("Not found sub scenaio.[{}]", name);
		}
	}

	private void runScenario(final Scenario scenario, final Scenario topScenario) {

		for (Command command : scenario) {
			execCommand(command, scenario, topScenario);
		}
	}

	private void execCommand(final Command command, final Scenario scenario, final Scenario topScenario) {

		try {
			logger.debug("execute command.[{}]", command.getName());

			if (SiameseUtil.isEquals(command.getName(), "SCENARIO")) {

				runSubScenario(command.getArguments().get(0), topScenario);

			} else {
				final Action action = getAction(command);

				execAction(action);
			}

		} catch (CommandNotFoundException ex) {
			logger.error("Not found command.[{}]", command.getName());
		}

	}

	private void execAction(final Action action) {
		if (action instanceof ParameterSupport) {
			((ParameterSupport) action).setParameter(parameter);
		}
		for (Plugin p : getPlugins()) {
			p.support(action);
		}

		action.execute();
	}

	private Action getAction(final Command command) throws CommandNotFoundException {
		final String name = command.getName();
		if (SiameseUtil.isEquals(name, "ECHO")) {
			final EchoAction a = new EchoAction();
			a.setMessage(command.getArguments().get(0));
			return a;
		} else if (SiameseUtil.isEquals(name, "PARAMETER")) {
			final ParameterAction a = new ParameterAction();
			a.setName(command.getArguments().get(0));
			a.setValue(command.getArguments().get(1));
			return a;
		} else {
			Action action = null;
			for (Plugin plugin : getPlugins()) {
				action = plugin.getAction(command);
				if (null != action) {
					return action;
				}
			}

			throw new CommandNotFoundException();
		}
	}

	public static class BasicScenarioRunnerBuilder {

		private ScenarioSet scenarioSet;

		public static BasicScenarioRunnerBuilder newBuilder() {
			return new BasicScenarioRunnerBuilder();
		}

		public BasicScenarioRunnerBuilder setScenarioSet(final ScenarioSet scenarioSet) {
			this.scenarioSet = scenarioSet;
			return this;
		}

		public BasicScenarioRunner build() {
			return new BasicScenarioRunner(scenarioSet);
		}
	}
}
