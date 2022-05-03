package com.example.ciyashop.utils.apicall;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.model.Verb;

/**
 * Created by Bhumi Shah on 11/21/2017.
 */

public class OneLeggedApi10 extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return null;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

}
