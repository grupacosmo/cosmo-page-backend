package com.webdev.cosmo.cosmobackend.service.internal.posts.service.builder;

import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import com.webdev.cosmo.cosmobackend.util.interfaces.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.FacebookCursor;
import org.openapitools.model.FacebookDataItem;
import org.openapitools.model.FacebookPaging;
import org.openapitools.model.FacebookResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_CACHE_DATA;
import static com.webdev.cosmo.cosmobackend.util.ThrowableUtils.throwIf;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacebookDataItemProvider implements Supplier<List<FacebookDataItem>> {
    private final Validator<Cache> cacheValidator;
    private final Cache cache;
    private final FacebookClient facebookClient;

    @Override
    public List<FacebookDataItem> get() {
        throwIf(!cacheValidator.exists(cache), INVALID_CACHE_DATA.getError());

        FacebookResponse facebookResponse = facebookClient.getPostsPage(cache.getPageId(), cache.getPageAccessToken(), 100);
        List<FacebookDataItem> facebookDataItems = new ArrayList<>(facebookResponse.getData());

        while(hasNext(facebookResponse)) {

            facebookResponse = facebookClient.subsequentRetrieve(cache.getPageId(),
                    cache.getPageAccessToken(), 100, facebookResponse.getPaging().getCursors().getAfter());

            facebookDataItems.addAll(facebookResponse.getData());
        }

        return facebookDataItems;
    }

    private boolean hasNext(FacebookResponse facebookResponse) {
        return Optional.ofNullable(facebookResponse)
                .map(FacebookResponse::getPaging)
                .map(FacebookPaging::getCursors)
                .map(FacebookCursor::getAfter)
                .filter(StringUtils::isNotEmpty)
                .isPresent();
    }
}
