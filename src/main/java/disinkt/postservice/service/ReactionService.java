package disinkt.postservice.service;

import disinkt.postservice.entities.Post;
import disinkt.postservice.entities.Reaction;
import disinkt.postservice.repositories.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {

    private final ReactionRepository repository;

    @Autowired
    public ReactionService(ReactionRepository repository) {
        this.repository = repository;
    }

    public Reaction findReaction(Post post, Long userId) {
        return this.repository.findOneByPostAndUserId(post, userId);
    }

    public List<Reaction> findReactionByUserId(Long userId) {
        return this.repository.findAllByUserId(userId);
    }

    public void deleteReaction(Reaction reaction) {
        this.repository.delete(reaction);
    }

    public Reaction saveReaction(Reaction reaction) {
        return this.repository.save(reaction);
    }
}
