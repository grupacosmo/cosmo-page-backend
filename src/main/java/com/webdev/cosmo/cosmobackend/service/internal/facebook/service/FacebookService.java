package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.mapper.InternalPageSecretMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.FacebookInternalProfileRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.InternalPageSecretModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_FACEBOOK_PROFILE_DATA;

@Service
@RequiredArgsConstructor
public class FacebookService {
    private final AccessTokenService accessTokenService;
    private final InternalPageSecretMapper internalPageSecretMapper;
    private final FacebookInternalProfileRepository facebookInternalProfileRepository;

    public InternalPageSecretModel createNewPassword(InternalPageSecretModel internalPageSecretModel) {
        return Optional.of(internalPageSecretMapper.map(internalPageSecretModel))
                .map(facebookInternalProfileRepository::save)
                .map(internalPageSecretMapper::map)
                .orElseThrow(INVALID_FACEBOOK_PROFILE_DATA::getError);

    }
}
