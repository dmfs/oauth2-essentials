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
