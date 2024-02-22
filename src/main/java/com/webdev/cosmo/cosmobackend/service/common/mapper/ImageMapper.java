package com.webdev.cosmo.cosmobackend.service.common.mapper;

import com.webdev.cosmo.cosmobackend.config.properties.EnvConfig;
import com.webdev.cosmo.cosmobackend.service.api.Image;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.openapitools.model.ImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Mapper
@Component
public abstract class ImageMapper {

    @Autowired
    protected EnvConfig envConfig;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "id", target = "url", qualifiedByName = "mapUrl")
    public abstract ImageModel mapToModel(Image image);

    @Mapping(source = "bytes", target = "data")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "contentType", target = "type")
    public abstract Image map(MultipartFile image) throws IOException;


    public abstract List<Image> map(List<MultipartFile> images);
    public abstract List<ImageModel> mapToModel(List<Image> images);

    @Named("mapUrl")
    protected String mapUrl(String id) {
        return envConfig.getUrl()
                .concat("/api/images/")
                .concat(id);
    }
}
