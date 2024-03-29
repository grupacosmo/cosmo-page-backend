package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.UserModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserModel save(User user) {
        User savedUser = userRepository.save(user);

        log.info("Saved User: " + savedUser);

        return userMapper.mapToModel(savedUser);
    }

    public UserModel findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null)
            return userMapper.mapToModel(user);
        else throw new RuntimeException("Could not find user");
    }

    public UserModel putUser(User user) {
        if(userRepository.existsById(user.getId())){
            userRepository.deleteById(user.getId());
            userRepository.save(user);
            return userMapper.mapToModel(user);
        }
        else throw new RuntimeException("Could not put user");
    }

    public String deleteByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            userRepository.deleteById(user.getId());
            return user.getEmail();
        }
        else throw new RuntimeException("Could not delete user by email");
    }
}
