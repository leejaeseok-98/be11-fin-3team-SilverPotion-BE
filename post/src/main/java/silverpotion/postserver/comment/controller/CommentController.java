package silverpotion.postserver.comment.controller;

import jakarta.ws.rs.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.comment.dtos.*;
import silverpotion.postserver.comment.service.CommentLikeService;
import silverpotion.postserver.comment.service.CommentService;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.post.dtos.UserListDto;

import java.util.List;

@RestController
@RequestMapping("silverpotion/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }
//  댓글 생성
    @PostMapping("/create")
    public ResponseEntity<?> commentCreate(@RequestHeader("X-User-LoginId") String loginId, @RequestBody CommentCreateDto commentCreate){
        Long postId = commentService.commentCreate(loginId,commentCreate);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "댓글 작성 완료",postId),HttpStatus.CREATED);
    }

//  댓글 수정
    @PatchMapping("/update")
    public ResponseEntity<?> commentUpdate(@RequestHeader("X-User-LoginId") String loginId, @RequestBody CommentUpdateDto commentUpdateDto){
        Long postId = commentService.commentUpdate(loginId,commentUpdateDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "댓글 수정 완료",postId),HttpStatus.OK);
    }

//  댓글 삭제
    @PostMapping("/delete/{commentId}")
    public ResponseEntity<?> commentDelete(@RequestHeader("X-User-LoginId") String loginId, @PathVariable Long commentId){
        Long postId = commentService.commentDelete(loginId, commentId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"댓글 삭제 완료",postId),HttpStatus.OK);
    }

//    대댓글 생성
    @PostMapping("/reply")
    public ResponseEntity<?> replyComment(@RequestHeader("X-User-LoginId") String loginId,@RequestBody ReplyCommentCreateReqDto dto){
        Long postId = commentService.replyCreate(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"대댓글 작성 완료",postId),HttpStatus.OK);
    }

//    댓글 좋아요
    @PostMapping("/like/{commentId}")
    public ResponseEntity<?> commentLike(@PathVariable Long commentId, @RequestHeader("X-User-LoginId") String loginId){
        CommentLikeResDto commentLikeResDto = commentService.commentLikeToggle(commentId,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"댓글 좋아요 완료",commentLikeResDto),HttpStatus.OK);
    }

//    댓글 좋아요 유저리스트 조회
    @GetMapping("/like/list/{commentId}")
    public ResponseEntity<?> getLikeList(@PathVariable Long commentId,
                                         @PageableDefault(size = 10)Pageable pageable){
        Page<UserListDto> commentLikeUserList = commentLikeService.getCommentLikeUserList(commentId,pageable);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"댓글 좋아요 유저리스트 완료",commentLikeUserList),HttpStatus.OK);
    }


}
