package silverpotion.postserver.comment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.comment.dtos.CommentCreateDto;
import silverpotion.postserver.comment.repository.CommentRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;
import silverpotion.postserver.post.repository.PostRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserClient userClient) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userClient = userClient;
    }

    public Long commentCreate(String loginId, CommentCreateDto commentCreateDto){
        UserProfileInfoDto userProfileInfoDto = userClient.getUserProfileInfo(loginId);
        Post post = postRepository.findById(commentCreateDto.getPostId()).orElseThrow(()->new EntityNotFoundException("게시물이 없습니다."));
        Comment comment = Comment.builder()
                .userId(userProfileInfoDto.getUserId())
                .post(post)
                .content(commentCreateDto.getContent())
                .build();

        commentRepository.save(comment);

        return commentCreateDto.getPostId();
    }
}
