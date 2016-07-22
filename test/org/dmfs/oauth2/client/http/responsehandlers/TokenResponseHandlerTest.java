package org.dmfs.oauth2.client.http.responsehandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.mockutils.entities.StaticMockResponseEntity;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.scope.EmptyScope;
import org.junit.Test;


/**
 * Test the {@link TokenResponseHandler}.
 * <p />
 * TODO: also test some invalid responses.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class TokenResponseHandlerTest
{

	@Test
	public void testHandleResponse() throws UnsupportedEncodingException, IOException, ProtocolError, ProtocolException
	{
		final String accessTokenResponse = "{" + "\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\"," + "\"token_type\":\"example\"," + "\"expires_in\":3600,"
			+ "\"refresh_token\":\"tGzv3JOkF0XG5Qx2TlKWIA\"," + "\"example_parameter\":\"example_value\"" + "}";

		OAuth2AccessToken token = new TokenResponseHandler(EmptyScope.INSTANCE).handleResponse(new StaticMockResponse(HttpStatus.OK, EmptyHeaders.INSTANCE,
			new StaticMockResponseEntity(new StructuredMediaType("application", "json", "utf-8"), accessTokenResponse)));
		assertEquals("2YotnFZFEjr1zCsicMWpAA", token.accessToken());
		assertEquals("tGzv3JOkF0XG5Qx2TlKWIA", token.refreshToken());
		assertTrue(token.hasRefreshToken());
		assertEquals("example", token.tokenType());
		// note this is quite fragile: it assumes that the test completes in much less time than 1 second and doesn't span over two seconds
		assertEquals(System.currentTimeMillis() / 1000 + 3600, token.expiriationDate().getTimestamp() / 1000);
	}
}
