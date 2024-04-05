package com.webdev.cosmo.cosmobackend.service.internal.facebook.service.builder;

import com.webdev.cosmo.cosmobackend.service.api.InternalPageSecret;
import com.webdev.cosmo.cosmobackend.service.mapper.InternalPageSecretMapper;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.InternalPageSecretModel;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;

@RequiredArgsConstructor
public class InternalPageSecretBuilder implements Function<InternalPageSecretModel, InternalPageSecret> {

    private final InternalPageSecretMapper internalPageSecretMapper;

    @Override
    public InternalPageSecret apply(InternalPageSecretModel internalPageSecret) {
        return null;
    }

    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }
}
