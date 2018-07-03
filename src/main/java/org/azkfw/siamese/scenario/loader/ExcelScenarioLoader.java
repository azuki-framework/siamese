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

import static org.azkfw.siamese.util.SiamesUtil.isNotEmpty;
import static org.azkfw.siamese.util.SiamesUtil.trim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.azkfw.siamese.excel.ExcelUtil;
import org.azkfw.siamese.scenario.Command;
import org.azkfw.siamese.scenario.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitorjbl.xlsx.StreamingReader;

/**
 *
 * @author Kawakicchi
 */
public class ExcelScenarioLoader extends AbstractScenarioLoader {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(ExcelScenarioLoader.class);

	private ExcelScenarioLoader(final List<File> directorys) {
		super(directorys);
	}

	@Override
	protected void doLoad(final File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			final Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
			load(workbook);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void load(final Workbook workbook) {
		for (Sheet sheet : workbook) {
			load(sheet);
		}
	}

	private void load(final Sheet sheet) {
		String name = null;
		String memo = null;
		final List<Command> commands = new ArrayList<Command>();

		int s = 0;
		for (Row row : sheet) {
			switch (s) {
			case 0: {
				Cell cell = row.getCell(0);
				if ("シナリオ名".equals(cell.getStringCellValue())) {
					cell = row.getCell(2);
					name = cell.getStringCellValue();
					s = 10;
				}
				break;
			}
			case 10: {
				Cell cell = row.getCell(0);
				if ("概要".equals(cell.getStringCellValue())) {
					s = 11;
				}
				break;
			}
			case 11: {
				Cell cell = row.getCell(0);
				if ("*".equals(cell.getStringCellValue())) {
					s = 20;
				} else {
					memo = cell.getStringCellValue();
					s = 19;
				}
				break;
			}
			case 19: {
				Cell cell = row.getCell(0);
				if ("*".equals(cell.getStringCellValue())) {
					s = 20;
				}
				break;
			}
			case 20: {
				final String control = trim(ExcelUtil.getValueToString(row.getCell(0)));
				final String cmdName = trim(ExcelUtil.getValueToString(row.getCell(1)));
				final String argument1 = trim(ExcelUtil.getValueToString(row.getCell(2)));
				final String argument2 = trim(ExcelUtil.getValueToString(row.getCell(3)));
				final String argument3 = trim(ExcelUtil.getValueToString(row.getCell(4)));
				final String argument4 = trim(ExcelUtil.getValueToString(row.getCell(5)));
				final String argument5 = trim(ExcelUtil.getValueToString(row.getCell(6)));
				final String option = trim(ExcelUtil.getValueToString(row.getCell(7)));

				if (isNotEmpty(cmdName)) {
					final List<String> arguments = new ArrayList<String>();
					arguments.add(argument1);
					arguments.add(argument2);
					arguments.add(argument3);
					arguments.add(argument4);
					arguments.add(argument5);

					final MyCommand command = new MyCommand(cmdName, arguments, option, false);
					commands.add(command);
				}
				break;
			}
			}
		}

		final MyScenario scenario = new MyScenario(name, memo, commands);
		addScenario(scenario);
	}

	private static class MyScenario implements Scenario {

		private final String name;
		private final String memo;
		private List<Command> commands;

		public MyScenario(final String name, final String memo, final List<Command> commands) {
			this.name = name;
			this.memo = memo;
			this.commands = new ArrayList<Command>(commands);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getMemo() {
			return memo;
		}

		@Override
		public List<Command> getCommands() {
			return commands;
		}
	}

	private static class MyCommand implements Command {

		private final String name;
		private final List<String> arguments;
		private final String option;
		private final boolean skip;

		public MyCommand(final String name, final List<String> arguments, final String option, final boolean skip) {
			this.name = name;
			this.arguments = new ArrayList<String>(arguments);
			this.option = option;
			this.skip = skip;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List<String> getArguments() {
			return arguments;
		}

		@Override
		public String getOption() {
			return option;
		}

		@Override
		public boolean isSkip() {
			return skip;
		}
	}

	public static class ExcelScenarioLoaderBuilder {

		private final List<File> directorys;

		private ExcelScenarioLoaderBuilder() {
			directorys = new ArrayList<File>();
		}

		public static ExcelScenarioLoaderBuilder newBuilder() {
			return new ExcelScenarioLoaderBuilder();
		}

		public ExcelScenarioLoaderBuilder addTargetDirectory(final File file) {
			directorys.add(file);
			return this;
		}

		public ExcelScenarioLoader build() {
			return new ExcelScenarioLoader(directorys);
		}
	}
}
