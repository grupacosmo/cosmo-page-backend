package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.util.BetterOptional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.TokenModel;

import java.util.List;
import java.util.function.Consumer;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_ACCESS_TOKEN;
import static com.webdev.cosmo.cosmobackend.error.Error.TOKEN_SAVE_ERROR;

@Slf4j
@RequiredArgsConstructor
public class SaveTokenConsumer implements Consumer<TokenModel> {

    private static final String COSMO_PK_PAGE = "COSMO PK";
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final Cache cache;
    private final FacebookClient facebookClient;

    @Override
    public void accept(TokenModel tokenModel) {
        Pair<String, String> pageIdPageTokenPair = retrievePageAccessToken(tokenModel.getToken());

        List<Token> existingTokens = tokenRepository.findAll();

        tokenRepository.deleteAll();
        log.info("Deleted tokens: {}. Attempting to save new token", existingTokens.size());

        BetterOptional.of(tokenModel)
                .peek(model -> model.setPageId(pageIdPageTokenPair.getLeft()))
                .peek(model -> model.setToken(pageIdPageTokenPair.getRight()))
                .optionalMap(tokenMapper::map)
                .map(tokenRepository::save)
                .orElseThrow(TOKEN_SAVE_ERROR::getError);

        log.info("Overriding token in cache.");
        cache.setPageAccessToken(tokenModel.getToken());
        cache.setPageId(tokenModel.getPageId());
    }

    private Pair<String, String> retrievePageAccessToken(String token) {
        FacebookResponse facebookResponse = facebookClient.getUserInfo(token);

        return facebookResponse.getData()
                .stream()
                .filter(item -> StringUtils.equals(COSMO_PK_PAGE, item.getName()))
                .findFirst()
                .map(item -> Pair.of(item.getId(), item.getAccessToken()))
                .orElseThrow(INVALID_ACCESS_TOKEN::getError);
    }
}
