package silverpotion.postserver.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.comment.dtos.CommentListResDto;
import silverpotion.postserver.comment.repository.CommentLikeRepository;
import silverpotion.postserver.comment.repository.CommentRepository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.repository.GatheringPeopleRepository;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.domain.*;
import silverpotion.postserver.post.dtos.*;
import silverpotion.postserver.post.repository.*;
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
    private final VoteRepository voteRepository;
    private final PostQueryRepository postQueryRepository;
//    private final NotificationService notificationService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public PostService(PostRepository postRepository, GatheringRepository gatheringRepository, PostFileRepository postFileRepository, S3Client s3Client, UserClient userClient, PostLikeRepository postLikeRepository, CommentRepository commentRepository, CommentLikeRepository commentLikeRepository, GatheringPeopleRepository gatheringPeopleRepository, ObjectMapper objectMapper, VoteRepository voteRepository, PostQueryRepository postQueryRepository) {
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
        this.voteRepository = voteRepository;
        this.postQueryRepository = postQueryRepository;
    }

    //    1. 게시물 생성시, 카테고리 유형 저장(임시저장)
    public Long createDraftPost(PostInitDto dto){
        Gathering gathering = gatheringRepository.findById(dto.getGatheringId()).orElseThrow(()-> new EntityNotFoundException("gathering is not found"));

        if (dto.getPostCategory() == PostCategory.free){
            Post draftPost = Post.builder()
                    .gathering(gathering)
                    .postCategory(dto.getPostCategory())
                    .postStatus(PostStatus.draft)
                    .viewCount(0)
                    .build();
            return postRepository.save(draftPost).getId();
        }
        else if (dto.getPostCategory() == PostCategory.notice) {
            Post draftPost = Post.builder()
                    .gathering(gathering)
                    .postCategory(dto.getPostCategory())
                    .postStatus(PostStatus.draft)
                    .viewCount(0)
                    .build();
            return postRepository.save(draftPost).getId();

        } else if (dto.getPostCategory() == PostCategory.vote) {
            Vote draftVote = Vote.builder()
                    .gathering(gathering)
                    .postCategory(dto.getPostCategory())
                    .postStatus(PostStatus.draft)
                    .build();
            return voteRepository.save(draftVote).getVoteId();
        }
        else {
            throw new EntityNotFoundException("post is not found");
        }
    }

    //  2. 게시물 최종 저장(카테고리 분기)
    public Object updateFinalPost(Long postId, String loginId, PostUpdateDto dto){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));

        switch (post.getPostCategory()) {
            case free:
                FreePostUpdateDto freeDto = (FreePostUpdateDto) dto;
                saveFreePost(post, userId, (FreePostUpdateDto) dto);
                return freeDto;

            case notice:
                NoticePostUpdateDto noticeDto = (NoticePostUpdateDto) dto;
                saveNoticePost(post, userId, noticeDto);
                return noticeDto;

            default:
                throw new UnsupportedOperationException("지원하지 않는 게시물 유형");
        }

    }

//    투표 저장
    public VotePostUpdateDto saveVote(Long voteId, String loginId, VotePostUpdateDto dto){
        Vote vote = voteRepository.findVoteByVoteId(voteId).orElseThrow(()->new EntityNotFoundException("투표가 없습니다."));
        Long userId = userClient.getUserIdByLoginId(loginId);
        saveVotePost(vote,userId,dto);
        return dto;
    }

    // 3. 자유글 저장
    private void saveFreePost(Post post, Long userId, FreePostUpdateDto dto) {
        post.update(dto.getTitle(), dto.getContent());
        post.changeStatus(PostStatus.fin);
        post.assignWriter(userId); // 작성자를 한번만 지정할 수 있도록 제약
        postRepository.save(post);

        for (MultipartFile file : dto.getPostImg()) {
            String fileUrl = uploadImage(file);
            postFileRepository.save(new PostFile(post, fileUrl));
        }
    }

//    공지글 저장
    private void saveNoticePost(Post post, Long userId, NoticePostUpdateDto dto) {
        post.update(dto.getTitle(), dto.getContent());
        post.changeStatus(PostStatus.fin);
        post.assignWriter(userId); // 작성자를 한번만 지정할 수 있도록 제약
        postRepository.save(post);

        if (dto.getPostImg() != null) {
            for (MultipartFile file : dto.getPostImg()) {
                String fileUrl = uploadImage(file);
                postFileRepository.save(new PostFile(post, fileUrl));
            }
        }
    }

    // 4. 투표 게시물 저장
    private void saveVotePost(Vote vote, Long userId, VotePostUpdateDto dto) {
        vote.update(userId,dto); // 예시
        vote.changeStatus(PostStatus.fin);
        voteRepository.save(vote);
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

//    //    7. 게시물 조회
    public Page<PostVoteResDTO> getPostAndVoteList(int page, int size) {
        int safePage = (page <= 0) ? 0 : page;
        int offset = (safePage == 0) ? 0 : (safePage - 1) * size;
        List<PostVoteUnionDto> rawList = postQueryRepository.findAllPostAndVote(size, offset);
        System.out.println("rawList" + rawList);
        long totalCount = postQueryRepository.countPostAndVote();
        System.out.println("totalCount" + totalCount);

        List<PostVoteResDTO> dtoList = rawList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        System.out.println("dtoList"+dtoList);

        Pageable pageable = PageRequest.of(safePage, size);
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private PostVoteResDTO convertToDto(PostVoteUnionDto dto) {
        List<String> voteOptions = null;

        if (dto.getVoteOptions() instanceof List) {
            voteOptions = dto.getVoteOptions();
        }

        return PostVoteResDTO.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .type(dto.getType())
                .viewCount(dto.getViewCount())
                .multipleChoice(dto.getMultiChoice())
                .voteOptions(voteOptions)
                .build();
    }

//    자유글 조회
    public Page<PostListResDto> getFreeList(int page, int size, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);
        List<Long> gatheringUserIds = gatheringPeopleRepository.findMemberIdsInSameGatherings(userId); // 같은 모임 id리스트
        System.out.println("gatheringIds"+gatheringUserIds);

        List<Long> accessibleUserIds = new ArrayList<>(gatheringUserIds);//본인 포함 모임 id리스트
        accessibleUserIds.addAll(gatheringUserIds);
        accessibleUserIds.add(userId);
        System.out.println("accessibleUserIds : " + accessibleUserIds);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));

        // 작성자 프로필 정보 조회
        CommonDto profileInfoDtoMap = userClient.PostProfileInfo(accessibleUserIds);
        Object result = profileInfoDtoMap.getResult();

        if (result == null) {
            System.out.println("profileInfoDtoMap.getResult()가 null입니다.");
            result = List.of(); // 빈 리스트 처리
        }
        Map<Long,UserProfileInfoDto> profileList = objectMapper.convertValue(result, new TypeReference<Map<Long,UserProfileInfoDto>>() {});

