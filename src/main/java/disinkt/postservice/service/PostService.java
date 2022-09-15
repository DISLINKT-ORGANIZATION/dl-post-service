package disinkt.postservice.service;

import disinkt.postservice.dtos.PostDto;
import disinkt.postservice.dtos.UserIds;
import disinkt.postservice.entities.Post;
import disinkt.postservice.mappers.PostDtoMapper;
import disinkt.postservice.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final PostDtoMapper mapper;

    @Autowired
    public PostService(PostRepository repository, PostDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PostDto> retrievePosts(UserIds userIds) {
        List<Post> posts = repository.findAllByUserIdIn(userIds.getIds());
        return mapper.toCollectionDto(posts);
    }
}
