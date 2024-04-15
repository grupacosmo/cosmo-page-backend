package com.webdev.cosmo.cosmobackend.service.internal.image;

import com.webdev.cosmo.cosmobackend.service.internal.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ImageModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public List<ImageModel> uploadImage(@RequestParam("images") List<MultipartFile> images) {
        return imageService.save(images);
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String id) {
        return imageService.findById(id);
    }
}
