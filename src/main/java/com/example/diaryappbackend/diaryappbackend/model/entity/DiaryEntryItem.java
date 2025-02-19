package com.example.diaryappbackend.diaryappbackend.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("DiaryEntries")
public class DiaryEntryItem {

    private String userId;
    private String title;
    private String story;
    private long diaryTimestamp;
    private long createdAtTimestamp;
    private long lastEditedAtTimestamp;

    /**public DiaryEntryItem(String userId, String title, String story, long diaryTimestamp, long createdAtTimestamp, long lastEditedAtTimestamp) {
        this.userId = userId;
        this.title = title;
        this.story = story;
        this.diaryTimestamp = diaryTimestamp;
        this.createdAtTimestamp = createdAtTimestamp;
        this.lastEditedAtTimestamp = lastEditedAtTimestamp;
    }*/

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
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
