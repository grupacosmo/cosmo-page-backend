//package com.webdev.cosmo.cosmobackend.posts.controller;
//
//import com.webdev.cosmo.cosmobackend.service.internal.posts.mapper.PostMapper;
//import com.webdev.cosmo.cosmobackend.service.internal.posts.model.Post;
//import com.webdev.cosmo.cosmobackend.service.internal.posts.repository.PostRepository;
//import com.webdev.cosmo.cosmobackend.service.internal.posts.service.PostServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.openapitools.model.PostModel;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//    @Mock
//    private PostMapper mapper;
//    @InjectMocks
//    private PostServiceImpl postService;
//
//    @Test
//    void getAllPosts_shouldReturnListOfPosts() {
//        // given
//        List<Post> posts = new ArrayList<>();
//        posts.add(new Post()
//                .setId("id_1")
//                .setTitle("Nastepna rakieta wystrzelona")
//                .setDescription("To juz 50 w naszej karierze");
////                .setImageIds(Arrays.asList("img1", "img2")));
//        posts.add(new Post()
//                .setId("id_2")
//                .setTitle("Spotkanie po wakacjach")
//                .setDescription("No nareszcie!");
////                .setImageIds(Arrays.asList("img1")));
//
//        List<PostModel> mappedPosts = new ArrayList<>();
//        mappedPosts.add(new PostModel()
//                .id("id_1")
//                .title("Nastepna rakieta wystrzelona")
//                .description("To juz 50 w naszej karierze")
//                .imageIds(Arrays.asList("img1", "img2")));
//        mappedPosts.add(new PostModel()
//                .id("id_2")
//                .title("Spotkanie po wakacjach")
//                .description("No nareszcie!")
//                .imageIds(Arrays.asList("img1")));
//
//        when(postRepository.findAll()).thenReturn(posts);
//        when(mapper.map(posts)).thenReturn(mappedPosts);
//
//        // when
//        List<PostModel> result = postService.getAllPosts();
//
//        // then
//        assertEquals(mappedPosts, result);
//    }
//
//    @Test
//    void getPostById_existingId_shouldReturnPost() {
//        // given
//        String postId = "id_1";
//        Post post = new Post()
//                .setId("id_1")
//                .setTitle("Spotkanie po wakacjach")
//                .setDescription("No nareszcie!")
//                .setImageIds(Arrays.asList("img1"));
//        PostModel mappedPost = new PostModel()
//                .id("id_1")
//                .title("Spotkanie po wakacjach")
//                .description("No nareszcie!")
//                .imageIds(Arrays.asList("img1"));
//
//        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
//        when(mapper.map(post)).thenReturn(mappedPost);
//
//        // when
//        PostModel result = postService.getPostById(postId);
//
//        // then
//        assertNotNull(result);
//        assertEquals(postId, result.getId());
//        assertEquals("Spotkanie po wakacjach", result.getTitle());
//        assertEquals("No nareszcie!", result.getDescription());
//        assertEquals(Arrays.asList("img1"), result.getImageIds());
//    }
//
//    @Test
//    void getPostById_nonExistingId_shouldReturnINVALID_REQUEST() {
//
//    }
//
//    @Test
//    void createPost_shouldReturnSavedPost() {
//        //given
//        Post post = new Post()
//                .setId("id_1")
//                .setTitle("Spotkanie po wakacjach")
//                .setDescription("No nareszcie!")
//                .setImageIds(Arrays.asList("img1"));
//        PostModel mappedPost = new PostModel()
//                .id("id_1")
//                .title("Spotkanie po wakacjach")
//                .description("No nareszcie!")
//                .imageIds(Arrays.asList("img1"));
//
//        when(postRepository.save(post)).thenReturn(post);
//        when(mapper.map(post)).thenReturn(mappedPost);
//
//        //when
//        PostModel result = postService.createPost(post);
//
//        //then
//        assertNotNull(result);
//        assertEquals("id_1", result.getId());
//        assertEquals("Spotkanie po wakacjach", result.getTitle());
//        assertEquals("No nareszcie!", result.getDescription());
//        assertEquals(Arrays.asList("img1"), result.getImageIds());
//    }
//
//    @Test
//    public void updatePost_existingId_shouldReturnUpdatedPost() {
//
//    }
//
//    @Test
//    public void updatePost_nonExistingId_shouldReturnINVALID_REQUEST() {
//
//    }
//
//    @Test
//    public void deletePost_existingId_shouldDeletePost() {
//        // given
//        String postId = "id_1";
//        Post post = new Post()
//                .setId("id_1")
//                .setTitle("Spotkanie po wakacjach")
//                .setDescription("No nareszcie!")
//                .setImageIds(Arrays.asList("img1"));
//
//        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
//
//        // when
//        postService.deletePost(postId);
//
//        //then
//        verify(postRepository, times(1)).delete(post);
//    }
//
//    @Test
//    public void deletePost_nonExistingId_shouldReturnINVALID_REQUEST() {
//
//    }
//}
