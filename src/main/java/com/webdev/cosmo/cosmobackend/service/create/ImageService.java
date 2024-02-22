package com.webdev.cosmo.cosmobackend.service.create;

import com.webdev.cosmo.cosmobackend.service.api.Image;
import com.webdev.cosmo.cosmobackend.service.common.mapper.ImageMapper;
import com.webdev.cosmo.cosmobackend.service.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ImageModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_IMAGE_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public List<ImageModel> save(List<MultipartFile> images) {
        List<Image> savedImages = imageRepository.saveAll(imageMapper.map(images));

        log.info("Saved Images: " + savedImages);

        return imageMapper.mapToModel(savedImages);
    }

    public byte[] findById(String id) {
        return imageRepository.findById(id)
                .map(Image::getData)
                .orElseThrow(INVALID_IMAGE_DATA::getError);
    }
}
