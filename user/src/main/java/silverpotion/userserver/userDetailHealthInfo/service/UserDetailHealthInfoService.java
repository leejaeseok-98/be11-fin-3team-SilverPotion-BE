package silverpotion.userserver.userDetailHealthInfo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;
import silverpotion.userserver.userDetailHealthInfo.dtos.UserDetailHealthInfoCreateReqDto;
import silverpotion.userserver.userDetailHealthInfo.repository.UserDetailHealthInfoRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserDetailHealthInfoService {

    private final UserDetailHealthInfoRepository userDetailHealthInfoRepository;
    private final UserRepository userRepository;

    public UserDetailHealthInfoService(UserDetailHealthInfoRepository userDetailHealthInfoRepository, UserRepository userRepository) {
        this.userDetailHealthInfoRepository = userDetailHealthInfoRepository;
        this.userRepository = userRepository;
    }
//  1.유저상세건강정보 추가(생성)
    public void detailInfoAdd(String loginId, UserDetailHealthInfoCreateReqDto dto){
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
        if(user.getUserDetailHealthInfo() != null){
            UserDetailHealthInfo existedInfo = user.getUserDetailHealthInfo();
            existedInfo.updateInfo(dto);

        } else{
            UserDetailHealthInfo userDetailHealthInfo = dto.toEntityFromReqForCreation(user);
            userDetailHealthInfoRepository.save(userDetailHealthInfo);
        }


    }

//  2.BMI지수 조회
   public Map<String,Object> bmiCheck(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        Map<String,Object> bmiInfo = new HashMap<>();
        if(user.getUserDetailHealthInfo()==null){
            throw new IllegalArgumentException("건강상세조회를 하지 않은 유저입니다");
        } else {
            bmiInfo = user.makingBmi();
        }
        return bmiInfo;
   }





}
