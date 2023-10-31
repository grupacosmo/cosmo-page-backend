package com.webdev.cosmo.cosmobackend.flows.notif;

import com.webdev.cosmo.cosmobackend.flows.notif.models.NotifStrategyRecord;
import com.webdev.cosmo.cosmobackend.flows.templates.ChallengeTokenProvider;
import com.webdev.cosmo.cosmobackend.flows.templates.TokenFromRequestProvider;
import lombok.Builder;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
