package com.webdev.cosmo.cosmobackend.service.internal.posts.mapper;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.openapitools.model.FacebookDataItem;
import org.openapitools.model.FacebookPostImage;
import org.openapitools.model.FacebookPostMedia;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.PostListQueryItemDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class PostMapperTest {

    private final PostMapper mapper = new PostMapperImpl();

    @Test
    void mapsImageToMutableList() {
        FacebookPostImage image = new FacebookPostImage().src("http://img").width(100).height(50);

        List<FacebookImage> result = mapper.map(image);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSrc()).isEqualTo("http://img");
        assertThatCode(result::clear).doesNotThrowAnyException();
    }

    @Test
    void mapsNullImageToEmptyMutableList() {
        List<FacebookImage> result = mapper.map((FacebookPostImage) null);

        assertThat(result).isEmpty();
        assertThatCode(result::clear).doesNotThrowAnyException();
    }

    @Test
    void emptyAttachmentsClearsImagesWithoutThrowing() {
        Post post = new Post();
        post.getFacebookImages().add(new FacebookImage().setSrc("old"));
        FacebookResponse attachments = new FacebookResponse().data(List.of());

        assertThatCode(() -> mapper.mapPostFromFacebookData(
                Pair.of(new FacebookDataItem().id("1").message("message"), attachments), post))
                .doesNotThrowAnyException();

        assertThat(post.getFacebookImages()).isEmpty();
    }

    @Test
    void mapsFacebookItemToNewPostWithoutOverridingEntityId() {
        Post post = mapper.mapPostFromFacebookData(
                new FacebookDataItem().id("1001").message("Sample message"));

        assertThat(post.getProviderId()).isEqualTo("1001");
        assertThat(post.getId()).isNull();
        assertThat(post.getDescription()).isEqualTo("Sample message");
    }

    @Test
    void attachmentWithMediaSetsImages() {
        Post post = new Post();
        FacebookResponse attachments = new FacebookResponse().data(List.of(
                new FacebookDataItem().media(
                        new FacebookPostMedia().image(
                                new FacebookPostImage().src("http://img2").width(200).height(100)))));

        mapper.mapPostFromFacebookData(Pair.of(new FacebookDataItem().id("1"), attachments), post);

        assertThat(post.getFacebookImages()).hasSize(1);
        assertThat(post.getFacebookImages().get(0).getSrc()).isEqualTo("http://img2");
    }

    @Test
    void detailsImagesComeFromFacebookImagesNotRawImages() {
        PostMapper mapper = new PostMapperImpl();
        mapper.facebookImageMapper = new FacebookImageMapperImpl();

        Post post = new Post();
        post.setId("p1");
        post.setDescription("desc");
        post.getFacebookImages().add(new FacebookImage().setSrc("http://img").setWidth(100).setHeight(50));

        PostListQueryItemDetails details = mapper.mapPostListQueryItemDetails(post);

        assertThat(details.getId()).isEqualTo("p1");
        assertThat(details.getDescription()).isEqualTo("desc");
        assertThat(details.getImages()).hasSize(1);
        assertThat(details.getImages().get(0).getSrc()).isEqualTo("http://img");
    }
}