package com.webdev.cosmo.cosmobackend.security.mapper;

import com.webdev.cosmo.cosmobackend.security.FacebookAuthentication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.FacebookUser;

@Mapper
public interface FacebookAuthenticationMapper {

    @Mapping(target = "authenticated", constant = "true")
    FacebookAuthentication map(FacebookUser user);

}
