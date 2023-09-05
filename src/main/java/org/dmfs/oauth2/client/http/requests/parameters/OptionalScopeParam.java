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
import org.dmfs.jems2.optional.Conditional;
import org.dmfs.jems2.optional.DelegatingOptional;
import org.dmfs.jems2.optional.Mapped;
import org.dmfs.oauth2.client.OAuth2Scope;


/**
 * An Optional OAuth2 {@code scope} parameter.
 *
 * @author Marten Gajda
 */
public final class OptionalScopeParam extends DelegatingOptional<Pair<CharSequence, CharSequence>>
{
    public OptionalScopeParam(OAuth2Scope scope)
    {
        super(new Mapped<>(
            s -> new ValuePair<>("scope", s.toString()),
            new Conditional<>(s -> !s.isEmpty(), scope)));
    }
}
