package com.webdev.cosmo.cosmobackend.service.mapper;

import com.webdev.cosmo.cosmobackend.service.api.InternalPageSecret;
import org.mapstruct.Mapper;
import org.openapitools.model.InternalPageSecretModel;

@Mapper
public interface InternalPageSecretMapper {
    InternalPageSecret map(InternalPageSecretModel model);
    InternalPageSecretModel map(InternalPageSecret internalPageSecret);
}
