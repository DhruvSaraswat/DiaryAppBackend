package com.example.diaryappbackend.diaryappbackend.api;

import com.example.diaryappbackend.diaryappbackend.model.DiaryEntry;
import com.example.diaryappbackend.diaryappbackend.model.entity.DiaryEntryItem;
import com.example.diaryappbackend.diaryappbackend.model.error.NoRecordFoundException;
import com.example.diaryappbackend.diaryappbackend.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/diary")
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public void saveDiaryEntry(@RequestBody DiaryEntry diaryEntry, @RequestHeader("userId") String userId) {
        diaryService.save(diaryEntry, userId);
    }

    @GetMapping
    public List<DiaryEntryItem> fetchAllDiaryEntries(@RequestHeader("userId") String userId) {
        return diaryService.findAll(userId);
    }

    @DeleteMapping
    public void deleteDiaryEntry(@RequestHeader("userId") String userId, @RequestParam(name="diaryTimestamp") long diaryTimestamp) {
        diaryService.deleteDiaryEntry(userId, diaryTimestamp);
        /*if (diaryEntry == null) {
            throw new NoRecordFoundException();
        }
        return new ResponseEntity<>(diaryEntry, HttpStatus.OK);*/
    }
}
