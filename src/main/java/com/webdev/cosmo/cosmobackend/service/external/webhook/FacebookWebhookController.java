package com.webdev.cosmo.cosmobackend.service.external.webhook;

import com.webdev.cosmo.cosmobackend.service.external.webhook.NotifContext;
import com.webdev.cosmo.cosmobackend.service.external.webhook.Strategy;
import com.webdev.cosmo.cosmobackend.service.external.webhook.models.NotifStrategyRecord;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FacebookWebhookController {
    private final Strategy<Integer, NotifContext> notifStrategy;

    @Resource(name = "notifContext")
    private NotifContext notifContext;

    @GetMapping("/api/facebook/notif")
    public Integer triggerNotif(
            @RequestParam("hub.mode") String subscribe,
            @RequestParam("hub.challenge") int challenge,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestBody String body
    ) {
        log.info(verifyToken);
        log.info(String.valueOf(challenge));
        log.info(subscribe);
        log.info(body);
        notifContext.setNotifStrategyRecord(NotifStrategyRecord.init(verifyToken, challenge, subscribe));

        return notifStrategy.run(notifContext);
    }

    @PostMapping("/api/facebook/notif")
    public void sampleWebhookTest(@RequestBody String body) {
        log.info(body);
    }
}
