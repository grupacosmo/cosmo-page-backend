package com.webdev.cosmo.cosmobackend.flows.notif.controller;

import com.webdev.cosmo.cosmobackend.flows.notif.NotifContext;
import com.webdev.cosmo.cosmobackend.flows.notif.models.NotifStrategyRecord;
import com.webdev.cosmo.cosmobackend.flows.templates.Strategy;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebook")
@RequiredArgsConstructor
public class FacebookWebhookController {
    private final Strategy<Integer, NotifContext> notifStrategy;

    @Resource(name = "notifContext")
    private NotifContext notifContext;

    @GetMapping("notif")
    public Integer triggerNotif(
            @RequestParam("hub.mode") String subscribe,
            @RequestParam("hub.challenge") int challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        notifContext.setNotifStrategyRecord(NotifStrategyRecord.init(verifyToken, challenge, subscribe));

        return notifStrategy.run(notifContext);
    }
}
