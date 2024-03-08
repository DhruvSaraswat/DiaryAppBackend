package com.example.diaryappbackend.diaryappbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiaryEntry {
    private final String title;
    private final String story;
    private final long createdAtTimestamp;
    private final long lastEditedAtTimestamp;

    public DiaryEntry(@JsonProperty(value = "title", required = true) String title,
                      @JsonProperty(value = "story", required = true) String story,
                      @JsonProperty(value = "createdAtTimestamp", required = true) long createdAtTimestamp,
                      @JsonProperty(value = "lastEditedAtTimestamp", required = true) long lastEditedAtTimestamp) {
        this.title = title;
        this.story = story;
        this.createdAtTimestamp = createdAtTimestamp;
        this.lastEditedAtTimestamp = lastEditedAtTimestamp;
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
