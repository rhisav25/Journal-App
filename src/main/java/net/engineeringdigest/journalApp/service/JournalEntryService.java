package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
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

    public void saveEntry(JournalEntry entry) {
        repository.save(entry);
    }

    public List<JournalEntry> getAll(){
        return repository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return repository.findById(id);
    }

    public void deleteById(ObjectId id) {
        repository.deleteById(id);
    }

}
