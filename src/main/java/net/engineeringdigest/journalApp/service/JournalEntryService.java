package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository repository;
    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry entry, String userName) {
        User user = userService.findByUsername(userName);
        JournalEntry savedEntry = repository.save(entry); //ami ai2 save hua journal entry tu lolu
        user.getJournalEntries().add(savedEntry);  //xe2 user r in memory t save korilu
        userService.saveEntry(user);  // and xe2 db t save krlu
    }

    public void saveEntry(JournalEntry entry) {
        repository.save(entry);
    }

    public List<JournalEntry> getAll(){
        return repository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return repository.findById(id);
    }

    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUsername(userName);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));//user tur pora delete kori save krilu karon
        //user t thoka journal entry nije nije delete nhi
        repository.deleteById(id);
    }

}
