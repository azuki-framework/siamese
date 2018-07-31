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
package org.azkfw.siamese.parameter;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

/**
 *
 * @author Kawakicchi
 */
public class BasicParameter implements Parameter {

	private final Map<String, Object> map;

	public BasicParameter() {
		map = new HashedMap<String, Object>();
	}

	@Override
	public void put(final String name, final Object value) {
		map.put(name, value);
	}

	@Override
	public Object getObject(final String name) {
		return getObject(name, null);
	}

	@Override
	public Object getObject(final String name, final Object defVal) {
		Object v = defVal;
		if (map.containsKey(name)) {
			v = map.get(name);
		}
		return v;
	}

	@Override
	public String getString(final String name) {
		return getString(name, null);
	}

	@Override
	public String getString(final String name, final String defVal) {
		String v = defVal;
		if (map.containsKey(name)) {
			final Object obj = map.get(name);
			if (null == obj) {
			} else if (obj instanceof String) {
				v = (String) obj;
			} else {
				v = obj.toString();
			}
		}
		return v;
	}

	@Override
	public Integer getInteger(final String name) {
		return getInteger(name, null);
	}

	@Override
	public Integer getInteger(final String name, final Integer defVal) {
		Integer v = defVal;
		if (map.containsKey(name)) {
			final Object obj = map.get(name);
			if (null == obj) {
			} else if (obj instanceof Integer) {
				v = (Integer) obj;
			} else {
				try {
					v = Integer.parseInt(obj.toString());
				} catch (NumberFormatException ex) {

				}
			}
		}
		return v;
	}

}
