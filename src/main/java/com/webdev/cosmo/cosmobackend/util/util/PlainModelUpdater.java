package com.webdev.cosmo.cosmobackend.util.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class PlainModelUpdater {
    public static <T> T plainUpdate(T oldModel, T newModel) {
        Field[] fields = oldModel.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(newModel);
                Object existingValue = field.get(oldModel);
                if (newValue != null && !newValue.equals(existingValue)) {
                    field.set(oldModel, newValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error updating news", e);
            }
        }
        return oldModel;
    }
}
