package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.openapitools.model.UserModel;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserModel postUser(@Valid @RequestBody User user){
        return userService.save(user);
    }

    @GetMapping(value = "/{email}")
    public UserModel getUser(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PutMapping
    public UserModel updateUser(@Valid @RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping(value ="{email}")
    public Map<String, String> deleteUser(@PathVariable String email){
         userService.deleteByEmail(email);
        return Map.of("email", email);
    }
}
