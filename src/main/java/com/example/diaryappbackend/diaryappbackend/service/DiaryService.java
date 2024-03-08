package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.dao.DiaryRepository;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    private final DiaryRepository repository;

    @Autowired
    public DiaryService(DiaryRepository repository) {
        this.repository = repository;
    }

    public void saveDiaryEntry(DiaryEntry diaryEntry, String userId) {
        DiaryEntryItem item = new DiaryEntryItem(userId,
                diaryEntry.getTitle(),
                diaryEntry.getStory(),
                diaryEntry.getCreatedAtTimestamp(),
                diaryEntry.getLastEditedAtTimestamp());
        repository.save(item);
    }

    public List<DiaryEntry> fetchALlDiaryEntries(String userId) {
        List<DiaryEntryItem> fetchedItems = repository.findByUserId(userId);
        return fetchedItems.stream().map(fetchedItem -> new DiaryEntry(fetchedItem.getTitle(),
                fetchedItem.getStory(),
                fetchedItem.getCreatedAtTimestamp(),
                fetchedItem.getLastEditedAtTimestamp())).toList();
    }
}
