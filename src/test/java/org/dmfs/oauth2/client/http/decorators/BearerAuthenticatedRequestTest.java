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

package org.dmfs.oauth2.client.http.decorators;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Unit test for {@link BearerAuthenticatedRequest}.
 *
 * @author Gabor Keszthelyi
 */
@Ignore
@RunWith(JMockit.class)
public class BearerAuthenticatedRequestTest
{

    // TODO: use a generic authorization header instead (once we have one)
    private final static BasicSingletonHeaderType<String> AUTHORIZATION_HEADER = new BasicSingletonHeaderType<String>(
            "Authorization",
            new PlainStringHeaderConverter());

    private static final Headers ORIGINAL_HEADER = EmptyHeaders.INSTANCE.withHeader(
            HttpHeaders.CONTENT_LENGTH.entity(15));
    private static final String ACCESS_TOKEN_STRING = "access token";

    @Injectable
    private HttpRequest<Result> originalRequest;
    @Injectable
    private HttpRequestEntity originalEntity;
    @Injectable
    private HttpMethod originalHttpMethod;
    @Injectable
    private HttpResponseHandler<Result> originalResponseHandler;
    @Injectable
    private HttpResponse response;
    @Injectable
    private OAuth2AccessToken accessToken;


    @Test
    public void testThatNonHeaderPropertiesAreNotAffected() throws Exception
    {
        // ARRANGE
        new Expectations()
        {{
            // @formatter:off
            originalRequest.method(); result = originalHttpMethod;
            originalRequest.requestEntity(); result = originalEntity;
            originalRequest.responseHandler(response); result = originalResponseHandler;
            // @formatter:on
        }};

        // ACT
        BearerAuthenticatedRequest<Result> wrappedRequest = new BearerAuthenticatedRequest<>(originalRequest,
                accessToken);

        // ASSERT
        assertSame(originalHttpMethod, wrappedRequest.method());
        assertSame(originalEntity, wrappedRequest.requestEntity());
        assertSame(originalResponseHandler, wrappedRequest.responseHandler(response));
    }


    @Test
    public void testAuthHeaderIsAdded() throws Exception
    {
        // ARRANGE
        new Expectations()
        {{
            // @formatter:off
            originalRequest.headers(); result = ORIGINAL_HEADER;
            accessToken.accessToken(); result = ACCESS_TOKEN_STRING;
            // @formatter:on
        }};

        // ACT
        BearerAuthenticatedRequest<Result> wrappedRequest = new BearerAuthenticatedRequest<>(originalRequest,
                accessToken);

        // ASSERT
        Headers headers = wrappedRequest.headers();
        assertEquals("Bearer " + ACCESS_TOKEN_STRING, headers.header(AUTHORIZATION_HEADER).value());
        assertTrue(headers.contains(HttpHeaders.CONTENT_LENGTH)); // to make sure the original headers is kept as well
    }


    @Test
    public void testExceptionWhenAccessTokenIsNotAvailable() throws Exception
    {
        final ProtocolException protocolException = new ProtocolException("error message");
        new Expectations()
        {{
            // @formatter:off
            originalRequest.headers(); result = ORIGINAL_HEADER;
            accessToken.accessToken(); result = protocolException;
            // @formatter:on
        }};

        BearerAuthenticatedRequest<Result> wrappedRequest = new BearerAuthenticatedRequest<>(originalRequest,
                accessToken);
        try
        {
            wrappedRequest.headers();
            fail();
        }
        catch (RuntimeException e)
        {
            assertEquals("Can't authenticate request", e.getMessage());
            assertSame(protocolException, e.getCause());
        }
    }


    private class Result
    {
    }

}