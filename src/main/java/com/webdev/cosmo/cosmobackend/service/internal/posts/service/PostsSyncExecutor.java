package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.FacebookDataItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsSyncExecutor implements Executor {

    private final Supplier<List<FacebookDataItem>> facebookDataItemProvider;
    private final PostRepository postRepository;
    private final BiFunction<List<FacebookDataItem>, List<Post>, List<FacebookDataItem>> unsavedPostsExtractor;
    private final Function<List<FacebookDataItem>, List<Post>> postsFromDataItemsBuilder;

    @Override
    public void execute() {
        List<FacebookDataItem> facebookDataItems = facebookDataItemProvider.get();

        log.info("Successfully obtained {} items from facebook.", facebookDataItems.size());
        List<FacebookDataItem> facebookDataItemsToBeAdded = unsavedPostsExtractor.apply(facebookDataItems, postRepository.findAll());
        postRepository.saveAll(postsFromDataItemsBuilder.apply(facebookDataItemsToBeAdded));
//        log.info("Added ");
    }
}
