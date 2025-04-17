package silverpotion.postserver.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.post.domain.PostCategory;
import silverpotion.postserver.post.dtos.*;
import silverpotion.postserver.post.repository.PostLikeRepository;
import silverpotion.postserver.post.repository.VoteRepository;
import silverpotion.postserver.post.service.PostLikeService;
import silverpotion.postserver.post.service.PostService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("silverpotion/post")
public class PostController {
    private final PostService postService;
    private final PostLikeService PostLikeService;
    private final VoteRepository voteRepository;

    public PostController(PostService postService, silverpotion.postserver.post.service.PostLikeService postLikeService, VoteRepository voteRepository) {
        this.postService = postService;
        PostLikeService = postLikeService;
        this.voteRepository = voteRepository;
    }

    //    1. 게시물 생성 시, 게시물 유형 페이지 조회
    @GetMapping("/init")
    public ResponseEntity<?> initGet() {
        List<PostCategoryDto> categories = new ArrayList<>();
        for (PostCategory c : PostCategory.values()) {
            categories.add(new PostCategoryDto(c.name(), c.getLabel()));
        }
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 카테고리 유형 조회 완료", categories), HttpStatus.OK);
    }

    //    2. 게시물 작성시, 게시물 유형 먼저 저장(임시 저장)
    @PostMapping("/init")
    public ResponseEntity<?> initPost(@RequestBody PostInitDto dto) {
        Long postId = postService.createDraftPost(dto); // postId를 받는 이유는 제목,글 저장할 때 post를 바로 찾아가서 저장할 수 있도록!
        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("category", dto.getPostCategory()); //게시물 유형마다 페이지가 다르니, 카테고리 데이터도 같이 넘김
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "임시 저장완료", response), HttpStatus.OK);
    }

    //    3. 자유글, 공지사항 게시물 작성시, 제목/이미지/내용 저장(최종 저장)
    @PutMapping("/update/free/{postId}") // 임시저장 때, postId가 나와서 쉽게 조회 후 저장
    public ResponseEntity<?> freeSave(@PathVariable Long postId, @RequestHeader("X-User-LoginId") String loginId
            , @ModelAttribute FreePostUpdateDto freePostUpdateDto) {
        Object dto = postService.updateFinalPost(postId, loginId, freePostUpdateDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "최종 저장완료", dto), HttpStatus.OK);
    }
    //  공지사항 게시물 작성시, 제목/이미지/내용 저장(최종 저장)
    @PutMapping("/update/notice/{postId}") // 임시저장 때, postId가 나와서 쉽게 조회 후 저장
    public ResponseEntity<?> noticeSave(@PathVariable Long postId, @RequestHeader("X-User-LoginId") String loginId
            , @ModelAttribute NoticePostUpdateDto noticePostUpdateDto) {
        Object dto = postService.updateFinalPost(postId, loginId, noticePostUpdateDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "최종 저장완료", dto), HttpStatus.OK);
    }


//    4. 투표 게시물 저장
    @PutMapping("/update/vote/{voteId}") // 임시저장 때, postId가 나와서 쉽게 조회 후 저장
    public ResponseEntity<?> voteSave(@PathVariable Long voteId, @RequestHeader("X-User-LoginId") String loginId
            , @ModelAttribute VotePostUpdateDto votePostUpdateDto) {
        VotePostUpdateDto dto = postService.saveVote(voteId, loginId, votePostUpdateDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "최종 저장완료", dto), HttpStatus.OK);
    }

    //    5. 게시물 삭제
    @PostMapping("/delete/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId, @RequestHeader("X-User-LoginId") String loginId) {
        postService.delete(postId, loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 삭제 완료", postId), HttpStatus.OK);
    }

    //    6.게시물 전체 조회
    @GetMapping("/list")
    public ResponseEntity<?> getPostList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<PostVoteResDTO> postListResDtos = postService.getPostAndVoteList(page, size);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시물 리스트 불러오기 완료", postListResDtos), HttpStatus.OK);
    }

//    자유글 조회
    @GetMapping("/free/list")
    public ResponseEntity<?> getFreeList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                                         @RequestHeader("X-User-LoginId") String loginId){
        Page<PostListResDto> freeList = postService.getFreeList(page,size,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"자유글 리스트 불러오기 완료",freeList),HttpStatus.OK);
    }
//    공지글 조회
    @GetMapping("/notice/list")
    public ResponseEntity<?> getNoticeList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                                         @RequestHeader("X-User-LoginId") String loginId){
        Page<PostListResDto> noticeList = postService.getNoticeList(page,size,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"공지글 리스트 불러오기 완료",noticeList),HttpStatus.OK);
    }
//    투표 조회
    @GetMapping("/vote/list")
    public ResponseEntity<?> getVoteList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                           @RequestParam(name = "size", defaultValue = "5") Integer size,
                                           @RequestHeader("X-User-LoginId") String loginId){
        Page<VoteResListDto> voteList = postService.getVoteList(page,size,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"공지글 리스트 불러오기 완료",voteList),HttpStatus.OK);
    }

    // 7. 상세게시물 조회
    @GetMapping("/detail/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId,@RequestHeader("X-User-LoginId") String loginId){
        PostDetailResDto postDetailResDto = postService.getDetail(postId,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "게시줄 조회 완료",postDetailResDto),HttpStatus.OK);
    }


//    8. 게시물 좋아요 완료
    @PostMapping("/like/{postId}")
    public ResponseEntity<?> postLike(@PathVariable Long postId,@RequestHeader("X-User-LoginId") String loginId){
        PostLikeResDto likeInfo = PostLikeService.togglePostLike(postId,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"게시물 좋아요 완료",likeInfo),HttpStatus.OK);
    }


}