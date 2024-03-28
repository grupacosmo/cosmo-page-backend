package com.webdev.cosmo.cosmobackend.service.api;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
public class Image {

    @Id
    private String id;
    private String name;
    private String type;
    private byte[] data;
}
