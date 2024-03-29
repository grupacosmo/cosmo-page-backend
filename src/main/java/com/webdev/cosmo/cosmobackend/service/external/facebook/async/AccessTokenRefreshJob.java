package com.webdev.cosmo.cosmobackend.service.external.facebook.async;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenRefreshJob extends QuartzJobBean {

    private final AccessTokenService accessTokenService;

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        log.info("Executing scheduled process");
        final String token = accessTokenService.getAccessToken();
        log.info("Ending scheduled process with result token: {}", token);
    }
}
