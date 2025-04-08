package silverpotion.userserver.plan.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import silverpotion.userserver.plan.domain.Plan;
import silverpotion.userserver.plan.dto.PlanCreateDto;
import silverpotion.userserver.plan.dto.PlanDetailResDto;
import silverpotion.userserver.plan.dto.PlanListResDto;
import silverpotion.userserver.plan.dto.PlanUpdateDto;
import silverpotion.userserver.plan.repository.PlanRepository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PlanService {
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
//    private final GoogleCalendarService googleCalendarService;

    // 캘린더 플랜 생성
    public void createPlan(PlanCreateDto dto, String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        planRepository.save(dto.toEntity(user));
//        // ✅ 구글 캘린더 연동
//        googleCalendarService.createGoogleCalendarEvent(
//                dto.getTitle(),
//                dto.getContent(),
//                dto.getStartTime(),
//                dto.getEndTime()
//        );
    }

    // 캘린더 플랜 업데이트
    public void updatePlan(PlanUpdateDto dto, String loginId ){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("not found user"));

        Plan plan = planRepository.findById(dto.getId()).orElseThrow(()->new EntityNotFoundException("not found plan"));
        System.out.println(dto);

        if(plan.getUser().getLoginId().equals(loginId)){
            plan.toUpdate(dto);
            planRepository.save(plan);
        }
    }

    // 자신의 캘린더 리스트
    public List<PlanListResDto> planListResDtos(String loginId){
        return planRepository.findByUserLoginId(loginId).stream()
                .map(Plan::listFromEntity)
                .toList();
    }
    // 플랜 디테일
    public PlanDetailResDto planDetail(Long id){
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        return plan.detailFromEntity();
    }
    // 플랜 삭제
    public void deletedPlan(Long id){
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        planRepository.delete(plan);
    }

}
