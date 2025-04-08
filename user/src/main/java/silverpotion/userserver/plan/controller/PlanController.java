package silverpotion.userserver.plan.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.annotation.LoginUser;
import silverpotion.userserver.plan.dto.PlanCreateDto;
import silverpotion.userserver.plan.dto.PlanDetailResDto;
import silverpotion.userserver.plan.dto.PlanListResDto;
import silverpotion.userserver.plan.dto.PlanUpdateDto;
import silverpotion.userserver.plan.service.PlanService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    // 캘린더 플랜 생성
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateDto dto, @LoginUser String loginId){
        planService.createPlan(dto,loginId);
        return ResponseEntity.ok().build();
    }
    // 캘린더 플랜 업데이트
    @PostMapping("/update")
    public ResponseEntity<?> updateEvent(@RequestBody PlanUpdateDto dto, @LoginUser String loginId) {
        planService.updatePlan(dto, loginId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // 자신의 캘린더 플랜 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<PlanListResDto>> eventList(@LoginUser String loginId) {
        List<PlanListResDto> planList = planService.planListResDtos(loginId);
        return ResponseEntity.ok(planList);
    }

    // 특정 플랜 디테일 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> eventDetail(@PathVariable Long id) {
        PlanDetailResDto planDetail = planService.planDetail(id);
        return ResponseEntity.ok(planDetail);
    }

    // 캘린더 플랜 삭제 (soft delete)
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        planService.deletedPlan(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
