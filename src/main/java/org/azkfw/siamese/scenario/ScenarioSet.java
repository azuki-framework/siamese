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
package org.azkfw.siamese.scenario;

import java.util.List;

import org.azkfw.siamese.exception.ScenarioNotFoundException;

/**
 * シナリオセット機能を定義したインターフェース
 * 
 * @author Kawakicchi
 */
public interface ScenarioSet extends Iterable<Scenario> {

	/**
	 * シナリオ一覧 を取得する。
	 *
	 * @return シナリオ一覧
	 */
	List<Scenario> getScenarios();

	/**
	 * シナリオ を取得する。
	 *
	 * @param name シナリオ名
	 * @return シナリオ
	 * @throws ScenarioNotFoundException シナリオが見つからない場合
	 */
	Scenario getScenario(String name) throws ScenarioNotFoundException;
}
