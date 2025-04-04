package silverpotion.postserver.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.comment.dtos.CommentListResDto;
import silverpotion.postserver.comment.repository.CommentLikeRepository;
import silverpotion.postserver.comment.repository.CommentRepository;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.repository.GatheringPeopleRepository;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostFile;
import silverpotion.postserver.post.domain.PostLike;
import silverpotion.postserver.post.domain.PostStatus;
import silverpotion.postserver.post.dtos.*;
import silverpotion.postserver.post.repository.PostFileRepository;
import silverpotion.postserver.post.repository.PostLikeRepository;
import silverpotion.postserver.post.repository.PostRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final S3Client s3Client;
    private final UserClient userClient;
    private final GatheringRepository gatheringRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final GatheringPeopleRepository gatheringPeopleRepository;
    private final ObjectMapper objectMapper;
//    private final NotificationService notificationService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public PostService(PostRepository postRepository, GatheringRepository gatheringRepository, PostFileRepository postFileRepository, S3Client s3Client, UserClient userClient, PostLikeRepository postLikeRepository, CommentRepository commentRepository, CommentLikeRepository commentLikeRepository, GatheringPeopleRepository gatheringPeopleRepository, ObjectMapper objectMapper) {
        this.postRepository = postRepository;
        this.gatheringRepository = gatheringRepository;
        this.postFileRepository = postFileRepository;
        this.s3Client = s3Client;
        this.userClient = userClient;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.gatheringPeopleRepository = gatheringPeopleRepository;
        this.objectMapper = objectMapper;
    }

    //    1. 게시물 생성시, 카테고리 유형 저장(임시저장)
    public Long createDraftPost(PostInitDto dto){
        Gathering gathering = gatheringRepository.findById(dto.getGatheringId()).orElseThrow(()-> new EntityNotFoundException("gathering is not found"));

        Post draftPost = Post.builder()
                .gathering(gathering)
                .postCategory(dto.getPostCategory())
                .postStatus(PostStatus.draft)
                .viewCount(0)
                .build();
        return postRepository.save(draftPost).getId();

    }

    //  2. 게시물 최종 저장(카테고리 분기)
    public Object updateFinalPost(Long postId, String loginId, PostUpdateDto dto){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));

        switch (post.getPostCategory()) {
            case free:
                FreePostUpdateDto freeDto = (FreePostUpdateDto) dto;
                saveCommonPost(post, userId, (FreePostUpdateDto) dto);
                return freeDto;

//            case notice:
//                NoticePostUpdateDto noticeDto = (NoticePostUpdateDto) dto;
//                saveCommonPost(post, userId, (NoticePostUpdateDto) noticeDto);
//                notificationService.sendToAllMembers(
//                        "새로운 공지사항", dto.getTitle(), "/post/" + post.getId()
//                );
//                return noticeDto;

            case vote:
                VotePostUpdateDto voteDto = (VotePostUpdateDto) dto;
                saveVotePost(post, userId, voteDto);
                return voteDto;
            default:
                throw new UnsupportedOperationException("지원하지 않는 게시물 유형");
        }

    }

    // 3. 공통 저장 로직 (자유글/공지글 공통)
    private void saveCommonPost(Post post, Long userId, FreePostUpdateDto dto) {
        post.update(dto.getTitle(), dto.getContent());
        post.changeStatus(PostStatus.fin);
        post.assignWriter(userId); // 작성자를 한번만 지정할 수 있도록 제약
        postRepository.save(post);

        for (MultipartFile file : dto.getPostImg()) {
            String fileUrl = uploadImage(file);
            postFileRepository.save(new PostFile(post, fileUrl));
        }
    }

    // 4. 투표 게시물 저장
    private void saveVotePost(Post post, Long userId, VotePostUpdateDto dto) {
        post.update(dto.getTitle(), dto.getDescription()); // 예시
        post.changeStatus(PostStatus.fin);
        post.assignWriter(userId); // 작성자를 한번만 지정할 수 있도록 제약
        postRepository.save(post);
        // 투표 항목 저장 등 로직 추가 필요
    }

    //  5. S3에 이미지저장
    public String uploadImage(MultipartFile file) {
        String fileName = "post/"+ UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    //    6. 게시물 삭제
    public void delete(Long postId,String loginId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Post post = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("게시물을 찾을 수 없습니다."));
        if (!userId.equals(post.getWriterId())){return;}
        postRepository.delete(post);
    }

    //    7. 게시물 조회
    @Transactional(readOnly = true)
    public Page<PostListResDto> getList(Integer page, Integer size, String loginId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        List<Long> gatheringUserIds = gatheringPeopleRepository.findMemberIdsInSameGatherings(userId);
        System.out.println("gatheringIds"+gatheringUserIds);

        List<Long> accessibleUserIds = new ArrayList<>(gatheringUserIds);
        accessibleUserIds.addAll(gatheringUserIds);
        accessibleUserIds.add(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime")); // 최신순 정렬

        System.out.println("accessibleUserIds : " + accessibleUserIds);
        // 작성자 프로필 정보 조회
        CommonDto profileInfoDtoMap = userClient.PostProfileInfo(accessibleUserIds);
        Object result = profileInfoDtoMap.getResult();

        if (result == null) {
            System.out.println("profileInfoDtoMap.getResult()가 null입니다.");
            result = List.of(); // 빈 리스트 처리
        }
        Map<Long,UserProfileInfoDto> profileList = objectMapper.convertValue(result, new TypeReference<Map<Long,UserProfileInfoDto>>() {});


        // 해당 유저들의 게시물만 조회
        return postRepository.findByWriterIdIn(new ArrayList<>(accessibleUserIds), pageable)
                .map(post -> {
                    Long likeCount = postRepository.countPostLikes(post.getId());
                    Long commentCount = postRepository.countPostComments(post.getId());
                    UserProfileInfoDto writerInfo = profileList.get(post.getWriterId());

                    boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), userId);
                    String isLike = isLiked ? "Y" : "N";

                    return PostListResDto.fromEntity(post, likeCount, commentCount, isLike, writerInfo);
                });
    }

//    게시물 상세조회
    @Transactional(readOnly = true)
    public PostDetailResDto getDetail(Long postId,String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        // 게시물 조회 (없으면 예외 발생)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다."));

        Long writerId = post.getWriterId(); // 작성자 프로필을 가져오기 위해
        UserProfileInfoDto writerProfile = userClient.getUserProfileInfo(writerId);

        // 게시물 좋아요 개수 조회
        Long postLikeCount = postRepository.countPostLikes(postId);

        // 댓글 목록 조회
        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentListResDto> commentList = comments.stream()
                .map(comment -> {
                    Long commentLikeCount = commentRepository.countCommentLikes(comment.getId());
                    boolean isCommentLiked = commentLikeRepository.existsByCommentIdAndUserId(comment.getId(), userId);
                    String isCommentLike = isCommentLiked ? "Y" : "N";
                    return CommentListResDto.fromEntity(comment, commentLikeCount, isCommentLike, writerProfile);
                })
                .collect(Collectors.toList());

        // 사용자의 좋아요 여부 확인
        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId,userId);
        String isLike = isLiked ? "Y" : "N";

        // DTO 변환 후 반환
        return PostDetailResDto.fromEntity(post,writerProfile,postLikeCount, commentList, isLike);
    }

}
