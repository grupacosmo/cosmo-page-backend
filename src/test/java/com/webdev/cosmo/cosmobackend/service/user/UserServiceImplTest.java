package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.error.ServiceError;
import com.webdev.cosmo.cosmobackend.service.api.ROLE;
import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserModel;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void save_savesAndMaps() {
        User user = new User().setEmail("user@example.com").setRole(ROLE.USER);
        UserModel model = new UserModel().email("user@example.com");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToModel(user)).thenReturn(model);

        UserModel result = userService.save(user);

        assertThat(result).isEqualTo(model);
        verify(userRepository).save(user);
    }

    @Test
    void findByEmail_existingUserReturnsMappedModel() {
        User user = new User().setEmail("user@example.com");
        UserModel model = new UserModel().email("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userMapper.mapToModel(user)).thenReturn(model);

        UserModel result = userService.findByEmail("user@example.com");

        assertThat(result).isEqualTo(model);
    }

    @Test
    void findByEmail_missingUserThrowsServiceError() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("missing@example.com"))
                .isInstanceOf(ServiceError.class);
    }

    @Test
    void updateUser_updatesAndSaves() {
        User existing = new User().setEmail("user@example.com").setName("old");
        User update = new User()
                .setEmail("user@example.com")
                .setName("new")
                .setSurname("surname")
                .setRole(ROLE.ADMIN)
                .setId("1")
                .setCreationDate(LocalDateTime.now());
        UserModel model = new UserModel().email("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));
        when(userMapper.mapToModel(existing)).thenReturn(model);

        UserModel result = userService.updateUser(update);

        assertThat(result).isEqualTo(model);
        assertThat(existing.getName()).isEqualTo("new");
        assertThat(existing.getSurname()).isEqualTo("surname");
        assertThat(existing.getRole()).isEqualTo(ROLE.ADMIN);
        verify(userRepository).save(existing);
    }

    @Test
    void updateUser_missingUserThrowsServiceError() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(new User().setEmail("missing@example.com")))
                .isInstanceOf(ServiceError.class);
    }

    @Test
    void deleteByEmail_existingUserDeletesById() {
        User user = new User().setEmail("user@example.com").setId("1");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        userService.deleteByEmail("user@example.com");

        verify(userRepository).deleteById("1");
    }

    @Test
    void deleteByEmail_missingUserThrowsServiceError() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteByEmail("missing@example.com"))
                .isInstanceOf(ServiceError.class);
    }
}