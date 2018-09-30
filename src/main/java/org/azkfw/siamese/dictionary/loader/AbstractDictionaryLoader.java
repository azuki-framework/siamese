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
package org.azkfw.siamese.dictionary.loader;

import org.azkfw.siamese.exception.DictionaryFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ディクショナリ読込み機能を実装する為の基底クラス
 * 
 * @author Kawakicchi
 */
public abstract class AbstractDictionaryLoader implements DictionaryLoader {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(AbstractDictionaryLoader.class);

	/**
	 * コンストラクタ
	 */
	public AbstractDictionaryLoader() {
	}

	@Override
	public final void load() throws DictionaryFormatException {
		try {
			doLoad();
		} catch (DictionaryFormatException ex) {
			throw ex;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new DictionaryFormatException(ex);
		}
	}

	/**
	 * シナリオをロードする。
	 * 
	 * @throws DictionaryFormatException シナリオが不正な場合
	 */
	protected abstract void doLoad() throws DictionaryFormatException;

}
