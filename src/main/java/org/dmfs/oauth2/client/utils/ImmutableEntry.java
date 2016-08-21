/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.dmfs.oauth2.client.utils;

import java.util.Map;


/**
 * Simple immutable implementation of {@link Map.Entry} of Strings.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class ImmutableEntry implements Map.Entry<String, String>
{

	private final String mKey;
	private final String mValue;


	/**
	 * Creates an immutable {@link Map.Entry} with the given key and value.
	 * 
	 * @param key
	 *            The key of the entry.
	 * @param value
	 *            The value of the entry.
	 */
	public ImmutableEntry(String key, String value)
	{
		mKey = key;
		mValue = value;
	}


	@Override
	public String getKey()
	{
		return mKey;
	}


	@Override
	public String getValue()
	{
		return mValue;
	}


	@Override
	public String setValue(String value)
	{
		throw new UnsupportedOperationException("Can not set value of an ImmutableEntry!");
	}

}
