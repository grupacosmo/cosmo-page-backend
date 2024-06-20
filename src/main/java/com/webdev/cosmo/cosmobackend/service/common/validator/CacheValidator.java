package com.webdev.cosmo.cosmobackend.service.common.validator;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.util.interfaces.Validator;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class CacheValidator implements Validator<Cache> {

    @Override
    public boolean exists(Cache cache) {
        return StringUtils.isNotEmpty(cache.getPageAccessToken());
    }
}
