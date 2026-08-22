package com.webdev.cosmo.cosmobackend.service.common.validator;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CacheValidator {

    public boolean exists(Cache cache) {
        return StringUtils.isNotEmpty(cache.getPageAccessToken());
    }
}
