package com.example.diaryappbackend.diaryappbackend.encryption;

import org.bson.BsonBinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${master.key.path}")
    private String masterKeyPath;

    @Value("${key.vault.namespace}")
    private String keyVaultNamespace;

    @Value("${key.vault.alias}")
    private String keyVaultAlias;

    private BsonBinary dataKeyId;

    public String getMasterKeyPath() {
        return masterKeyPath;
    }

    public String getKeyVaultNamespace() {
        return keyVaultNamespace;
    }

    public String getKeyVaultAlias() {
        return keyVaultAlias;
    }

    public BsonBinary getDataKeyId() {
        return dataKeyId;
    }

    public void setDataKeyId(BsonBinary dataKeyId) {
        this.dataKeyId = dataKeyId;
    }
}