//        해당 유저들의 자유글만 조회(페이징)
        Page<Post> freeList = postRepository.findByWriterIdInAndPostCategoryAndDelYnAndPostStatus(gatheringUserIds,PostCategory.free,
                DelYN.N,PostStatus.fin,pageable);
        return freeList.map(post ->
            {
                Long likeCount = postRepository.countPostLikes(post.getId());
                Long commentCount = postRepository.countPostComments(post.getId());
                UserProfileInfoDto writerInfo = profileList.get(post.getWriterId());
                boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), userId);
                String isLike = isLiked ? "Y" : "N";

                return PostListResDto.fromEntity(post,likeCount,commentCount,isLike,writerInfo);
            });
    }

    // 공지글 조회
    public Page<PostListResDto> getNoticeList(int page, int size, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);
        List<Long> gatheringUserIds = gatheringPeopleRepository.findMemberIdsInSameGatherings(userId); // 같은 모임 id리스트
        System.out.println("gatheringIds"+gatheringUserIds);

        List<Long> accessibleUserIds = new ArrayList<>(gatheringUserIds);//본인 포함 모임 id리스트
        accessibleUserIds.add(userId);
        System.out.println("accessibleUserIds : " + accessibleUserIds);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));

        // 작성자 프로필 정보 조회
        CommonDto profileInfoDtoMap = userClient.PostProfileInfo(accessibleUserIds);
        Object result = profileInfoDtoMap.getResult();

        if (result == null) {
            System.out.println("profileInfoDtoMap.getResult()가 null입니다.");
            result = List.of(); // 빈 리스트 처리
        }
        Map<Long,UserProfileInfoDto> profileList = objectMapper.convertValue(result, new TypeReference<Map<Long,UserProfileInfoDto>>() {});

//        해당 유저들의 공지글만 조회(페이징)
        Page<Post> noticeList = postRepository.findByWriterIdInAndPostCategoryAndDelYnAndPostStatus(accessibleUserIds,PostCategory.notice,
                DelYN.N,PostStatus.fin,pageable);
        return noticeList.map(post ->
        {
            Long likeCount = postRepository.countPostLikes(post.getId());
            Long commentCount = postRepository.countPostComments(post.getId());
            UserProfileInfoDto writerInfo = profileList.get(post.getWriterId());
            boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), userId);
            String isLike = isLiked ? "Y" : "N";

            return PostListResDto.fromEntity(post,likeCount,commentCount,isLike,writerInfo);
        });
    }

//    투표조회
    public Page<VoteResListDto> getVoteList(int page, int size, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        List<Long> gatheringUserIds = gatheringPeopleRepository.findMemberIdsInSameGatherings(userId); // 같은 모임 id리스트
        System.out.println("gatheringIds"+gatheringUserIds);

        List<Long> accessibleUserIds = new ArrayList<>(gatheringUserIds);//본인 포함 모임 id리스트
        accessibleUserIds.add(userId);
        System.out.println("accessibleUserIds : " + accessibleUserIds);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));

        // 작성자 프로필 정보 조회
        CommonDto profileInfoDtoMap = userClient.PostProfileInfo(accessibleUserIds);
        Object result = profileInfoDtoMap.getResult();

        if (result == null) {
            System.out.println("profileInfoDtoMap.getResult()가 null입니다.");
            result = List.of(); // 빈 리스트 처리
        }
        Map<Long,UserProfileInfoDto> profileList = objectMapper.convertValue(result, new TypeReference<Map<Long,UserProfileInfoDto>>() {});

        //        해당 유저들의 공지글만 조회(페이징)
        Page<Vote> voteList = voteRepository.findByWriterIdInAndPostCategoryAndDelYnAndPostStatus(accessibleUserIds,PostCategory.vote,
                DelYN.N,PostStatus.fin,pageable);
        return voteList.map(vote ->
        {
            UserProfileInfoDto writerInfo = profileList.get(vote.getWriterId());

            return VoteResListDto.fromEntity(vote,writerInfo);
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
