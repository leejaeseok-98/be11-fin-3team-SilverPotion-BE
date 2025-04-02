package silverpotion.postserver.comment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.comment.domain.CommentLike;
import silverpotion.postserver.comment.dtos.*;
import silverpotion.postserver.comment.repository.CommentLikeRepository;
import silverpotion.postserver.comment.repository.CommentRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.dtos.UserListDto;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;
import silverpotion.postserver.post.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static silverpotion.postserver.common.config.RabbitMQConfig.BACKUP_QUEUE_COMMENT_AL;
import static silverpotion.postserver.common.config.RabbitMQConfig.BACKUP_QUEUE_COMMENT_ML;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;
    private final CommentLikeRepository commentLikeRepository;
    private final RabbitTemplate rabbitTemplate;
    @Qualifier("commentLikeRedisTemplate")
    private final RedisTemplate<String, Object> commentLikeRedisTemplate;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserClient userClient, CommentLikeRepository commentLikeRepository, RabbitTemplate rabbitTemplate, @Qualifier("commentLikeRedisTemplate") RedisTemplate<String, Object> commentLikeRedisTemplate) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userClient = userClient;
        this.commentLikeRepository = commentLikeRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.commentLikeRedisTemplate = commentLikeRedisTemplate;
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

    public Long commentUpdate(String loginId, CommentUpdateDto commentUpdateDto) {
        Long userId = userClient.getUserIdByLoginId(loginId);
        Comment comment = commentRepository.findById(commentUpdateDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("댓글이 없습니다."));

        if (!(userId.equals(comment.getUserId()))) {
            return 0L;
        }
        comment.updateContent(commentUpdateDto.getContent());

        commentRepository.save(comment);
        return comment.getPost().getId();
    }

    public Long commentDelete(String loginId,Long commentId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("댓글이 없습니다."));
        Long commentWriterId = comment.getUserId();

        if (!(userId.equals(commentWriterId))){return 0L;}
        comment.delete();
        commentRepository.save(comment);
        return comment.getId();
    }

    public Long replyCreate(String loginId,ReplyCommentCreateReqDto dto){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Comment parent = commentRepository.findById(dto.getParentId()).orElseThrow(()->new EntityNotFoundException("댓글이 없다"));
        Post post = commentRepository.findPostByParentId(parent.getId());

        Comment comment = Comment.builder()
                .userId(userId)
                .post(post)
                .parent(parent)
                .content(dto.getContent())
                .build();
        commentRepository.save(comment);

        parent.addChild(comment);
        return post.getId();

    }


    public CommentLikeResDto commentLikeToggle(Long commentId,String loginId){
        String redisKey = "comment:like" + commentId;

        Object cachedValue = commentLikeRedisTemplate.opsForValue().get(redisKey);
        Long likeCount = cachedValue != null ? ((Number) cachedValue).longValue() : null;

        Long userId = userClient.getUserIdByLoginId(loginId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("댓글이 없습니다."));

        if (likeCount==null){
            likeCount = commentLikeRepository.countByComment(comment);
            commentLikeRedisTemplate.opsForValue().set(redisKey,likeCount,10, TimeUnit.MINUTES);
        }

        Optional<CommentLike> commentLikeOptional = commentLikeRepository.findByCommentAndUserId(comment,userId);
        boolean isLike;

        if (commentLikeOptional.isPresent()){
            commentLikeRepository.delete(commentLikeOptional.get());
            isLike = false;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_ML,commentId);
            likeCount--;
        }
        else {
            CommentLike newLike = CommentLike.builder()
                    .comment(comment)
                    .userId(userId)
                    .build();
            commentLikeRepository.save(newLike);
            isLike = true;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_COMMENT_AL,commentId);
            likeCount++;
        }
        commentLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);

        return new CommentLikeResDto(likeCount, isLike);
    }


}
