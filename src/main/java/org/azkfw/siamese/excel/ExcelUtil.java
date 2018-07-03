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
package org.azkfw.siamese.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;

/**
 *
 * @author Kawakicchi
 *
 */
public class ExcelUtil {

	public static String getValueToString(final Cell cell) {
		String str = "";
		if (null != cell) {
			final CellType type = cell.getCellTypeEnum();

			if (CellType.STRING == type) {
				str = cell.getStringCellValue();

			} else if (CellType.NUMERIC == type) {
				if (DateUtil.isCellDateFormatted(cell)) {

				} else {

				}

			} else if (CellType.BOOLEAN == type) {

			} else if (CellType.FORMULA == type) {

				final CellType fType = cell.getCachedFormulaResultTypeEnum();
				if (CellType.STRING == fType) {

				} else if (CellType.NUMERIC == fType) {

				} else if (CellType.BOOLEAN == fType) {

				} else if (CellType.ERROR == fType) {
					final byte code = cell.getErrorCellValue();
					final FormulaError error = FormulaError.forInt(code);

				} else {
				}

			} else if (CellType.BLANK == type) {

			} else if (CellType.ERROR == type) {
				// System.out.println("Error");
				final byte code = cell.getErrorCellValue();
				final FormulaError error = FormulaError.forInt(code);
				str = error.getString();

			} else {

			}
		}
		return str;
	}
}
