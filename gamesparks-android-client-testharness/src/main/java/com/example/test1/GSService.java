package com.example.test1;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;

/**
 * Created by scalla on 14/01/2018.
 */

public class GSService {
    public static void sendRegistrationRequest(String username, String password, GSEventConsumer<GSResponseBuilder.RegistrationResponse> r) {
        GSAndroidPlatform.gs().getRequestBuilder().createRegistrationRequest()
                .setUserName(username)
                .setPassword(password)
                .setDisplayName(username)
                .send(r);
    }

    public static void sendMatchMakingRequest(long skill, String shortCode, GSEventConsumer<GSResponseBuilder.MatchmakingResponse> r) {
        GSAndroidPlatform.gs().getRequestBuilder().createMatchmakingRequest()
                .setSkill(skill)
                .setMatchShortCode(shortCode)
                .send(r);
    }

    public static void sendEndSessionRequest(GSEventConsumer<GSResponseBuilder.EndSessionResponse> r) {
        GSAndroidPlatform.gs().getRequestBuilder().createEndSessionRequest()
                .send(r);
    }
}
