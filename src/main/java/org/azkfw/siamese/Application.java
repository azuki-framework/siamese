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
package org.azkfw.siamese;

import java.io.File;

import org.azkfw.siamese.browser.Browser;
import org.azkfw.siamese.browser.Window;
import org.azkfw.siamese.browser.selenium.SeleniumBrowser;
import org.azkfw.siamese.runner.BasicScenarioRunner;
import org.azkfw.siamese.runner.BasicScenarioRunner.BasicScenarioRunnerBuilder;
import org.azkfw.siamese.scenario.ScenarioSet;
import org.azkfw.siamese.scenario.loader.ExcelScenarioLoader;
import org.azkfw.siamese.scenario.loader.ExcelScenarioLoader.ExcelScenarioLoaderBuilder;
import org.azkfw.siamese.ui.frame.SiameseFrame;

/**
 *
 * @author Kawakicchi
 */
public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}

	public void run() {
		final Browser browser = new SeleniumBrowser();

		try {
			browser.open("http://google.co.jp");
			Thread.sleep(3 * 1000);
			for (Window win : browser.windows()) {
				System.out.println(win.title());
				System.out.println(win.source());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			browser.close();
		}
	}

	public void run2() {
		final SiameseFrame frm = new SiameseFrame();
		frm.setVisible(true);
	}

	public void run1() {
		final File dir = new File("/Users/Kawakicchi/workspace/workflow/siamese/tmp");

		final ExcelScenarioLoaderBuilder builderLoader = ExcelScenarioLoaderBuilder.newBuilder();
		builderLoader.addTargetDirectory(dir);

		final ExcelScenarioLoader loader = builderLoader.build();
		loader.load();

		final ScenarioSet scenarioSet = loader.getScenarioSet();

		final BasicScenarioRunnerBuilder builderRunner = BasicScenarioRunnerBuilder.newBuilder();
		builderRunner.setScenarioSet(scenarioSet);

		final BasicScenarioRunner runner = builderRunner.build();
		runner.run("サンプルシナリオ");
	}

}
