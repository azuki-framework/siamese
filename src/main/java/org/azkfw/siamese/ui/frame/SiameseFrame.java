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
package org.azkfw.siamese.ui.frame;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

/**
 *
 * @author Kawakicchi
 */
@SuppressWarnings("serial")
public class SiameseFrame extends JFrame {

	public SiameseFrame() {
		setTitle("Siamese");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		System.setProperty("apple.awt.application.name", "Siamese");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Siamese");
		System.setProperty("com.apple.macos.useScreenMenuBar", "true");
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle rect = env.getMaximumWindowBounds();
		setBounds(rect);
	}
}
