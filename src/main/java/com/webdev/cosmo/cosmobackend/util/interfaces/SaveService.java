package com.webdev.cosmo.cosmobackend.util.interfaces;


import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public interface SaveService<RQ, RS> {
    default RQ save(RS rs) {
        throw new NotImplementedException();
    }
    default List<RQ> saveAll(List<RS> rs) {
        throw new NotImplementedException();
    }
}
