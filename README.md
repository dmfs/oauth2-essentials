[![Build Status](https://travis-ci.org/dmfs/oauth2-essentials.svg?branch=master)](https://travis-ci.org/dmfs/oauth2-essentials)

# oauth2-essentials

An OAuth2 client implementation based on http-client-essentials-suite.

## Rationale

[OAuth2](https://tools.ietf.org/html/rfc6749) is an authentication frameworks that's standardized in RFC 6749. There are a couple of Java implementations for OAuth2 clients available,
but most of them either depend on a specific platform (e.g. Android), pull in large dependencies (usually a specific HTTP client implementation) or are incomplete.
This library aims to provide a complete, platform independent Java implementation of RFC 6749 which can be used with any HTTP client implementation. The later is achieved by using the
http-client-essentials abstraction framework for HTTP clients.

## Supported Grants

* [Authorization Code Grant (requires a webview)](https://tools.ietf.org/html/rfc6749#section-4.1)
* [Implicit Grant (requires a webview)](https://tools.ietf.org/html/rfc6749#section-4.2)
* [Resource Owner Password Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.3)
* [Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4)
* [Refresh Token Grant](https://tools.ietf.org/html/rfc6749#section-6)

## Examples

### Initialize the client

Before doing any request you need to initialize the client to provide endpoints and client credentials.
In general it will look like this:

```java
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
    new LazyUri(new Precoded("http://localhost")) /* Redirect URL */);
```

### Authorization Code Grant

```java
// Start an interactive Authorization Code Grant
OAuth2InteractiveGrant grant = new AuthorizationCodeGrant(
    client, new BasicScope("scope"));

// Get the authorization URL and open it in a WebView
URI authorizationUrl = grant.authorizationUrl();

// Open the URL in a WebView and wait for the redirect to the redirect URL
// After the redirect, feed the URL to the grant to retrieve the access token
OAuth2AccessToken token = grant.withRedirect(redirectUrl).accessToken(executor);
```

### Implicit Grant

```java
// Start an interactive Implicit Grant
OAuth2InteractiveGrant grant = new ImplicitGrant(client, new BasicScope("scope"));

// Get the authorization URL and open it in a WebView
URI authorizationUrl = grant.authorizationUrl();

// Open the URL in a WebView and wait for the redirect to the redirect URL
// After the redirect, feed it to the grant to retrieve the access token
OAuth2AccessToken token = grant.withRedirect(redirectUrl).accessToken(executor);
```

### Resource Owner Password Credentials Grant

```java
// Request access token using a Resource Owner Password Grant
OAuth2AccessToken token = new ResourceOwnerPasswordGrant(
    client, new BasicScope("scope"), "UserName", "UserSecret").accessToken(executor);
```

### Client Credentials Grant

```java
// Request access token using a Client Credentials Grant
OAuth2AccessToken token = new ClientCredentialsGrant(client, new BasicScope("scope")).accessToken(executor);
```

### Refresh Token Grant

```java
// Request new access token, providing the previous one
OAuth2AccessToken token = new TokenRefreshGrant(client, oldToken).accessToken(executor);
```

### Authenticate a request

After receiving the access token you usually want to use it to authenticate connections. In general this depends on the http framework in use.

#### Using http-client-essentials

To authenticate a request using http-client-essentials just use a `BearerAuthenticatedRequest` like so

```java
// 'request' is a HttpRequest instance that's to be authenticated
result = executor.execute(url, new BearerAuthenticatedRequest(request, token));
```

#### Using another http client or a non-http protocol

When not using http-client-essentials you can generate and add the `Authorization` header yourself.

```java
// build the value of the Authorization header
String authorization = String.format("Bearer %s", token.accessToken());

// add the header to your request
// For HttpUrlConnection this looks like:
myConnection.setRequestProperty("Authorization", authorization);
```

## Choice of HTTP client

This library doesn't depend on any specific HTTP client implementation. Instead it builds upon [http-client-essentials-suite](https://github.com/dmfs/http-client-essentials-suite) to allow any 3rd party HTTP client to be used.

## Download

Get the latest version via [Maven](https://search.maven.org/remote_content?g=org.dmfs&a=oauth2-essentials&v=LATEST)

Or add it to your build.gradle:


    dependencies {
        // oauth2-essentials
        implementation 'org.dmfs:oauth2-essentials:0.18'
        // optional to use httpurlconnection-executor, any other HttpRequestExecutor
        // implementation will do
        implementation 'org.dmfs:httpurlconnection-executor:0.20'
    }


## License

Copyright dmfs GmbH 2019, licensed under Apache2.

