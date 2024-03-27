package com.webdev.cosmo.cosmobackend.service.common.mapper;

import com.webdev.cosmo.cosmobackend.service.api.ROLE;
import com.webdev.cosmo.cosmobackend.service.api.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.openapitools.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Component
public abstract class UserMapper {

    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    public abstract UserModel mapToModel(User user);

}
