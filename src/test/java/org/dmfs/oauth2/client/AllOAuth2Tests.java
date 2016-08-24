package org.dmfs.oauth2.client;

import org.dmfs.oauth2.client.http.responsehandlers.TokenErrorResponseHandlerTest;
import org.dmfs.oauth2.client.http.responsehandlers.TokenResponseHandlerTest;
import org.dmfs.oauth2.client.scope.BasicScopeTest;
import org.dmfs.oauth2.client.scope.EmptyScopeTest;
import org.dmfs.oauth2.client.scope.StringScopeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
        BasicScopeTest.class, EmptyScopeTest.class, StringScopeTest.class, BasicOAuth2AuthorizationRequestTest.class,
        TokenResponseHandlerTest.class,
        TokenErrorResponseHandlerTest.class })
public class AllOAuth2Tests
{

}
