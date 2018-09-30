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
import org.azkfw.siamese.dictionary.Dictionary;
import org.azkfw.siamese.dictionary.DictionarySet;
import org.azkfw.siamese.dictionary.Item;
import org.azkfw.siamese.excel.ExcelUtil;
import org.azkfw.siamese.exception.DictionaryFormatException;
import org.azkfw.siamese.exception.DictionaryNotFoundException;
import org.azkfw.siamese.util.SiameseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitorjbl.xlsx.StreamingReader;

/**
 * エクセルファイルのディクショナリを読込むローダークラス
 * 
 * @author Kawakicchi
 */
public class ExcelDictionaryLoader extends AbstractDictionaryLoader {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(ExcelDictionaryLoader.class);

	/** ディレクトリ一覧 */
	private final List<File> directories;

	/** ディクショナリセット */
	private final MyDictionarySet dictionarySet;

	/**
	 * コンストラクタ
	 *
	 * @param directorys ディクショナリファイルを格納したディレクトリ
	 */
	private ExcelDictionaryLoader(final List<File> directorys) {
		this.directories = new ArrayList<File>(directorys);
		this.dictionarySet = new MyDictionarySet();
	}

	@Override
	public final DictionarySet getDictionarySet() {
		return dictionarySet;
	}

	@Override
	protected void doLoad() throws DictionaryFormatException {
		for (File dir : directories) {
			final List<File> files = SiameseUtil.getFiles(dir);
			for (File file : files) {
				doLoad(file);
			}
		}
	}

	private void doLoad(final File file) throws DictionaryFormatException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			final Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
			load(workbook);
		} catch (FileNotFoundException ex) {
			throw new DictionaryFormatException("Dictionary excel file not found.", ex);
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
		final MyDictionary dictionary = new MyDictionary();

		int s = 0;
		for (Row row : sheet) {
			switch (s) {
			case 0: {
				Cell cell = row.getCell(0);
				if ("ディクショナリ名".equals(cell.getStringCellValue())) {
					cell = row.getCell(2);
					dictionary.name = cell.getStringCellValue();
					s = 10;
				}
				break;
			}
			case 10: {
				Cell cell = row.getCell(0);
				if ("NO".equals(cell.getStringCellValue())) {
					s = 20;
				}
				break;
			}
			case 20: {
				// final String no = trim(ExcelUtil.getValueToString(row.getCell(0)));
				final String name = trim(ExcelUtil.getValueToString(row.getCell(1)));
				final String value = trim(ExcelUtil.getValueToString(row.getCell(2)));
				// final String memo = trim(ExcelUtil.getValueToString(row.getCell(3)));

				if (SiameseUtil.isNotEmptyAll(name, value)) {
					final MyItem item = new MyItem(name, value);
					dictionary.map.put(name, item);
				}
				break;
			}
			}
		}

		addDictionary(dictionary);
	}

	private boolean addDictionary(final Dictionary dictionary) {
		final boolean result = dictionarySet.addDictionary(dictionary);
		if (result) {
			logger.debug("load dictionary.[{}]", dictionary.getName());
		} else {
			logger.warn("Duplicate dictionary name.[{}]", dictionary.getName());
		}
		return result;
	}

	private static class MyDictionarySet implements DictionarySet {

		private final Map<String, Dictionary> map;
		private final List<Dictionary> list;

		public MyDictionarySet() {
			map = new HashMap<String, Dictionary>();
			list = new ArrayList<Dictionary>();
		}

		public boolean addDictionary(final Dictionary dictionary) {
			boolean result = false;
			if (!map.containsKey(dictionary.getName())) {
				map.put(dictionary.getName(), dictionary);
				list.add(dictionary);
				result = true;
			} else {

			}
			return result;
		}

		@Override
		public List<Dictionary> getDictionarys() {
			return new ArrayList<Dictionary>(list);
		}

		@Override
		public Dictionary getDictionary(final String name) throws DictionaryNotFoundException {
			Dictionary dictionary = null;
			if (map.containsKey(name)) {
				dictionary = map.get(name);
			}
			if (null == dictionary) {
				throw new DictionaryNotFoundException(String.format("Not found dictionary.[%s]", name));
			}
			return dictionary;
		}

		@Override
		public Iterator<Dictionary> iterator() {
			return list.iterator();
		}
	}

	private static class MyDictionary implements Dictionary {

		private String name;
		private final Map<String, MyItem> map;

		public MyDictionary() {
			map = new HashMap<String, MyItem>();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Item lookup(String name) {
			return map.get(name);
		}
	}

	private static class MyItem implements Item {

		private final String name;

		private final String value;

		public MyItem(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public static class ExcelDictionaryLoaderBuilder {

		private final List<File> directorys;

		private ExcelDictionaryLoaderBuilder() {
			directorys = new ArrayList<File>();
		}

		public static ExcelDictionaryLoaderBuilder newBuilder() {
			return new ExcelDictionaryLoaderBuilder();
		}

		public ExcelDictionaryLoaderBuilder addTargetDirectory(final File file) {
			directorys.add(file);
			return this;
		}

		public ExcelDictionaryLoader build() {
			return new ExcelDictionaryLoader(directorys);
		}
	}
}
