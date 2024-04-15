package com.webdev.cosmo.cosmobackend.users;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import com.webdev.cosmo.cosmobackend.service.user.UserController;
import com.webdev.cosmo.cosmobackend.service.user.UserService;
import com.webdev.cosmo.cosmobackend.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserModel;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final org.openapitools.model.ROLE USER = org.openapitools.model.ROLE.USER;
    @Mock
    private UserService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void testPostUser() {
        UserModel userModel = new UserModel();
        userModel.setRole(USER);
        userModel.setEmail("test@example.com");

        User user = new User();
        user.setRole(com.webdev.cosmo.cosmobackend.service.api.ROLE.USER);
        user.setEmail("test@example.com");

        // When
        when(mapper.mapToModel(Optional.of(user))).thenReturn(userModel);
        when(userRepository.save(user)).thenReturn(user);

        UserModel result = userService.save(user);
        // Then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getRole()).isEqualTo(USER);
    }

    @Test
    void testPutUser() {
        UserModel userModel = new UserModel();
        userModel.setRole(USER);
        userModel.setEmail("test@example.com");

        User user = new User();
        user.setRole(com.webdev.cosmo.cosmobackend.service.api.ROLE.USER);
        user.setEmail("test@example.com");

        // When
        when(mapper.mapToModel(Optional.of(user))).thenReturn(userModel);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserModel result = userService.updateUser(user);
        // Then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getRole()).isEqualTo(USER);
    }

    @Test
    void testDeleteUser() {

        User user = new User();
        user.setRole(com.webdev.cosmo.cosmobackend.service.api.ROLE.USER);
        user.setEmail("test@example.com");
        user.setId("123");

        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getId());
        userService.deleteByEmail(user.getEmail());

        //then
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testGetUser() {

        UserModel userModel = new UserModel();
        userModel.setRole(USER);
        userModel.setEmail("test@example.com");

        User user = new User();
        user.setRole(com.webdev.cosmo.cosmobackend.service.api.ROLE.USER);
        user.setEmail("test@example.com");

        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.mapToModel(Optional.of(user))).thenReturn(userModel);

        UserModel result = userService.findByEmail(user.getEmail());

        // Then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getRole()).isEqualTo(USER);
    }
}
