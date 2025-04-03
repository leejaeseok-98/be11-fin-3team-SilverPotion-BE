package silverpotion.postserver.comment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import silverpotion.postserver.comment.repository.CommentLikeRepository;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.dtos.UserListDto;

import java.util.List;

@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, UserClient userClient, ObjectMapper objectMapper) {
        this.commentLikeRepository = commentLikeRepository;
        this.userClient = userClient;
        this.objectMapper = objectMapper;
    }

    public Page<UserListDto> getCommentLikeUserList(Long commentId, Pageable pageable){
        // 1. 댓글 좋아요 누른 유저 ID 페이징 조회
        Page<Long> likedUserIdsPage = commentLikeRepository.findUsersWhoLikedComment(commentId,pageable);
        System.out.println(likedUserIdsPage);
        // 2. ID리스트만 추출 (getContent는 현재 요청한 페이지에 존재하는 데이터 리스트를 가져옴)
        List<Long> userIds = likedUserIdsPage.getContent();
//        3. userClient를 통해 유저 정보 받아오기 ** commonDto 타입으로 받아  데이터를 꺼내서 매핑시켜줌
        CommonDto userList = userClient.getUsersByIds(userIds);
        Object result = userList.getResult();
        if (result == null) {
            System.out.println("⚠️ userList.getResult()가 null입니다.");
        }
        List<UserListDto> listDtos = objectMapper.convertValue(result, new TypeReference<List<UserListDto>>() {
        });

//        4. pageImpl로 다시 포장(page 정보 유지)  pageImpl은 페이징하는 기본 클래스 / getTotalElements는 조건에 해당하는 전체 데이터 수
        return new PageImpl<>(listDtos, pageable, likedUserIdsPage.getTotalElements());
    }

}
