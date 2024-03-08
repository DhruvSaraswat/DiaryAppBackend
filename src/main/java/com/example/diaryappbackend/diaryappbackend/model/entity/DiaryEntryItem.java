package com.example.diaryappbackend.diaryappbackend.model.entity;

public class DiaryEntryItem {

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
}
