package com.webdev.cosmo.cosmobackend.service.internal.posts.mapper;

import com.webdev.cosmo.cosmobackend.service.api.FacebookImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.FacebookPostImage;

@Mapper
public abstract class FacebookImageMapper {

    public abstract FacebookPostImage map(FacebookPostImage facebookPostImage);
    public abstract FacebookPostImage map(FacebookImage facebookPostImage);
}
