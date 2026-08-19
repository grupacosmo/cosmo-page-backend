package com.webdev.cosmo.cosmobackend.service.internal.posts.service;

import com.webdev.cosmo.cosmobackend.error.ServiceError;
import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.PostModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository repository;

    @Mock
    private PostMapper mapper;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void createPost_savesAndMaps() {
        Post post = new Post().setTitle("title").setDescription("description");
        PostModel model = new PostModel().title("title").description("description");
        when(repository.save(post)).thenReturn(post);
        when(mapper.map(post)).thenReturn(model);

        PostModel result = postService.createPost(post);

        assertThat(result).isEqualTo(model);
        verify(repository).save(post);
    }

    @Test
    void getPostById_existingPostReturnsMappedModel() {
        Post post = new Post().setId("1");
        PostModel model = new PostModel().id("1");
        when(repository.findById("1")).thenReturn(Optional.of(post));
        when(mapper.map(post)).thenReturn(model);

        PostModel result = postService.getPostById("1");

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getPostById_missingPostThrowsServiceError() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostById("1"))
                .isInstanceOf(ServiceError.class);
    }

    @Test
    void getAllPosts_mapsAllPosts() {
        PostModel model = new PostModel().id("1");
        when(repository.findAll()).thenReturn(List.of(new Post(), new Post()));
        when(mapper.map(anyList())).thenReturn(List.of(model));

        List<PostModel> result = postService.getAllPosts();

        assertThat(result).containsExactly(model);
    }

    @Test
    void updatePost_existingPostUpdatesAndSaves() {
        Post existing = new Post().setId("1").setTitle("old").setDescription("old-desc");
        Post update = new Post().setTitle("new").setDescription("new-desc");
        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        postService.updatePost("1", update);

        assertThat(existing.getTitle()).isEqualTo("new");
        assertThat(existing.getDescription()).isEqualTo("new-desc");
        verify(repository).save(existing);
    }

    @Test
    void updatePost_missingPostThrowsServiceError() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost("1", new Post()))
                .isInstanceOf(ServiceError.class);
    }

    @Test
    void deletePost_existingPostDeletesIt() {
        Post post = new Post().setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(post));

        postService.deletePost("1");

        verify(repository).delete(post);
    }

    @Test
    void deletePost_missingPostThrowsServiceError() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost("1"))
                .isInstanceOf(ServiceError.class);
    }
}