/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
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
 */

package org.dmfs.oauth2.client.scope;

import java.util.Iterator;

import org.dmfs.iterators.CsvIterator;
import org.dmfs.oauth2.client.OAuth2Scope;


/**
 * An {@link OAuth2Scope} that interprets a space separated token list.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class StringScope implements OAuth2Scope
{
	private final String mScope;


	/**
	 * Creates an {@link OAuth2Scope} from the given space separated token list.
	 * 
	 * @param scope
	 */
	public StringScope(String scope)
	{
		mScope = scope;
	}


	@Override
	public boolean hasToken(String token)
	{
		Iterator<String> tokenIterator = new CsvIterator(mScope, ' ');
		while (tokenIterator.hasNext())
		{
			if (tokenIterator.next().equals(token))
			{
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean isEmpty()
	{
		return mScope.length() == 0;
	}


	@Override
	public String toString()
	{
		return mScope;
	}

}
