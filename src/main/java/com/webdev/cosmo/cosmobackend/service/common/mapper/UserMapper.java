package com.webdev.cosmo.cosmobackend.service.common.mapper;

import com.webdev.cosmo.cosmobackend.service.api.User;
import org.mapstruct.Mapper;
import org.openapitools.model.UserModel;

import java.util.Optional;

@Mapper
public interface UserMapper {

    UserModel mapToModel(Optional<User> user);
}
