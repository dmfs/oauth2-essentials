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

package org.dmfs.oauth2.client.http.requests;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher;
import org.dmfs.oauth2.client.OAuth2AuthCodeAuthorization;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.scope.BasicScope;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.TextParameter;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.dmfs.rfc3986.uris.LazyUri;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * @author Marten Gajda
 */
public class AuthorizationCodeTokenRequestTest
{
    @Test
    public void testRequestEntity() throws Exception
    {
        OAuth2AuthCodeAuthorization authorization = new OAuth2AuthCodeAuthorization()
        {
            @Override
            public CharSequence code()
            {
                return "authcode";
            }


            @Override
            public OAuth2Scope scope()
            {
                return new BasicScope("scope1", "scope2");
            }
        };

        // get the entity to test
        HttpRequestEntity entity = new AuthorizationCodeTokenRequest(authorization, new LazyUri(new Precoded("http://example.com")), "123").requestEntity();

        // get the entity data and parse it as x-www-form-urlencoded
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeContent(out);
        ParameterList params = new XwfueParameterList(new Precoded(new String(out.toByteArray())));

        // test content type
        assertThat(entity.contentType(), is(PresentMatcher.<MediaType>present(new StringMediaType("application/x-www-form-urlencoded"))));

        // test data
        assertEquals("123", new TextParameter(new BasicParameterType<CharSequence>("code_verifier", TextValueType.INSTANCE), params).toString());
        assertEquals("authcode", new TextParameter(new BasicParameterType<CharSequence>("code", TextValueType.INSTANCE), params).toString());
        assertEquals("http://example.com", new TextParameter(new BasicParameterType<CharSequence>("redirect_uri", TextValueType.INSTANCE), params).toString());
        assertEquals("authorization_code", new TextParameter(new BasicParameterType<CharSequence>("grant_type", TextValueType.INSTANCE), params).toString());
    }


    @Test
    public void testMethod() throws Exception
    {
        OAuth2AuthCodeAuthorization authorization = new OAuth2AuthCodeAuthorization()
        {
            @Override
            public CharSequence code()
            {
                return "123";
            }


            @Override
            public OAuth2Scope scope()
            {
                return new BasicScope("scope1", "scope2");
            }
        };

        assertEquals(HttpMethod.POST, new AuthorizationCodeTokenRequest(authorization, new LazyUri(new Precoded("http://example.com")), "123").method());
    }


    @Ignore
    @Test
    public void testHeaders() throws Exception
    {
        // we don't need any specific headers, so there is nothing to test
    }


    @Ignore
    @Test
    public void testResponseHandler() throws Exception
    {
        fail("not implemented yet");
    }

}