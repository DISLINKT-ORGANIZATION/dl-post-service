package disinkt.postservice.service;

import disinkt.postservice.entities.Comment;
import disinkt.postservice.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository repository;

    @Autowired
    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public Comment saveComment(Comment comment) {
        return this.repository.save(comment);
    }

}
