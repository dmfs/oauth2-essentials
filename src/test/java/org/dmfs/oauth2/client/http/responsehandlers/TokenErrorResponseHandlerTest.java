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

package org.dmfs.oauth2.client.http.responsehandlers;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.mockutils.entities.StaticMockResponseEntity;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.oauth2.client.errors.TokenRequestError;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


/**
 * Test the {@link TokenErrorResponseHandler}.
 * <p/>
 * TODO: also test some invalid responses.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public class TokenErrorResponseHandlerTest
{

    @Test
    public void testHandleResponseNoUri() throws UnsupportedEncodingException, IOException, ProtocolError, ProtocolException
    {
        final String accessTokenResponse = "{" + "\"error\":\"invalid_grant\"," + "\"error_description\":\"DESCRIPTION\"," + "}";

        try
        {
            new TokenErrorResponseHandler().handleResponse(
                    new StaticMockResponse(HttpStatus.BAD_REQUEST, EmptyHeaders.INSTANCE, new StaticMockResponseEntity(
                            new StructuredMediaType("application", "json", "utf-8"), accessTokenResponse)));
            fail("No exception thrown");
        }
        catch (TokenRequestError e)
        {
            assertEquals("invalid_grant", e.getMessage());
            assertEquals("DESCRIPTION", e.description());
            assertNull(e.uri());
        }
        catch (Exception t)
        {
            fail(String.format("wrong exception type thrown: %s", t.getClass().getName()));
        }
    }


    @Test
    public void testHandleResponseNoDescription() throws UnsupportedEncodingException, IOException, ProtocolError, ProtocolException
    {
        final String accessTokenResponse = "{" + "\"error\":\"invalid_grant\"," + "\"error_uri\":\"http://example.com\"" + "}";

        try
        {
            new TokenErrorResponseHandler().handleResponse(
                    new StaticMockResponse(HttpStatus.BAD_REQUEST, EmptyHeaders.INSTANCE, new StaticMockResponseEntity(
                            new StructuredMediaType("application", "json", "utf-8"), accessTokenResponse)));
            fail("No exception thrown");
        }
        catch (TokenRequestError e)
        {
            assertEquals("invalid_grant", e.getMessage());
            assertEquals("", e.description());
            assertEquals(URI.create("http://example.com"), e.uri());
        }
        catch (Exception t)
        {
            fail(String.format("wrong exception type thrown: %s", t.getClass().getName()));
        }
    }


    @Test
    public void testHandleResponse() throws UnsupportedEncodingException, IOException, ProtocolError, ProtocolException
    {
        final String accessTokenResponse = "{" + "\"error\":\"invalid_grant\"," + "\"error_description\":\"DESCRIPTION\","
                + "\"error_uri\":\"http://example.com\"" + "}";

        try
        {
            new TokenErrorResponseHandler().handleResponse(
                    new StaticMockResponse(HttpStatus.BAD_REQUEST, EmptyHeaders.INSTANCE, new StaticMockResponseEntity(
                            new StructuredMediaType("application", "json", "utf-8"), accessTokenResponse)));
            fail("No exception thrown");
        }
        catch (TokenRequestError e)
        {
            assertEquals("invalid_grant", e.getMessage());
            assertEquals("DESCRIPTION", e.description());
            assertEquals(URI.create("http://example.com"), e.uri());
        }
        catch (Exception t)
        {
            fail(String.format("wrong exception type thrown: %s", t.getClass().getName()));
        }
    }
}
