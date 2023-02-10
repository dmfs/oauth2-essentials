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
import org.junit.Test;

import java.net.URI;

import static org.dmfs.jems2.mockito.Mock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.saynotobugs.confidence.Assertion.assertThat;
import static org.saynotobugs.confidence.quality.Core.*;


public class AuthorizationCodeGrantTest
{
    @Test
    public void testInitialState()
    {
        OAuth2Client mockClient = mock(OAuth2Client.class,
            with(OAuth2Client::randomChars, returning("123456789012345678901234567890")),
            with(client -> client.authorizationUrl(any()),
                answering(invocation -> ((OAuth2AuthorizationRequest) invocation.getArgument(0)).authorizationUri(URI.create("http://1234")))));

        assertThat(new AuthorizationCodeGrant(mockClient, new BasicScope("token1", "token2")),
            has("state", OAuth2InteractiveGrant::encodedState,
                has("grant", new InteractiveGrantFactory(mockClient),
                    allOf(
                        instanceOf(AuthorizationCodeGrant.class),
                        has("authorization URI", OAuth2InteractiveGrant::authorizationUrl,
                            equalTo(
                                URI.create(
                                    "http://1234?" +
                                        "response_type=code&" +
                                        "scope=token1+token2&" +
                                        "state=123456789012345678901234567890&" +
                                        "code_challenge_method=S256&" +
                                        "code_challenge=9U5cj4EGSOdjjSXrftbSS35ZmdWI6Igm8qqDfS7lLs0"))))))
        );
    }


    @Test
    public void testRedirectedState()
    {
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class,
            with(Object::toString, returning("accessToken")));

        HttpRequestExecutor executor = mock(HttpRequestExecutor.class);

        OAuth2Client mockClient = mock(OAuth2Client.class,
            with(OAuth2Client::randomChars, returning("123456789012345678901234567890")),
            with(OAuth2Client::redirectUri, returning(new LazyUri(new Precoded("http://xyz")))),
            with(client -> client.authorizationUrl(any()),
                answering(invocation -> ((OAuth2AuthorizationRequest) invocation.getArgument(0)).authorizationUri(URI.create("http://1234")))),
            with(client -> client.accessToken(any(), eq(executor)), returning(accessToken)));

        assertThat(new AuthorizationCodeGrant(mockClient, new BasicScope("token1", "token2")),
            has("redirected", grant -> grant.withRedirect(
                    new LazyUri(new Precoded("http://redirected?code=98765&state=123456789012345678901234567890"))),
                has("state", OAuth2InteractiveGrant::encodedState,
                    has("grant", new InteractiveGrantFactory(mockClient),
                        has("authorization URI", grant -> grant.accessToken(executor),
                            equalTo(accessToken)))))
        );
    }

}