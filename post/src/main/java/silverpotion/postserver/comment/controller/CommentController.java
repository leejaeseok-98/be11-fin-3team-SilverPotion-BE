package silverpotion.postserver.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.postserver.comment.dtos.CommentCreateDto;
import silverpotion.postserver.comment.service.CommentLikeService;
import silverpotion.postserver.comment.service.CommentService;
import silverpotion.postserver.common.dto.CommonDto;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> commentCreate(@RequestHeader("X-User-Id") String loginId, CommentCreateDto commentCreate){
        Long postId = commentService.commentCreate(loginId,commentCreate);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "댓글 작성 완료",postId),HttpStatus.CREATED);
    }
}
