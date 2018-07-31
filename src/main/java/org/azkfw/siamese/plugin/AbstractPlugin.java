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
package org.azkfw.siamese.plugin;

import org.azkfw.siamese.action.Action;
import org.azkfw.siamese.scenario.Command;

/**
 *
 * @author Kawakicchi
 *
 */
public abstract class AbstractPlugin implements Plugin {

	@Override
	public void load() {
		doLoad();
	}

	@Override
	public void initialize() {
		doInitialize();
	}

	@Override
	public void terminate() {
		doTerminate();
	}

	@Override
	public Action getAction(final Command command) {
		return doGetAction(command);
	}

	@Override
	public void support(final Action action) {
		doSupport(action);
	}

	protected abstract void doLoad();

	protected abstract void doInitialize();

	protected abstract void doTerminate();

	protected abstract Action doGetAction(Command command);

	protected abstract void doSupport(Action action);
}
