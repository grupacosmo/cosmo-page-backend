package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.openapitools.model.UserModel;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserModel postUser(@RequestBody User user){
        return userService.save(user);
    }

    @GetMapping(value = "/{email}")
    public UserModel getUser(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PutMapping
    public UserModel updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping(value ="{email}")
    public String deleteUser(@PathVariable String email){
        return userService.deleteByEmail(email);
    }
}
