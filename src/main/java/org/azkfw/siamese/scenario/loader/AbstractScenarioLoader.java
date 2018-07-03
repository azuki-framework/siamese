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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.azkfw.siamese.scenario.Scenario;
import org.azkfw.siamese.scenario.ScenarioSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kawakicchi
 */
public abstract class AbstractScenarioLoader implements ScenarioLoader {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(AbstractScenarioLoader.class);

	/** ディレクトリ一覧 */
	private final List<File> directories;
	/** シナリオセット */
	private final MyScenarioSet scenarioSet;

	public AbstractScenarioLoader(final List<File> directories) {
		this.directories = new ArrayList<File>(directories);
		this.scenarioSet = new MyScenarioSet();
	}

	@Override
	public final ScenarioSet getScenarioSet() {
		return scenarioSet;
	}

	@Override
	public final void load() {
		for (File dir : directories) {
			final List<File> files = getFiles(dir);
			for (File file : files) {
				doLoad(file);
			}
		}
	}

	/**
	 * シナリオファイルをロードする。
	 *
	 * @param file ファイル
	 */
	protected abstract void doLoad(final File file);

	protected final boolean addScenario(final Scenario scenario) {
		final boolean result = scenarioSet.addScenario(scenario);
		if (!result) {
			logger.warn("Duplicate scenario name.[{}]", scenario.getName());
		}
		return result;
	}

	private List<File> getFiles(final File file) {
		final List<File> files = new ArrayList<File>();
		if (file.isDirectory()) {
			dir(file, files);
		} else if (file.isFile()) {
			file(file, files);
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

		private final Map<String, Scenario> scenarios;

		public MyScenarioSet() {
			scenarios = new HashMap<String, Scenario>();
		}

		public boolean addScenario(final Scenario scenario) {
			boolean result = false;
			if (!scenarios.containsKey(scenario.getName())) {
				scenarios.put(scenario.getName(), scenario);
				result = true;
			}
			return result;
		}

		@Override
		public Scenario getScenario(final String name) {
			Scenario scenario = null;
			if (scenarios.containsKey(name)) {
				scenario = scenarios.get(name);
			}
			return scenario;
		}
	}
}
