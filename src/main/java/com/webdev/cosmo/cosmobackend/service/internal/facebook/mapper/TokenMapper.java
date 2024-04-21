package com.webdev.cosmo.cosmobackend.service.internal.facebook.mapper;

import com.webdev.cosmo.cosmobackend.service.api.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.TokenModel;

@Mapper
public interface TokenMapper {

    @Mapping(source = "token", target = "value")
    Token map(TokenModel token);
}
