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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Paths;
import java.util.List;

import org.azkfw.siamese.dictionary.Dictionary;
import org.azkfw.siamese.dictionary.DictionarySet;
import org.azkfw.siamese.dictionary.Item;
import org.azkfw.siamese.dictionary.loader.ExcelDictionaryLoader.ExcelDictionaryLoaderBuilder;
import org.azkfw.siamese.exception.DictionaryFormatException;
import org.azkfw.siamese.test.AbstractTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * {@link ExcelDictionaryLoader} の検証を行うためのクラス
 * 
 * @author Kawakicchi
 */
@RunWith(JUnit4.class)
public class ExcelDictionaryLoaderTest extends AbstractTestCase {

	@Test
	public void nodata_noTargetDirectory() throws DictionaryFormatException {
		ExcelDictionaryLoaderBuilder builder = ExcelDictionaryLoaderBuilder.newBuilder();

		ExcelDictionaryLoader loader = builder.build();
		loader.load();

		DictionarySet dictionarySet = loader.getDictionarySet();

		assertNotNull(dictionarySet);
		assertNotNull(dictionarySet.getDictionarys());
		assertEquals(0, dictionarySet.getDictionarys().size());
	}

	@Test
	public void test() throws Exception {
		ExcelDictionaryLoaderBuilder builder = ExcelDictionaryLoaderBuilder.newBuilder();
		builder.addTargetDirectory(Paths.get("src", "test", "resources", "org", "azkfw", "siamese", "dictionary", "loader", "02").toFile());

		ExcelDictionaryLoader loader = builder.build();
		loader.load();

		DictionarySet dictionarySet = loader.getDictionarySet();

		assertNotNull(dictionarySet);

		List<Dictionary> dictionaries = dictionarySet.getDictionarys();
		assertNotNull(dictionaries);
		assertEquals(1, dictionaries.size());

		Dictionary dictionary = dictionarySet.getDictionary("サンプルディクショナリ");
		assertNotNull(dictionary);
		assertEquals("サンプルディクショナリ", dictionary.getName());

		Item item = dictionary.lookup("名称１");
		assertNotNull(item);
		assertEquals("名称１", item.getName());
		assertEquals("item1", item.getValue());
	}
}
