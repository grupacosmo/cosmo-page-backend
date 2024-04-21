package com.webdev.cosmo.cosmobackend.util.interfaces;

import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Predicate;

public interface QueryService<RS, ID> extends SimpleQueryService<ID, RS>,
        ListQueryService<RS> {

    default RS findByCriteria(Predicate<RS> predicate) {
        throw new NotImplementedException();
    }

}
