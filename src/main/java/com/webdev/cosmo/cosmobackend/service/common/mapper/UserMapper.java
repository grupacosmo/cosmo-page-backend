package com.webdev.cosmo.cosmobackend.service.common.mapper;

import com.webdev.cosmo.cosmobackend.service.api.User;
import org.mapstruct.Mapper;
import org.openapitools.model.UserModel;

@Mapper
public interface UserMapper {

    UserModel mapToModel(User user);
}