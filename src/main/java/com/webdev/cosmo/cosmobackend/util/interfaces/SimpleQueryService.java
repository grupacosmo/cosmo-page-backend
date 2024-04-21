package com.webdev.cosmo.cosmobackend.util.interfaces;

import org.apache.commons.lang3.NotImplementedException;

public interface SimpleQueryService<ID, RS> {
    default RS findBy(ID id) {
        throw new NotImplementedException();
    };
}
