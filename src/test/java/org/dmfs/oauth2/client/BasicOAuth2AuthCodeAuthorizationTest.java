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

package org.dmfs.oauth2.client;

import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.oauth2.client.scope.BasicScope;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Marten Gajda <marten@dmfs.org>
 */
public class BasicOAuth2AuthCodeAuthorizationTest
{
    @Test(expected = ProtocolException.class)
    public void invalidState() throws Exception
    {
        new BasicOAuth2AuthCodeAuthorization(
                new LazyUri(new Precoded("https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz")),
                new BasicScope("scope"),
                "abc");
    }


    @Test
    public void code() throws Exception
    {
        assertEquals("SplxlOBeZQQYbYS6WxSbIA",
                new BasicOAuth2AuthCodeAuthorization(
                        new LazyUri(new Precoded("https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz")),
                        new BasicScope("scope"),
                        "xyz").code().toString());
    }


    @Test
    public void scope() throws Exception
    {
        assertEquals(new BasicScope("scope"),
                new BasicOAuth2AuthCodeAuthorization(
                        new LazyUri(new Precoded("https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz")),
                        new BasicScope("scope"),
                        "xyz").scope());
    }

}