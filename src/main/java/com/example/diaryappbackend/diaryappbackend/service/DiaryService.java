package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.dao.DiaryRepository;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository repository;

    public void save(String userId, DiaryEntry diaryEntry) {
        repository.upsert(userId, diaryEntry);
    }

    public List<DiaryEntry> findAll(String userId) {
        List<DiaryEntryItem> diaryEntryItems = repository.fetchAll(userId);
        return diaryEntryItems.stream()
                .map(diaryEntryItem -> new DiaryEntry(diaryEntryItem.getTitle(),
                        diaryEntryItem.getStory(),
                        diaryEntryItem.getDiaryTimestamp(),
                        diaryEntryItem.getCreatedAtTimestamp(),
                        diaryEntryItem.getLastEditedAtTimestamp()))
                .collect(Collectors.toList());

    }

    public void deleteDiaryEntry(String userId, long diaryTimestamp) {
        repository.delete(userId, diaryTimestamp);
    }
}
