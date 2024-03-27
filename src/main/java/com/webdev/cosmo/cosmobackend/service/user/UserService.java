package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.UserModel;
import org.springframework.stereotype.Service;
import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_USER_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserModel save(User user) {
        User savedUser = userRepository.save(user);

        log.info("Saved Users: " + savedUser);

        return userMapper.mapToModel(savedUser);
    }

    public UserModel findById(String id) {
        return userMapper.mapToModel(
                userRepository.findById(id).
                        orElseThrow(INVALID_USER_DATA::getError));
    }

    public UserModel putUser(User user) {
        if(userRepository.existsById(user.getId())){
            userRepository.deleteById(user.getId());
            userRepository.save(user);
            return userMapper.mapToModel(user);
        }
        else throw new RuntimeException("Could not put user");
    }

    public String deleteById(String id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return id;
        }
        else throw new RuntimeException("Could not delete user by id");
    }
}
