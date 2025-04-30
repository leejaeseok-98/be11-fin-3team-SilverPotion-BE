package silverpotion.userserver.admin.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import silverpotion.userserver.admin.service.AdminService;
import silverpotion.userserver.user.service.UserService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserUnbanScheduler {
    private final AdminService adminService;
    private final RedissonClient redissonClient;
    private final UserService userService;

    public UserUnbanScheduler(AdminService adminService, RedissonClient redissonClient, UserService userService) {
        this.adminService = adminService;
        this.redissonClient = redissonClient;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 * * ?") //매일 자정
    public void autoUnbanUsers(){
        RLock lock = redissonClient.getLock("user-unban-lock");

        boolean isLocked = false;
        try {
            //락 획득 시도 : 최대 5초 대기, 락 유지 시간 60초
            isLocked = lock.tryLock(5,60, TimeUnit.SECONDS);
            if(isLocked){
                log.info("락 획득! 사용자 정지 작업 시작");
                //사용자 정지 로직
                int bannedCount = adminService.unbanExpiredUsers();
                log.info("자동 정지 처리완료 - 총 {}명",bannedCount );
                log.info("사용자 정지 작업 완료");
            }
            else{
                log.warn("락 획득 실패. 다른 인스턴스가 이미 실행 중입니다.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("락 획득 중 인터럽트 발생", e);
        }finally {
            if(isLocked){
                lock.unlock();
                log.info("락 해제 완료");
            }
        }
    }
}
