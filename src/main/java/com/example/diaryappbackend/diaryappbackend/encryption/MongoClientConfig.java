package com.example.diaryappbackend.diaryappbackend.encryption;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Arrays;

@Configuration
public class MongoClientConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String db;

    @Autowired
    private EncryptionConfig encryptionConfig;

    @Override
    protected String getDatabaseName() {
        return db;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        ClientEncryption clientEncryption = clientEncryption();
        encryptionConfig.setDataKeyId(createOrRetrieveDataKey(clientEncryption));
        return MongoClients.create(clientSettings());
    }

    @Bean
    public ClientEncryption clientEncryption() {
        ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
                .keyVaultMongoClientSettings(clientSettings())
                .keyVaultNamespace(encryptionConfig.getKeyVaultNamespace())
                .kmsProviders(LocalKmsUtils.providersMap(encryptionConfig.getMasterKeyPath()))
                .build();

        return ClientEncryptions.create(clientEncryptionSettings);
    }

    private MongoClientSettings clientSettings() {
        return MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri)).build();
    }

    private BsonBinary createOrRetrieveDataKey(ClientEncryption clientEncryption) {
        BsonDocument key = clientEncryption.getKeyByAltName(encryptionConfig.getKeyVaultAlias());
        if (key == null) {
            createKeyUniqueIndex();

            DataKeyOptions options = new DataKeyOptions();
            options.keyAltNames(Arrays.asList(encryptionConfig.getKeyVaultAlias()));
            return clientEncryption.createDataKey("local", options);
        } else {
            return (BsonBinary) key.get("_id");
        }
    }

    private void createKeyUniqueIndex() {
        try (MongoClient client = MongoClients.create(clientSettings())) {
            MongoNamespace namespace = new MongoNamespace(encryptionConfig.getKeyVaultNamespace());
            MongoCollection<Document> keyVault = client.getDatabase(namespace.getDatabaseName())
                    .getCollection(namespace.getCollectionName());

            keyVault.createIndex(Indexes.ascending("keyAltNames"), new IndexOptions().unique(true)
                    .partialFilterExpression(Filters.exists("keyAltNames")));
        }
    }
}
