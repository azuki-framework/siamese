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
package org.azkfw.siamese.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kawakicchi
 */
public class SiameseUtil {

	public static String trim(final String string) {
		String s = null;
		if (null != string) {
			s = string.trim();
		}
		return s;
	}

	public static boolean isEquals(final String string1, final String string2) {
		if (null == string1 && null == string2) {
			return true;
		} else if (null != string1 && null != string2) {
			return string1.equals(string2);
		} else {
			return false;
		}
	}

	public static boolean isEmpty(final String string) {
		return (null == string || 0 == string.length());
	}

	public static boolean isNotEmpty(final String string) {
		return (!isEmpty(string));
	}

	public static boolean isNotEmptyAll(final String... strings) {
		for (String s : strings) {
			if (isEmpty(s)) {
				return false;
			}
		}
		return true;
	}

	public static List<File> getFiles(final File file) {
		final List<File> files = new ArrayList<File>();
		if (file.isDirectory()) {
			dir(file, files);
		} else if (file.isFile()) {
			file(file, files);
		}
		return files;
	}

	private static void file(final File file, final List<File> files) {
		final String name = file.getName().toLowerCase();
		if (!name.endsWith(".xlsx")) {
			return;
		}
		if (name.startsWith("~$")) {
			return;
		}
		files.add(file);
	}

	private static void dir(final File dir, final List<File> files) {
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
}
