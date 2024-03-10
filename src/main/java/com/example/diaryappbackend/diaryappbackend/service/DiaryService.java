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
                diaryEntry.getDiaryTimestamp(),
                diaryEntry.getCreatedAtTimestamp(),
                diaryEntry.getLastEditedAtTimestamp());
        repository.save(item);
    }

    public List<DiaryEntry> fetchALlDiaryEntries(String userId) {
        List<DiaryEntryItem> fetchedItems = repository.findByUserId(userId);
        return fetchedItems.stream().map(fetchedItem -> new DiaryEntry(fetchedItem.getTitle(),
                fetchedItem.getStory(),
                fetchedItem.getDiaryTimestamp(),
                fetchedItem.getCreatedAtTimestamp(),
                fetchedItem.getLastEditedAtTimestamp())).toList();
    }

    public DiaryEntry deleteDiaryEntry(String userId, long createdAt) {
        DiaryEntryItem item = repository.deleteByUserIdAndCreatedAtTimestamp(userId, createdAt);
        if (item == null) {
            return null;
        }
        return new DiaryEntry(item.getTitle(), item.getStory(), item.getDiaryTimestamp(), item.getCreatedAtTimestamp(), item.getLastEditedAtTimestamp());
    }
}
