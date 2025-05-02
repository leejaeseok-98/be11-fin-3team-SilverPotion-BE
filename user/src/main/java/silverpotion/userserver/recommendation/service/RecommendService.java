package silverpotion.userserver.recommendation.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.recommendation.domain.UserVectorUtils;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

@Service
@Transactional
public class RecommendService {
    private final UserRepository userRepository;

    public RecommendService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    1.사용자에게 맞는 소모임 5개 추천
    public void recommendGatherings(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
      //유저 벡터값 계산
        double[] userVector = UserVectorUtils.makingUserVector(user);
        System.out.println(userVector[0]);
        System.out.println(userVector[1]);
        System.out.println(userVector[2]);
        System.out.println(userVector[3]);
    }
}
