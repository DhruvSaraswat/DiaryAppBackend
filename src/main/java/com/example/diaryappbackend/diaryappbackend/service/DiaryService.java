package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.dao.DiaryRepository;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository repository;

    public void save(String userId, DiaryEntry diaryEntry) {
        repository.upsert(userId, diaryEntry);
    }

    public List<DiaryEntryItem> findAll(String userId) {
        return repository.fetchAll(userId);
    }

    public void deleteDiaryEntry(String userId, long diaryTimestamp) {
        repository.delete(userId, diaryTimestamp);
    }
}
