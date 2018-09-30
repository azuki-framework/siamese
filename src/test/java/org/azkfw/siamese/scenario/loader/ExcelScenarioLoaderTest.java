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
package org.azkfw.siamese.scenario.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.azkfw.siamese.exception.ScenarioFormatException;
import org.azkfw.siamese.scenario.Command;
import org.azkfw.siamese.scenario.Scenario;
import org.azkfw.siamese.scenario.ScenarioSet;
import org.azkfw.siamese.scenario.loader.ExcelScenarioLoader.ExcelScenarioLoaderBuilder;
import org.azkfw.siamese.test.AbstractTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * {@link ExcelScenarioLoader} の検証を行うためのクラス
 * 
 * @author Kawakicchi
 */
@RunWith(JUnit4.class)
public class ExcelScenarioLoaderTest extends AbstractTestCase {

	@Test
	public void nodata_noTargetDirectory() throws ScenarioFormatException {
		ExcelScenarioLoaderBuilder builder = ExcelScenarioLoaderBuilder.newBuilder();

		ExcelScenarioLoader loader = builder.build();
		loader.load();

		ScenarioSet scenarioSet = loader.getScenarioSet();

		assertNotNull(scenarioSet);
		assertNotNull(scenarioSet.getScenarios());
		assertEquals(0, scenarioSet.getScenarios().size());
	}

	@Test
	public void nodata_targetDirectoryNoFile() throws ScenarioFormatException {
		ExcelScenarioLoaderBuilder builder = ExcelScenarioLoaderBuilder.newBuilder();
		builder.addTargetDirectory(Paths.get("src", "test", "resources", "org", "azkfw", "siamese", "scenario", "loader", "01").toFile());

		ExcelScenarioLoader loader = builder.build();
		loader.load();

		ScenarioSet scenarioSet = loader.getScenarioSet();

		assertNotNull(scenarioSet);
		assertNotNull(scenarioSet.getScenarios());
		assertEquals(0, scenarioSet.getScenarios().size());
	}

	@Test
	public void test() throws Exception {
		ExcelScenarioLoaderBuilder builder = ExcelScenarioLoaderBuilder.newBuilder();
		builder.addTargetDirectory(Paths.get("src", "test", "resources", "org", "azkfw", "siamese", "scenario", "loader", "02").toFile());

		ExcelScenarioLoader loader = builder.build();
		loader.load();

		ScenarioSet scenarioSet = loader.getScenarioSet();

		assertNotNull(scenarioSet);

		List<Scenario> scenarios = scenarioSet.getScenarios();
		assertNotNull(scenarios);
		assertEquals(1, scenarios.size());

		Scenario scenario = scenarioSet.getScenario("サンプルシナリオ");
		assertNotNull(scenario);
		assertEquals("サンプルシナリオ", scenario.getName());
		assertEquals("このシナリオはサンプルです。", scenario.getMemo());

		List<Command> commands = scenario.getCommands();
		assertNotNull(commands);
		assertEquals(5, commands.size());

		Command command = null;
		List<String> arguments = null;

		// Comman1
		command = commands.get(0);
		assertNotNull(command);
		assertEquals("コマンド1", command.getName());
		arguments = command.getArguments();
		assertNotNull(arguments);
		assertEquals(5, arguments.size());
		assertEquals("", arguments.get(0));
		assertEquals("", arguments.get(1));
		assertEquals("", arguments.get(2));
		assertEquals("", arguments.get(3));
		assertEquals("", arguments.get(4));
		assertEquals("", command.getOption());
		assertFalse(command.isSkip());
		// Comman2
		command = commands.get(1);
		assertNotNull(command);
		assertEquals("コマンド2", command.getName());
		arguments = command.getArguments();
		assertNotNull(arguments);
		assertEquals(5, arguments.size());
		assertEquals("引数1", arguments.get(0));
		assertEquals("引数2", arguments.get(1));
		assertEquals("引数3", arguments.get(2));
		assertEquals("引数4", arguments.get(3));
		assertEquals("引数5", arguments.get(4));
		assertEquals("オプション", command.getOption());
		assertFalse(command.isSkip());
		// Comman3
		command = commands.get(2);
		assertNotNull(command);
		assertEquals("コマンド3", command.getName());
		arguments = command.getArguments();
		assertNotNull(arguments);
		assertEquals(5, arguments.size());
		assertEquals("引数1", arguments.get(0));
		assertEquals("", arguments.get(1));
		assertEquals("", arguments.get(2));
		assertEquals("", arguments.get(3));
		assertEquals("引数5", arguments.get(4));
		assertEquals("", command.getOption());
		assertFalse(command.isSkip());
		// Comman4
		command = commands.get(3);
		assertNotNull(command);
		assertEquals("コマンド4", command.getName());
		arguments = command.getArguments();
		assertNotNull(arguments);
		assertEquals(5, arguments.size());
		assertEquals("", arguments.get(0));
		assertEquals("引数2", arguments.get(1));
		assertEquals("", arguments.get(2));
		assertEquals("引数4", arguments.get(3));
		assertEquals("", arguments.get(4));
		assertEquals("", command.getOption());
		assertFalse(command.isSkip());
		// Comman5
		command = commands.get(4);
		assertNotNull(command);
		assertEquals("コマンド5", command.getName());
		arguments = command.getArguments();
		assertNotNull(arguments);
		assertEquals(5, arguments.size());
		assertEquals("", arguments.get(0));
		assertEquals("", arguments.get(1));
		assertEquals("", arguments.get(2));
		assertEquals("", arguments.get(3));
		assertEquals("", arguments.get(4));
		assertEquals("", command.getOption());
		assertTrue(command.isSkip());

	}
}
