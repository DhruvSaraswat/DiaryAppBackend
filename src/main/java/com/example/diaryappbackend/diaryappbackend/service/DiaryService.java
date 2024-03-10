package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.encryption.EncryptionConfig;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import com.example.diaryappbackend.diaryappbackend.model.entity.EncryptedDiaryEntryItem;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.vault.ClientEncryption;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final MongoTemplate mongo;
    private final EncryptionConfig encryptionConfig;
    private final ClientEncryption clientEncryption;
    private final String algorithm;

    public DiaryService(MongoTemplate mongo,
                        EncryptionConfig encryptionConfig,
                        ClientEncryption clientEncryption,
                        @Value("${encryption.algorithm}") String algorithm) {
        this.mongo = mongo;
        this.encryptionConfig = encryptionConfig;
        this.clientEncryption = clientEncryption;
        this.algorithm = algorithm;
    }

    public Binary encrypt(BsonValue bsonValue, String algorithm) {
        Objects.requireNonNull(bsonValue);
        Objects.requireNonNull(algorithm);

        EncryptOptions options = new EncryptOptions(algorithm);
        options.keyId(encryptionConfig.getDataKeyId());

        BsonBinary encryptedValue = clientEncryption.encrypt(bsonValue, options);
        return new Binary(encryptedValue.getType(), encryptedValue.getData());
    }

    Binary encrypt(String value, String algorithm) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(algorithm);

        return encrypt(new BsonString(value), algorithm);
    }

    public BsonValue decryptProperty(Binary value) {
        Objects.requireNonNull(value);
        return clientEncryption.decrypt(new BsonBinary(value.getType(), value.getData()));
    }

    private DiaryEntryItem decrypt(EncryptedDiaryEntryItem encrypted) {
        Objects.requireNonNull(encrypted);
        DiaryEntryItem diaryEntryItem = new DiaryEntryItem();
        diaryEntryItem.setUserId(encrypted.getUserId());
        diaryEntryItem.setLastEditedAtTimestamp(encrypted.getLastEditedAtTimestamp());
        diaryEntryItem.setDiaryTimestamp(encrypted.getDiaryTimestamp());
        diaryEntryItem.setCreatedAtTimestamp(encrypted.getCreatedAtTimestamp());

        BsonValue decryptedTitle = encrypted.getTitle() != null ? decryptProperty(encrypted.getTitle()) : null;
        if (decryptedTitle != null) {
            diaryEntryItem.setTitle(decryptedTitle.asString().getValue());
        }

        BsonValue decryptedStory = encrypted.getStory() != null ? decryptProperty(encrypted.getStory()) : null;
        if (decryptedStory != null) {
            diaryEntryItem.setStory(decryptedStory.asString().getValue());
        }

        return diaryEntryItem;
    }

    public void save(DiaryEntry diaryEntry, String userId) {
        EncryptedDiaryEntryItem encryptedDiaryEntryItem = new EncryptedDiaryEntryItem();
        encryptedDiaryEntryItem.setUserId(userId);
        encryptedDiaryEntryItem.setDiaryTimestamp(diaryEntry.getDiaryTimestamp());
        encryptedDiaryEntryItem.setCreatedAtTimestamp(diaryEntry.getCreatedAtTimestamp());
        encryptedDiaryEntryItem.setLastEditedAtTimestamp(diaryEntry.getLastEditedAtTimestamp());

        if (diaryEntry.getTitle() != null) {
            encryptedDiaryEntryItem.setTitle(encrypt(diaryEntry.getTitle(), algorithm));
        } else {
            encryptedDiaryEntryItem.setTitle(null);
        }

        if (diaryEntry.getStory() != null) {
            encryptedDiaryEntryItem.setStory(encrypt(diaryEntry.getStory(), algorithm));
        } else {
            encryptedDiaryEntryItem.setStory(null);
        }

        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("diaryTimestamp").is(diaryEntry.getDiaryTimestamp()));

        Update updateDefinition = new Update()
                .set("userId", userId)
                .set("diaryTimestamp", encryptedDiaryEntryItem.getDiaryTimestamp())
                .set("title", encryptedDiaryEntryItem.getTitle())
                .set("story", encryptedDiaryEntryItem.getStory())
                .set("createdAtTimestamp", encryptedDiaryEntryItem.getCreatedAtTimestamp())
                .set("lastEditedAtTimestamp", encryptedDiaryEntryItem.getLastEditedAtTimestamp());

        final UpdateResult upsertResult = mongo.upsert(query, updateDefinition, EncryptedDiaryEntryItem.class);
        System.out.println(upsertResult);
    }

    public List<DiaryEntryItem> findAll(String userId) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
        List<EncryptedDiaryEntryItem> allEncrypted = mongo.find(query, EncryptedDiaryEntryItem.class);
        return allEncrypted.stream()
                .map(this::decrypt)
                .collect(Collectors.toList());
    }

    public void deleteDiaryEntry(String userId, long diaryTimestamp) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("diaryTimestamp").is(diaryTimestamp));
        DeleteResult deleteResult = mongo.remove(query, EncryptedDiaryEntryItem.class);
        System.out.println(deleteResult);
    }
}
