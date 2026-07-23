package com.example.common;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.example.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * RSA 工具：前端公钥加密敏感字段，后端私钥解密
 */
@Component
public class RsaUtils {

    private final RSA rsa;
    private final String publicKeyPem;

    public RsaUtils(@Value("${rsa.private-key}") String privateKey,
                    @Value("${rsa.public-key}") String publicKey) {
        this.rsa = new RSA(privateKey, publicKey);
        String wrapped = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(Base64.getDecoder().decode(publicKey));
        this.publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" + wrapped + "\n-----END PUBLIC KEY-----";
    }

    public String getPublicKeyPem() {
        return publicKeyPem;
    }

    /** 解密前端传来的 Base64 密文；若不是密文则原样返回（兼容本地调试） */
    public String decrypt(String cipherOrPlain) {
        if (cipherOrPlain == null || cipherOrPlain.isEmpty()) {
            return cipherOrPlain;
        }
        try {
            return rsa.decryptStr(cipherOrPlain, KeyType.PrivateKey);
        } catch (Exception e) {
            // 非 RSA 密文时保持原样，避免开发期明文联调失败
            return cipherOrPlain;
        }
    }

    public String requireDecrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            throw new CustomException("密码不能为空");
        }
        try {
            return rsa.decryptStr(cipherText, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new CustomException("密码解密失败，请刷新后重试");
        }
    }
}
