package disinkt.postservice.repositories;

import disinkt.postservice.entities.Post;
import disinkt.postservice.entities.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Reaction findOneByPostAndUserId(Post post, Long userId);
    List<Reaction> findAllByUserId(Long userId);
}
