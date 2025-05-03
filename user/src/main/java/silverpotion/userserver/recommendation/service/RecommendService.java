package silverpotion.userserver.recommendation.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.common.feignClient.PostClient;
import silverpotion.userserver.recommendation.domain.UserVectorUtils;
import silverpotion.userserver.recommendation.dtos.GatheringInfoDtoForUserServiceDto;
import silverpotion.userserver.recommendation.dtos.GatheringVectorForUserServiceDto;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecommendService {
    private final UserRepository userRepository;
    private final PostClient postClient;


    public RecommendService(UserRepository userRepository, PostClient postClient) {
        this.userRepository = userRepository;
        this.postClient = postClient;
    }


//    1.사용자에게 맞는 소모임 5개 추천 성향과 다른 소모임 3개추천
    public  Map<String,List<GatheringInfoDtoForUserServiceDto>> recommendGatherings(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
      //유저 벡터값 계산
        double[] userVector = UserVectorUtils.makingUserVector(user);

        List<GatheringVectorForUserServiceDto> allGatheringVectors = postClient.allGatheringsForUserService();
        //코사인 유사도가 가장 유사한 모임 아이디 5개 뽑아내기,       //AbstractMap.SimpleEntry는 자바에서 키-값 쌍을 만드는 간단한 묶음 (여기서는 게더링 아이디와, 유사도를 묶었음)
        List<Long> top5Ids = allGatheringVectors.stream().map(gv->new AbstractMap.SimpleEntry<>(
                gv.getId(), UserVectorUtils.cosineSimilarity(userVector,gv.toVectorValue())
                ))//아래에서 부터 유사도 점수를 기준으로 내림차순 정렬.그러면 유사도가 높은 소모임이 앞으로 옴.그럼다음 앞에서 5개 끊고.Map.Entry객체에서 키(여기서는 아이디만) 가지고 온다
                .sorted((a,b) -> Double.compare(b.getValue(),a.getValue())).limit(5).map(Map.Entry::getKey)
                .toList();

        List<GatheringInfoDtoForUserServiceDto> similarGatherings = postClient.fiveRecommendedGatherings(top5Ids);


        //유클리드 거리 계산으로 가장 차이가 큰 모임 아이디 3개 뽑아내기
        List<Long> top3DiffIds = allGatheringVectors.stream().map(gv->new AbstractMap.SimpleEntry<>(
                gv.getId(), UserVectorUtils.euclideanDistance(userVector,gv.toVectorValue())
                ))
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue())).limit(3).map(Map.Entry::getKey)
                .toList();
        List<GatheringInfoDtoForUserServiceDto> differentGatherings = postClient.fiveRecommendedGatherings(top3DiffIds);

        Map<String,List<GatheringInfoDtoForUserServiceDto>> recommendedList = new HashMap<>();
        recommendedList.put("similar",similarGatherings);
        recommendedList.put("different", differentGatherings);
        return recommendedList;

    }





}
