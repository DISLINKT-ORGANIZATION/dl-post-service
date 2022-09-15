package disinkt.postservice.service;

import disinkt.postservice.repositories.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {

    private ReactionRepository repository;

    @Autowired
    public ReactionService(ReactionRepository repository) {
        this.repository = repository;
    }
}
