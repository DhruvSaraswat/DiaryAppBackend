package com.example.diaryappbackend.diaryappbackend.service;

import com.example.diaryappbackend.diaryappbackend.dao.DiaryEntryDao;
import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private final DiaryEntryDao diaryEntryDao;

    @Autowired
    public DiaryService(@Qualifier("fakeDao") DiaryEntryDao diaryEntryDao) {
        this.diaryEntryDao = diaryEntryDao;
    }

    public void saveDiaryEntry(DiaryEntry diaryEntry) {
        diaryEntryDao.upsert(diaryEntry);
    }
}
