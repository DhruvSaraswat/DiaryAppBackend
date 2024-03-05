package com.example.diaryappbackend.diaryappbackend.dao;

import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;

import java.util.List;

public interface DiaryEntryDao {
    List<DiaryEntry> fetchAll();

    void upsert(DiaryEntry diaryEntry);
}
