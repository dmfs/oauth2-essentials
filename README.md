[![Build Status](https://travis-ci.org/dmfs/oauth2-essentials.svg?branch=master)](https://travis-ci.org/dmfs/oauth2-essentials)

# oauth2-essentials

An OAuth2 client implementation based on http-client-essentials-suite.

## Supported Grants

* [Authorization Code Grant (requires a webwiev)](https://tools.ietf.org/html/rfc6749#section-4.1)
* [Implicit Grant (requires a webview)](https://tools.ietf.org/html/rfc6749#section-4.2)
* [Resource Owner Password Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.3)
* [Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4)
* [Refresh Token Grant](https://tools.ietf.org/html/rfc6749#section-6)

### Examples

### Initialize the client

Before doing any request you need to initialize the client to provide endpoints and client credentials.
In general it will look like this:

    // Create HttpRequestExecutor to execute HTTP requests
    // Any other HttpRequestExecutor implementaion will do
    HttpRequestExecutor executor = new HttpUrlConnectionExecutor();

    // Create OAuth2 provider
    OAuth2AuthorizationProvider provider = new BasicOAuth2AuthorizationProvider(
        URI.create("http://example.com/auth"),
        URI.create("http://example.com/token"),
        new Duration(1,0,3600) /* default expiration time in case the server doesn't return any */);

    // Create OAuth2 client credentials
    OAuth2ClientCredentials credentials = new BasicOAuth2ClientCredentials(
        "client-id", "client-password");

    // Create OAuth2 client
    OAuth2Client client = new BasicOAuth2Client(
        provider,
        credentials,
        URI.create("http://localhost") /* Redirect URL */);

### Authorization Code Grant (requires a webwiev)

    // Start an interactive Authorization Code Grant
    OAuth2InteractiveGrant grant = new AuthorizationCodeGrant(
        client, new BasicScope("scope"));

    // Get the authorization URL and open it in a WebView

    URI authorizationUrl = grant.authorizationUrl();

    // Open the URL in a WebView and wait for the redirect to the redirect URL
    // After the redirect, feed the URL to the grant to retrieve the access token

    OAuth2AccessToken token = grant.withRedirect(redirectUrl).accessToken(executor);

### Implicit Grant (requires a webview)

    // Start an interactive Implicit Grant
    OAuth2InteractiveGrant grant = new ImplicitGrant(client, new BasicScope("scope"));

    // Get the authorization URL and open it in a WebView

    URI authorizationUrl = grant.authorizationUrl();

    // Open the URL in a WebView and wait for the redirect to the redirect URL
    // After the redirect, feed it to the grant to retrieve the access token

    OAuth2AccessToken token = grant.withRedirect(redirectUrl).accessToken(executor);

### Resource Owner Password Credentials Grant

    // Request access token using a Resource Owner Password Grant
    OAuth2AccessToken token = new ResourceOwnerPasswordGrant(
        client, new BasicScope("scope"), "UserName", "UserSecret").accessToken(executor);

### Client Credentials Grant

    // Request access token using a Client Credentials Grant
    OAuth2AccessToken token = new ClientCredentialsGrant(
        client, new BasicScope("scope")).accessToken(executor);


### Refresh Token Grant

    // Request new access token, providing the previous one
    OAuth2AccessToken token = new TokenRefreshGrant(
        client, oldToken).accessToken(executor);



## Requirements

This library builds upon [http-client-essentials-suite](https://github.com/dmfs/http-client-essentials-suite).

## License

Copyright (c) Marten Gajda 2016, licensed under Apache2.

