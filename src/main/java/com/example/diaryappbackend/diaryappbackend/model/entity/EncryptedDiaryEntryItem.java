package com.example.diaryappbackend.diaryappbackend.model.entity;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DiaryEntries")
public class EncryptedDiaryEntryItem {

    private String userId;
    private Binary title;
    private Binary story;
    private long diaryTimestamp;
    private long createdAtTimestamp;
    private long lastEditedAtTimestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Binary getTitle() {
        return title;
    }

    public void setTitle(Binary title) {
        this.title = title;
    }

    public Binary getStory() {
        return story;
    }

    public void setStory(Binary story) {
        this.story = story;
    }

    public long getDiaryTimestamp() {
        return diaryTimestamp;
    }

    public void setDiaryTimestamp(long diaryTimestamp) {
        this.diaryTimestamp = diaryTimestamp;
    }

    public long getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(long createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public long getLastEditedAtTimestamp() {
        return lastEditedAtTimestamp;
    }

    public void setLastEditedAtTimestamp(long lastEditedAtTimestamp) {
        this.lastEditedAtTimestamp = lastEditedAtTimestamp;
    }
}
