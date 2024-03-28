package com.webdev.cosmo.cosmobackend.service.create;

import com.webdev.cosmo.cosmobackend.service.api.Image;
import com.webdev.cosmo.cosmobackend.service.common.mapper.ImageMapper;
import com.webdev.cosmo.cosmobackend.service.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ImageModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_IMAGE_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public Flux<ImageModel> save(List<MultipartFile> images) {
        return imageRepository.saveAll(imageMapper.map(images))
                .map(imageMapper::mapToModel);
    }

    public Mono<byte[]> findById(String id) {
        return imageRepository.findById(id)
                .map(Image::getData)
                .switchIfEmpty(Mono.error(INVALID_IMAGE_DATA.getError()));
    }
}
