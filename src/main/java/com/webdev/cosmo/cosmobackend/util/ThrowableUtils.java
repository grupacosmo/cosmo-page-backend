package com.webdev.cosmo.cosmobackend.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThrowableUtils {

    public static void throwIf(boolean exists, RuntimeException runtimeException) {
        if(exists) {
            throw runtimeException;
        }
    }
}
