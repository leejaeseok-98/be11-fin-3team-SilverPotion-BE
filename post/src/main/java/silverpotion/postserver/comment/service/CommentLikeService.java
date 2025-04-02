package silverpotion.postserver.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import silverpotion.postserver.comment.repository.CommentLikeRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.dtos.UserListDto;

import java.util.List;

@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final UserClient userClient;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, UserClient userClient) {
        this.commentLikeRepository = commentLikeRepository;
        this.userClient = userClient;
    }

    public Page<UserListDto> getCommentLikeUserList(Long commentId, Pageable pageable){
        // 1. 댓글 좋아요 누른 유저 ID 페이징 조회
        Page<Long> likedUserIdsPage = commentLikeRepository.findUsersWhoLikedComment(commentId,pageable);
        // 2. ID리스트만 추출 (getContent는 현재 요청한 페이지에 존재하는 데이터 리스트를 가져옴)
        List<Long> userIds = likedUserIdsPage.getContent();

//        3. userClient를 통해 유저 정보 받아오기
        List<UserListDto> userList = userClient.getUsersByIds(userIds);

//        4. pageImpl로 다시 포장(page 정보 유지)  pageImpl은 페이징하는 기본 클래스 / getTotalElements는 조건에 해당하는 전체 데이터 수
        return new PageImpl<>(userList, pageable, likedUserIdsPage.getTotalElements());
    }
}
