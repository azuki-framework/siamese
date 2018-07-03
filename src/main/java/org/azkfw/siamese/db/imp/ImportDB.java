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
package org.azkfw.siamese.db.imp;

import java.sql.Connection;

/**
 * このクラスは、データベースへデータをインポートするクラスです。
 * 
 * @author Kawakicchi
 */
public class ImportDB {

	/** コネクション */
	private final Connection con;

	/**
	 * コンストラクタ
	 * <p>
	 * インスタンス化禁止
	 * </p>
	 *
	 * @param con コネクション情報
	 */
	private ImportDB(final Connection con) {
		this.con = con;
	}

	public void imp() {

	}

	public static class Builder {

		private final Connection con;

		private Builder(final Connection con) {
			this.con = con;
		}

		public static Builder newInstance(final Connection con) {
			final Builder builder = new Builder(con);
			return builder;
		}

		public ImportDB build() {
			final ImportDB imp = new ImportDB(this.con);
			return imp;
		}
	}
}
