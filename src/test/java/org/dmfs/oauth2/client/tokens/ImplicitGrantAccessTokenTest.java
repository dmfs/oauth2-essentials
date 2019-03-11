/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.oauth2.client.tokens;

import org.dmfs.jems.hamcrest.matchers.optional.AbsentMatcher;
import org.dmfs.oauth2.client.scope.EmptyScope;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc5545.Duration;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ImplicitGrantAccessTokenTest
{

    @Test
    public void testExtraParameter() throws Exception
    {
        assertThat(new ImplicitGrantAccessToken(
                        new LazyUri(new Precoded("http://localhost#state=1&key=value")),
                        new EmptyScope(),
                        "1",
                        new Duration(1, 1, 0)).extraParameter("key"),
                is(present(Matchers.<CharSequence>hasToString("value"))));
    }


    @Test
    public void testExtraParameterDoesNotExist() throws Exception
    {
        assertThat(new ImplicitGrantAccessToken(
                        new LazyUri(new Precoded("http://localhost#state=1&key=value")),
                        new EmptyScope(),
                        "1",
                        new Duration(1, 1, 0)).extraParameter("idToken"),
                is(AbsentMatcher.<CharSequence>absent()));
    }

}
