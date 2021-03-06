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
package org.azkfw.siamese.browser;

import java.util.List;

/**
 * このインターフェースは、ブラウザ機能を定義するインターフェースです。
 * 
 * @author Kawakicchi
 */
public interface Browser {

	/**
	 * ブラウザを開く。
	 * 
	 * @param url URL
	 */
	void open(String url);

	/**
	 * ブラウザを閉じる。
	 */
	void close();

	/**
	 * ウィンドウ一覧を取得する。
	 *
	 * @return ウィンドウ一覧
	 */
	List<Window> windows();

	/**
	 * タイトル名からウィンドウを取得する。
	 *
	 * @param title タイトル名
	 * @return ウィンドウ
	 */
	Window window(String title);

	/**
	 * カレントウインドウを取得する。
	 *
	 * @return ウインドウ
	 */
	Window window();
}
