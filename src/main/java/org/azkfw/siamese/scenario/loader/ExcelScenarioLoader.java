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

import static org.azkfw.siamese.util.SiameseUtil.isNotEmpty;
import static org.azkfw.siamese.util.SiameseUtil.trim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.azkfw.siamese.excel.ExcelUtil;
import org.azkfw.siamese.exception.ScenarioFormatException;
import org.azkfw.siamese.exception.ScenarioNotFoundException;
import org.azkfw.siamese.scenario.Command;
import org.azkfw.siamese.scenario.Scenario;
import org.azkfw.siamese.scenario.ScenarioSet;
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

	/** ディレクトリ一覧 */
	private final List<File> directories;

	/** シナリオセット */
	private final MyScenarioSet scenarioSet;

	private ExcelScenarioLoader(final List<File> directorys) {
		this.directories = new ArrayList<File>(directorys);
		this.scenarioSet = new MyScenarioSet();
	}

	@Override
	public final ScenarioSet getScenarioSet() {
		return scenarioSet;
	}

	@Override
	protected void doLoad() throws ScenarioFormatException {
		for (File dir : directories) {
			final List<File> files = getFiles(dir);
			for (File file : files) {
				doLoad(file);
			}
		}
	}

	private boolean addScenario(final Scenario scenario) {
		final boolean result = scenarioSet.addScenario(scenario);
		if (result) {
			logger.debug("load scenario.[{}]", scenario.getName());
		} else {
			logger.warn("Duplicate scenario name.[{}]", scenario.getName());
		}
		return result;
	}

	private void doLoad(final File file) throws ScenarioFormatException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			final Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
			load(workbook);
		} catch (FileNotFoundException ex) {
			throw new ScenarioFormatException("Scenario excel file not found.", ex);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException ex) {
					logger.warn("File close error.", ex);
				}
			}
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

					boolean skip = "#".equals(control);

					final MyCommand command = new MyCommand(cmdName, arguments, option, skip);
					commands.add(command);
				}
				break;
			}
			}
		}

		final MyScenario scenario = new MyScenario(name, memo, commands);
		addScenario(scenario);
	}

	private List<File> getFiles(final File file) {
		final List<File> files = new ArrayList<File>();
		if (file.isDirectory()) {
			dir(file, files);
		} else if (file.isFile()) {
			file(file, files);
		} else {
			logger.warn("Unknown file.[{}]", file);
		}
		return files;
	}

	private void file(final File file, final List<File> files) {
		final String name = file.getName().toLowerCase();
		if (!name.endsWith(".xlsx")) {
			return;
		}
		if (name.startsWith("~$")) {
			return;
		}
		files.add(file);
	}

	private void dir(final File dir, final List<File> files) {
		final String name = dir.getName();
		if (name.startsWith(".")) {
			return;
		}

		final File[] fs = dir.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) {
				dir(f, files);
			} else if (f.isFile()) {
				file(f, files);
			}
		}
	}

	private static class MyScenarioSet implements ScenarioSet {

		private final Map<String, Scenario> map;
		private final List<Scenario> list;

		public MyScenarioSet() {
			map = new HashMap<String, Scenario>();
			list = new ArrayList<Scenario>();
		}

		public boolean addScenario(final Scenario scenario) {
			boolean result = false;
			if (!map.containsKey(scenario.getName())) {
				map.put(scenario.getName(), scenario);
				list.add(scenario);
				result = true;
			} else {

			}
			return result;
		}

		@Override
		public List<Scenario> getScenarios() {
			return new ArrayList<Scenario>(list);
		}

		@Override
		public Scenario getScenario(final String name) throws ScenarioNotFoundException {
			Scenario scenario = null;
			if (map.containsKey(name)) {
				scenario = map.get(name);
			}
			if (null == scenario) {
				throw new ScenarioNotFoundException(String.format("Not found scenario.[%s]", name));
			}
			return scenario;
		}

		@Override
		public Iterator<Scenario> iterator() {
			return list.iterator();
		}
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

		@Override
		public Iterator<Command> iterator() {
			return commands.iterator();
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
