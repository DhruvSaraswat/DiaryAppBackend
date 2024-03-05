package com.example.diaryappbackend.diaryappbackend.dao;

import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("fakeDao")
public class FakeDiaryDataAccessService implements DiaryEntryDao {
    private static List<DiaryEntry> db = new ArrayList<>();
    @Override
    public List<DiaryEntry> fetchAll() {
        return db;
    }

    @Override
    public void upsert(DiaryEntry diaryEntry) {
        db.add(diaryEntry);
    }
}
