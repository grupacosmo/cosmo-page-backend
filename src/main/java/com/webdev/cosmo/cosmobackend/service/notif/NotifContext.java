package com.webdev.cosmo.cosmobackend.service.notif;

import com.webdev.cosmo.cosmobackend.service.notif.models.NotifStrategyRecord;
import lombok.Setter;

public class NotifContext implements TokenFromRequestProvider, ChallengeTokenProvider {

    @Setter
    private NotifStrategyRecord notifStrategyRecord;

    @Override
    public String getToken() {
        return notifStrategyRecord.token();
    }

    @Override
    public Integer getChallengeToken() {
        return notifStrategyRecord.challengeToken();
    }
}
