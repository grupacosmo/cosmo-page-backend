package com.webdev.cosmo.cosmobackend.util.interfaces;


import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public interface ListQueryService<RS> {
    default List<RS> findAll() {
        throw new NotImplementedException();
    }
}
