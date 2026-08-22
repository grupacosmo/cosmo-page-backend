package com.webdev.cosmo.cosmobackend.service.external.webhook;

import com.webdev.cosmo.cosmobackend.service.external.webhook.models.NotifStrategyRecord;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.WebhookNotification;
import org.springframework.web.bind.annotation.*;

/**
 * Entry point for Facebook webhook traffic: handshake verification (GET) and
 * post-change notifications (POST).
 */
@Slf4j
@RestController
@RequestMapping("/api/facebook/notif")
@RequiredArgsConstructor
public class FacebookWebhookController {
    private final Strategy<Integer, NotifContext> notifStrategy;
    private final WebhookNotificationConsumer webhookNotificationConsumer;

    @Resource(name = "notifContext")
    private NotifContext notifContext;

    @GetMapping
    public Integer triggerNotif(
            @RequestParam("hub.mode") String subscribe,
            @RequestParam("hub.challenge") int challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        log.info(verifyToken);
        log.info(String.valueOf(challenge));
        log.info(subscribe);
        notifContext.setNotifStrategyRecord(NotifStrategyRecord.init(verifyToken, challenge, subscribe));

        return notifStrategy.run(notifContext);
    }

    @PostMapping
    public void sampleWebhookTest(@RequestBody WebhookNotification body) {
        webhookNotificationConsumer.accept(body);
    }
}