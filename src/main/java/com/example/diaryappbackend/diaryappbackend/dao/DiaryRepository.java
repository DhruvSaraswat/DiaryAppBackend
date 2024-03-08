package com.example.diaryappbackend.diaryappbackend.dao;

import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiaryRepository extends MongoRepository<DiaryEntryItem, String> {
    List<DiaryEntryItem> findByUserId(String userId);
}
