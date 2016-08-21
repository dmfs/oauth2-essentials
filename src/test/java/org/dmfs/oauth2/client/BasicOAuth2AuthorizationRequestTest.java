package org.dmfs.oauth2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;

import org.dmfs.oauth2.client.scope.BasicScope;
import org.dmfs.oauth2.client.scope.EmptyScope;
import org.junit.Test;


public class BasicOAuth2AuthorizationRequestTest
{

	@Test
	public void testBasicOAuth2AuthorizationRequestStringString()
	{
		assertEquals(URI.create("http://example.com/auth/?response_type=code&state=1234"),
			new BasicOAuth2AuthorizationRequest("code", "1234").authorizationUri(URI.create("http://example.com/auth/")));
		assertEquals(URI.create("http://example.com/auth?response_type=code&state=1234"),
			new BasicOAuth2AuthorizationRequest("code", "1234").authorizationUri(URI.create("http://example.com/auth")));
		assertEquals(URI.create("http://example.com/auth?response_type=code&state=1234%2F"),
			new BasicOAuth2AuthorizationRequest("code", "1234/").authorizationUri(URI.create("http://example.com/auth")));
	}


	@Test
	public void testBasicOAuth2AuthorizationRequestStringOAuth2ScopeString()
	{
		// note this test makes assumptions about the order of query parameters which is wrong.
		assertEquals(URI.create("http://example.com/auth?response_type=code&scope=&state=1234"), new BasicOAuth2AuthorizationRequest("code",
			EmptyScope.INSTANCE, "1234").authorizationUri(URI.create("http://example.com/auth")));
		assertEquals(URI.create("http://example.com/auth?response_type=code&scope=calendar&state=1234"), new BasicOAuth2AuthorizationRequest("code",
			new BasicScope("calendar"), "1234").authorizationUri(URI.create("http://example.com/auth")));
		assertEquals(URI.create("http://example.com/auth?response_type=code&scope=calendar+test+http%3A%2F%2Fexample.com%2Fcontacts&state=1234"),
			new BasicOAuth2AuthorizationRequest("code", new BasicScope("calendar", "test", "http://example.com/contacts"), "1234").authorizationUri(URI
				.create("http://example.com/auth")));
	}


	@Test
	public void testWithClientId()
	{
		// note this test makes assumptions about the order of query parameters which is wrong.
		assertEquals(URI.create("http://example.com/auth?response_type=code&scope=&state=1234&client_id=abcd"), new BasicOAuth2AuthorizationRequest("code",
			EmptyScope.INSTANCE, "1234").withClientId("abcd").authorizationUri(URI.create("http://example.com/auth")));
		assertEquals(URI.create("http://example.com/auth?response_type=code&scope=calendar&state=1234&client_id=xyz"), new BasicOAuth2AuthorizationRequest(
			"code", new BasicScope("calendar"), "1234").withClientId("abcd").withClientId("xyz").authorizationUri(URI.create("http://example.com/auth")));
		assertEquals(
			URI.create("http://example.com/auth?response_type=code&scope=calendar+test+http%3A%2F%2Fexample.com%2Fcontacts&state=1234&client_id=ab%3Ad"),
			new BasicOAuth2AuthorizationRequest("code", new BasicScope("calendar", "test", "http://example.com/contacts"), "1234").withClientId("ab:d")
				.authorizationUri(URI.create("http://example.com/auth")));
	}


	@Test
	public void testWithRedirectUri()
	{

		fail("not implemented yet");
	}
}
