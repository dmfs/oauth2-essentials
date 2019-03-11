/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.oauth2.client.tokens;

import org.dmfs.jems.hamcrest.matchers.optional.AbsentMatcher;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.scope.StringScope;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Test;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.dmfs.jems.mockito.doubles.TestDoubles.dummy;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Marten Gajda
 */
public class JsonAccessTokenTest
{
    @Test
    public void testNoScope() throws Exception
    {
        OAuth2Scope dummyScope = dummy(OAuth2Scope.class);
        JSONObject jsonObject = new JSONObject();
        assertThat(new JsonAccessToken(jsonObject, dummyScope).scope(), sameInstance(dummyScope));
    }


    @Test
    public void testScope() throws Exception
    {
        OAuth2Scope dummyScope = dummy(OAuth2Scope.class);
        // note: we don't use a mock here, because there are multiple ways to get a String value and we don't want to make assumptions on that
        JSONObject jsonObject = new JSONObject("{\"scope\": \"scope1 scope2\"}");

        assertThat(new JsonAccessToken(jsonObject, dummyScope).scope(), Matchers.<OAuth2Scope>is(new StringScope("scope1 scope2")));
    }


    @Test
    public void testCustomPayload() throws Exception
    {
        assertThat(new JsonAccessToken(new JSONObject("{\"idToken\":\"id_token_value\"}"), dummy(OAuth2Scope.class))
                .extraParameter("idToken"), is(present(Matchers.<CharSequence>hasToString("id_token_value"))));
    }


    @Test
    public void testCustomPayloadWithNonExistingParameter() throws Exception
    {
        assertThat(new JsonAccessToken(new JSONObject("{}"), dummy(OAuth2Scope.class)).extraParameter("idToken"),
                is(AbsentMatcher.<CharSequence>absent()));
    }

}