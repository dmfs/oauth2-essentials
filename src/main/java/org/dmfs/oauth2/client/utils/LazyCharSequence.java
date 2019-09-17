/*
 * Copyright 2019 dmfs GmbH
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

package org.dmfs.oauth2.client.utils;

import org.dmfs.jems.single.Single;
import org.dmfs.jems.single.elementary.Frozen;


/**
 * A {@link CharSequence} which is evaluated lazily.
 *
 * @author Marten Gajda
 */
public final class LazyCharSequence implements CharSequence
{

    private final Single<CharSequence> mDelegate;


    /**
     * Creates a {@link CharSequence} which delegates to the {@link Object#toString()} method of the delegate.
     */
    public LazyCharSequence(Object delegate)
    {
        this(() -> delegate.toString());
    }


    public LazyCharSequence(Single<CharSequence> delegate)
    {
        mDelegate = new Frozen<>(delegate);
    }


    @Override
    public int length()
    {
        return mDelegate.value().length();
    }


    @Override
    public char charAt(int i)
    {
        return mDelegate.value().charAt(i);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        return mDelegate.value().subSequence(start, end);
    }


    @Override
    public String toString()
    {
        return mDelegate.value().toString();
    }
}
