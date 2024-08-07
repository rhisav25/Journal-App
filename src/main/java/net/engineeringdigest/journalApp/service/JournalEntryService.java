package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository repository;
    @Autowired
    private UserService userService;

    @Transactional //if we give transactional property like this it will implement the entire code in one go
            //or none of the code. so it is like acid properties
    public void saveEntry(JournalEntry entry, String userName) {
        try{
            User user = userService.findByUsername(userName);
            JournalEntry savedEntry = repository.save(entry); //ami ai2 save hua journal entry tu lolu
            user.getJournalEntries().add(savedEntry);//xe2 user r in memory t save korilu
            userService.saveUser(user);
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
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

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try{
            User user = userService.findByUsername(userName);
            removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));//user tur pora delete kori save krilu karon
            //user t thoka journal entry nije nije delete nhi
            if(removed) {
                userService.saveUser(user);
                repository.deleteById(id);
            }

        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting journal entry "+ e);
        }
       return removed;
    }
}
