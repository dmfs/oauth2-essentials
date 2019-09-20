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

package org.dmfs.oauth2.client.http.requests.parameters;

import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;


/**
 * @author Marten Gajda
 * <p>
 * TODO: move to jems
 */
public abstract class DelegatingPair<Left, Right> implements Pair<Left, Right>
{
    private final Pair<? extends Left, ? extends Right> mDelegate;


    public DelegatingPair(Left left, Right right)
    {
        this(new ValuePair<>(left, right));
    }


    public DelegatingPair(Pair<? extends Left, ? extends Right> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final Left left()
    {
        return mDelegate.left();
    }


    @Override
    public final Right right()
    {
        return mDelegate.right();
    }
}
