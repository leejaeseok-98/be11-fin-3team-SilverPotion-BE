package silverpotion.postserver.post.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.postserver.common.config.RabbitMQConfig;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostLike;
import silverpotion.postserver.post.dtos.PostLikeResDto;
import silverpotion.postserver.post.repository.PostLikeRepository;
import silverpotion.postserver.post.repository.PostRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static silverpotion.postserver.common.config.RabbitMQConfig.BACKUP_QUEUE_ML;
import static silverpotion.postserver.common.config.RabbitMQConfig.BACKUP_QUEUE_AL;


@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;
    private final RabbitTemplate rabbitTemplate;

    @Qualifier("postLikeRedisTemple")
    private final RedisTemplate<String,Object> postLikeRedisTemplate;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository, UserClient userClient,RabbitTemplate rabbitTemplate,@Qualifier("postLikeRedisTemple") RedisTemplate<String, Object> postLikeRedisTemplate) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userClient = userClient;
        this.rabbitTemplate = rabbitTemplate;
        this.postLikeRedisTemplate = postLikeRedisTemplate;
    }

    @Transactional
    public PostLikeResDto togglePostLike(Long postId,String loginId){
        //레디스 좋아요 키
        String redisKey = "post:like:" + postId;

//        캐싱값
        Object cachedValue = postLikeRedisTemplate.opsForValue().get(redisKey);
        Long likeCount = cachedValue != null ? ((Number) cachedValue).longValue() : null;

        Long userId = userClient.getUserIdByLoginId(loginId);
        Post post = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("해당 게시물이 존재하지 않습니다."));

        if (likeCount == null) {
            likeCount = postLikeRepository.countByPost(post);
            postLikeRedisTemplate.opsForValue().set(redisKey, likeCount, 10, TimeUnit.MINUTES);
        }

        Optional<PostLike> postLikeOpt = postLikeRepository.findByPostAndUserId(post,userId);
        boolean isLike;

        if (postLikeOpt.isPresent()){
            postLikeRepository.delete(postLikeOpt.get());
            isLike = false;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_ML, postId);
            likeCount--;
        } else {
            PostLike newLike = PostLike.builder()
                    .post(post)
                    .userId(userId)
                    .build();
            postLikeRepository.save(newLike);
            isLike = true;
            rabbitTemplate.convertAndSend(BACKUP_QUEUE_AL, postId);
            likeCount++;
        }
        postLikeRedisTemplate.opsForValue().set(redisKey, likeCount,10, TimeUnit.MINUTES);

        return new PostLikeResDto(likeCount,isLike);
    }
}
