package com.webdev.cosmo.cosmobackend.service.internal.facebook.service;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import com.webdev.cosmo.cosmobackend.service.common.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper.TokenMapper;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.repository.TokenRepository;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.LongLivedAccessToken;
import org.openapitools.model.TokenModel;

import java.util.List;
import java.util.function.Consumer;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_ACCESS_TOKEN;

/**
 * Replaces the stored page token with a long-lived one obtained from the Facebook
 * exchange endpoint and refreshes the in-memory cache.
 */
@Slf4j
@RequiredArgsConstructor
public class SaveTokenConsumer implements Consumer<TokenModel> {

    private static final String COSMO_PK_PAGE = "COSMO PK";
    private static final String EXCHANGE_TOKEN_GRANT_TYPE = "fb_exchange_token";
    private final String clientId;
    private final String clientSecret;
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final Cache cache;
    private final FacebookClient facebookClient;

    @Override
    public void accept(TokenModel tokenModel) {
        Pair<String, String> pageIdPageTokenPair = retrievePageAccessToken(tokenModel.getToken());

        LongLivedAccessToken longLivedToken = facebookClient.getLongLivedToken(
                clientId,
                clientSecret,
                EXCHANGE_TOKEN_GRANT_TYPE,
                pageIdPageTokenPair.getRight()
        );

        List<Token> existingTokens = tokenRepository.findAll();

        tokenRepository.deleteAll();
        log.info("Deleted tokens: {}. Attempting to save new token", existingTokens.size());

        tokenModel.setPageId(pageIdPageTokenPair.getLeft());
        tokenModel.setToken(longLivedToken.getAccessToken());

        Token token = tokenMapper.map(tokenModel);
        token.setValidityPeriod(longLivedToken.getExpiresIn().toString());
        tokenRepository.save(token);

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
