package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import com.webdev.cosmo.cosmobackend.service.api.ROLE;
import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserModel;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserIT extends BaseTestConfiguration {

    @MockBean
    private UserService userService;

    @Test
    void testPostUser() {
        User user = new User().setName("John").setSurname("Doe").setEmail("john.doe@example.com").setRole(ROLE.USER).setCreation_date(LocalDateTime.now());
        UserModel userModel = new UserModel();
        userModel.setEmail("john.doe@example.com");
        userModel.setRole(org.openapitools.model.ROLE.USER);

        when(userService.save(any(User.class))).thenReturn(userModel);

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity("/api/user", user, UserModel.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getBody().getRole()).isEqualTo(org.openapitools.model.ROLE.USER);

    }

    @Test
    void testPutUser() {
        User user = new User().setName("John").setSurname("Doe").setEmail("john.doe@example.com").setRole(ROLE.USER).setCreation_date(LocalDateTime.now());
        UserModel userModel = new UserModel();
        userModel.setEmail("john.doe@example.com");
        userModel.setRole(org.openapitools.model.ROLE.USER);

        when(userService.updateUser(any(User.class))).thenReturn(userModel);

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity("/api/user?user={user}", null, UserModel.class, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getBody().getRole()).isEqualTo(org.openapitools.model.ROLE.USER);
    }

    @Test
    void testDeleteUser() {
        when(userService.deleteByEmail(anyString())).thenReturn("john.doe@example.com");

        ResponseEntity<String> response = testRestTemplate.exchange("/api/user/{email}", HttpMethod.DELETE, null, String.class, "john.doe@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testGetUser() {
        UserModel userModel = new UserModel();
        userModel.setEmail("john.doe@example.com");
        userModel.setRole(org.openapitools.model.ROLE.USER);

        when(userService.findByEmail(anyString())).thenReturn(userModel);

        ResponseEntity<UserModel> response = testRestTemplate.getForEntity("/api/user/{email}", UserModel.class, userModel.getEmail());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(userModel.getEmail());
        assertThat(response.getBody().getRole()).isEqualTo(org.openapitools.model.ROLE.USER);
    }
}
