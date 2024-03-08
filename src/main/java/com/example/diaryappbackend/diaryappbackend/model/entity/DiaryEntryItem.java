package com.example.diaryappbackend.diaryappbackend.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DiaryEntries")
public class DiaryEntryItem {

    @Id
    private final String userId;
    private final String title;
    private final String story;
    private final long createdAtTimestamp;
    private final long lastEditedAtTimestamp;

    public DiaryEntryItem(String userId, String title, String story, long createdAtTimestamp, long lastEditedAtTimestamp) {
        this.userId = userId;
        this.title = title;
        this.story = story;
        this.createdAtTimestamp = createdAtTimestamp;
        this.lastEditedAtTimestamp = lastEditedAtTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getStory() {
        return story;
    }

    public long getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public long getLastEditedAtTimestamp() {
        return lastEditedAtTimestamp;
    }
}
