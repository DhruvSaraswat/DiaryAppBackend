package com.example.diaryappbackend.diaryappbackend.dao;

import com.example.diaryappbackend.diaryappbackend.encryption.EncryptionConfig;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import com.example.diaryappbackend.diaryappbackend.model.entity.EncryptedDiaryEntryItem;
import com.example.diaryappbackend.diaryappbackend.model.error.DataDeletionException;
import com.example.diaryappbackend.diaryappbackend.model.error.DataPersistenceException;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.vault.ClientEncryption;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class DiaryRepositoryImpl implements DiaryRepository {

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private ClientEncryption clientEncryption;

    @Autowired
    private EncryptionConfig encryptionConfig;

    @Value("${encryption.algorithm}")
    private String algorithm;

    @Override
    public void upsert(String userId, DiaryEntry diaryEntry) {
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
        if (!upsertResult.wasAcknowledged()) {
            throw new DataPersistenceException();
        }
    }

    @Override
    public List<DiaryEntryItem> fetchAll(String userId) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
        List<EncryptedDiaryEntryItem> allEncrypted = mongo.find(query, EncryptedDiaryEntryItem.class);
        return allEncrypted.stream()
                .map(this::decrypt)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String userId, long diaryTimestamp) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("diaryTimestamp").is(diaryTimestamp));
        DeleteResult deleteResult = mongo.remove(query, EncryptedDiaryEntryItem.class);
        if ((!deleteResult.wasAcknowledged()) || (deleteResult.getDeletedCount() == 0)) {
            throw new DataDeletionException();
        }
    }

    private Binary encrypt(BsonValue bsonValue, String algorithm) {
        Objects.requireNonNull(bsonValue);
        Objects.requireNonNull(algorithm);

        EncryptOptions options = new EncryptOptions(algorithm);
        options.keyId(encryptionConfig.getDataKeyId());

        BsonBinary encryptedValue = clientEncryption.encrypt(bsonValue, options);
        return new Binary(encryptedValue.getType(), encryptedValue.getData());
    }

    private Binary encrypt(String value, String algorithm) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(algorithm);

        return encrypt(new BsonString(value), algorithm);
    }

    private BsonValue decryptProperty(Binary value) {
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
}
