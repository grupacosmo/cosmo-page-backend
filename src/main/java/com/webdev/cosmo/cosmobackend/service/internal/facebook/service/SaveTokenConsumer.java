package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.util.BetterOptional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.TokenModel;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class SaveTokenConsumer implements Consumer<TokenModel> {

    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final Cache cache;

    @Override
    public void accept(TokenModel tokenModel) {
        List<Token> existingTokens = tokenRepository.findAll();

        tokenRepository.deleteAll();
        log.info("Deleted tokens: {}. Attempting to save new token", existingTokens.size());

        BetterOptional.of(tokenModel)
                .optionalMap(tokenMapper::map)
                .map(tokenRepository::save)
                .orElseThrow();

        log.info("Overriding token in cache.");
        cache.setPageAccessToken(tokenModel.getToken());
        cache.setPageId(tokenModel.getPageId());
    }
}
