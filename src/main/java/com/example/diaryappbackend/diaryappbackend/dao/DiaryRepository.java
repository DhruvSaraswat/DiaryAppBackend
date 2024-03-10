package com.example.diaryappbackend.diaryappbackend.dao;

import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;

import java.util.List;

public interface DiaryRepository {

    void upsert(String userId, DiaryEntry diaryEntry);

    List<DiaryEntryItem> fetchAll(String userId);

    void delete(String userId, long diaryTimestamp);
}
