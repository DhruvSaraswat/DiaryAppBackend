package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.dao.DiaryEntryDao;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    private final DiaryEntryDao diaryEntryDao;

    @Autowired
    public DiaryService(DiaryEntryDao diaryEntryDao) {
        this.diaryEntryDao = diaryEntryDao;
    }

    public void saveDiaryEntry(DiaryEntry diaryEntry, String userId) {
        DiaryEntryItem item = new DiaryEntryItem(userId,
                diaryEntry.getTitle(),
                diaryEntry.getStory(),
                diaryEntry.getCreatedAtTimestamp(),
                diaryEntry.getLastEditedAtTimestamp());
        diaryEntryDao.save(item);
    }

    public List<DiaryEntry> fetchALlDiaryEntries(String userId) {
        List<DiaryEntryItem> fetchedItems = diaryEntryDao.findByUserId(userId);
        return fetchedItems.stream().map(fetchedItem -> new DiaryEntry(fetchedItem.getTitle(),
                fetchedItem.getStory(),
                fetchedItem.getCreatedAtTimestamp(),
                fetchedItem.getLastEditedAtTimestamp())).toList();
    }
}
