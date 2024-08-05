package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUsername(userName);
        List<JournalEntry>all = user.getJournalEntries(); //we have defined journal entries inside user
        if(!all.isEmpty()&&all!=null){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry,@PathVariable String userName) {
        try{
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry,userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry =  journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{userName}/{myId}")
    public boolean deleteJournalEntry(@PathVariable ObjectId myId, @PathVariable String userName) {
         journalEntryService.deleteById(myId,userName);
         return true;
    }

    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId myId ,
                                                           @PathVariable String userName,
                                                           @RequestBody JournalEntry newJournalEntry)
    {
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
            journalEntryService.saveEntry(oldJournalEntry);
            return new ResponseEntity<>(oldJournalEntry, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

