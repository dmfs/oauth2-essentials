package org.dmfs.oauth2.client.http.responsehandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.mockutils.entities.StaticMockResponseEntity;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.oauth2.client.errors.TokenRequestError;
import org.junit.Test;


/**
 * Test the {@link TokenErrorResponseHandler}.
 * <p />
 * TODO: also test some invalid responses.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class TokenErrorResponseHandlerTest
{

	@Test
	public void testHandleResponse() throws UnsupportedEncodingException, IOException, ProtocolError, ProtocolException
	{
		final String accessTokenResponse = "{" + "\"error\":\"invalid_grant\"," + "\"error_description\":\"DESCRIPTION\","
			+ "\"error_uri\":\"http://example.com\"" + "}";

		try
		{
			new TokenErrorResponseHandler().handleResponse(new StaticMockResponse(HttpStatus.BAD_REQUEST, EmptyHeaders.INSTANCE, new StaticMockResponseEntity(
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
