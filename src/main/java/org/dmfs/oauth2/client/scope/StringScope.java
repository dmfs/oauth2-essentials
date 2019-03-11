/*
 * Copyright 2016 dmfs GmbH
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

import org.dmfs.iterables.Split;
import org.dmfs.iterators.UnquotedSplit;
import org.dmfs.oauth2.client.OAuth2Scope;

import java.util.Iterator;


/**
 * An {@link OAuth2Scope} that interprets a space separated token list.
 *
 * @author Marten Gajda
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
        Iterator<CharSequence> tokenIterator = new UnquotedSplit(mScope, ' ');
        while (tokenIterator.hasNext())
        {
            if (tokenIterator.next().toString().equals(token))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public int tokenCount()
    {
        if (mScope.isEmpty())
        {
            return 0;
        }
        int count = 0;
        for (CharSequence token : new Split(mScope, ' '))
        {
            count += 1;
        }
        return count;
    }


    @Override
    public boolean isEmpty()
    {
        return mScope.isEmpty();
    }


    @Override
    public String toString()
    {
        return mScope;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof OAuth2Scope))
        {
            return false;
        }

        OAuth2Scope other = (OAuth2Scope) obj;
        if (isEmpty() && other.isEmpty())
        {
            return true;
        }

        if (tokenCount() != other.tokenCount())
        {
            return false;
        }

        Iterator<CharSequence> tokens = new UnquotedSplit(mScope, ' ');
        while (tokens.hasNext())
        {
            if (!other.hasToken(tokens.next().toString()))
            {
                return false;
            }
        }
        return true;
    }
}
