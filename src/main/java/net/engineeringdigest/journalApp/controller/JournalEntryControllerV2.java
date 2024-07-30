package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createJournalEntry(@RequestBody JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return journalEntry;
    }

    @GetMapping("/id/{myId}")
    public JournalEntry getJournalEntry(@PathVariable ObjectId myId) {
        return journalEntryService.findById(myId).orElse(null);
    }

    @DeleteMapping("/id/{myId}")
    public boolean deleteJournalEntry(@PathVariable ObjectId myId) {
         journalEntryService.deleteById(myId);
         return true;
    }

    @PutMapping("/id/{myId}")
    public JournalEntry updateJournalEntry(@PathVariable ObjectId myId , @RequestBody JournalEntry newJournalEntry) {
        JournalEntry oldJournalEntry = journalEntryService.findById(myId).orElse(null);
        if(oldJournalEntry != null) {
            if(newJournalEntry.getTitle()!= null&& !newJournalEntry.getTitle().equals("")) {
                oldJournalEntry.setTitle(newJournalEntry.getTitle());
            }else{
                oldJournalEntry.setTitle(oldJournalEntry.getTitle());
            }

            if(newJournalEntry.getContent()!= null && !newJournalEntry.getContent().equals("")) {
                oldJournalEntry.setContent(newJournalEntry.getContent());
            }else{
                oldJournalEntry.setContent(oldJournalEntry.getContent());
            }
        }
        journalEntryService.saveEntry(oldJournalEntry);
        return oldJournalEntry;
    }


}

