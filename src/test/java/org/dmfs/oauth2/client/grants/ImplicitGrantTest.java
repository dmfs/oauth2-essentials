package org.dmfs.oauth2.client.grants;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2AuthorizationRequest;
import org.dmfs.oauth2.client.OAuth2Client;
import org.dmfs.oauth2.client.OAuth2InteractiveGrant;
import org.dmfs.oauth2.client.scope.BasicScope;
import org.dmfs.oauth2.client.state.InteractiveGrantFactory;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc5545.Duration;
import org.junit.Test;

import java.net.URI;

import static org.dmfs.jems2.mockito.Mock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.saynotobugs.confidence.Assertion.assertThat;
import static org.saynotobugs.confidence.quality.Core.*;


public class ImplicitGrantTest
{
    @Test
    public void testInitialState()
    {
        OAuth2Client mockClient = mock(OAuth2Client.class,
            with(OAuth2Client::randomChars, returning("123456789012345678901234567890")),
            with(client -> client.authorizationUrl(any()),
                answering(invocation -> ((OAuth2AuthorizationRequest) invocation.getArgument(0)).authorizationUri(URI.create("http://1234")))));

        assertThat(new ImplicitGrant(mockClient, new BasicScope("token1", "token2")),
            has("state", OAuth2InteractiveGrant::encodedState,
                has("grant", new InteractiveGrantFactory(mockClient),
                    allOf(
                        instanceOf(ImplicitGrant.class),
                        has("authorization URI", OAuth2InteractiveGrant::authorizationUrl,
                            equalTo(
                                URI.create(
                                    "http://1234?response_type=token&scope=token1+token2&state=123456789012345678901234567890"))))))
        );
    }


    @Test
    public void testRedirectedState()
    {
        HttpRequestExecutor executor = mock(HttpRequestExecutor.class);

        OAuth2Client mockClient = mock(OAuth2Client.class,
            with(OAuth2Client::randomChars, returning("123456789012345678901234567890")),
            with(OAuth2Client::redirectUri, returning(new LazyUri(new Precoded("http://xyz")))),
            with(OAuth2Client::defaultTokenTtl, returning(new Duration(1, 0, 1000))),
            with(client -> client.authorizationUrl(any()),
                answering(invocation -> ((OAuth2AuthorizationRequest) invocation.getArgument(0)).authorizationUri(URI.create("http://1234")))));
        assertThat(new ImplicitGrant(mockClient, new BasicScope("token1", "token2")),
            has("redirected", grant -> grant.withRedirect(
                    new LazyUri(new Precoded("http://redirected#access_token=1234567&state=123456789012345678901234567890"))),
                has("state", OAuth2InteractiveGrant::encodedState,
                    has("grant", new InteractiveGrantFactory(mockClient),
                        has("authorization URI", grant -> grant.accessToken(executor),
                            has("token", OAuth2AccessToken::accessToken, equalTo("1234567"))))))
        );
    }

}