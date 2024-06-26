package com.webdev.cosmo.cosmobackend.service.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaginationContext {
    private int page;
    private int size;
}
