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

package org.dmfs.oauth2.client.http.requests;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.TextParameter;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.dmfs.jems.hamcrest.matchers.LambdaMatcher.having;
import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link ClientCredentialsTokenRequest}.
 *
 * @author Marten Gajda
 */
public class ClientCredentialsTokenRequestTest
{
    @Test
    public void test() throws IOException
    {
        // get the entity to test
        HttpRequestEntity entity = new ClientCredentialsTokenRequest(new StringScope("s1 s2")).requestEntity();

        // get the entity data and parse it as x-www-form-urlencoded
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeContent(out);
        ParameterList params = new XwfueParameterList(new Precoded(new String(out.toByteArray())));

        // test content type
        assertThat(entity.contentType(), is(present(
                allOf(
                        is(new StringMediaType("application/x-www-form-urlencoded")),
                        having(t -> t.charset("any"), is("UTF-8"))))));

        // test data
        assertEquals("client_credentials", new TextParameter(new BasicParameterType<CharSequence>("grant_type", TextValueType.INSTANCE), params).toString());
        assertEquals("s1 s2", new TextParameter(new BasicParameterType<CharSequence>("scope", TextValueType.INSTANCE), params).toString());
    }
}