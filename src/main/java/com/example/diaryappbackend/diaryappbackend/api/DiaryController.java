package com.example.diaryappbackend.diaryappbackend.api;

import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/diary")
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public void saveDiaryEntry(@RequestBody DiaryEntry diaryEntry) {
        diaryService.saveDiaryEntry(diaryEntry);
    }
}
