package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import org.openapitools.model.UserModel;

public interface UserService {

     UserModel save(User user);
     UserModel findByEmail(String email);
     UserModel updateUser(User user);
     void deleteByEmail(String email);
}
