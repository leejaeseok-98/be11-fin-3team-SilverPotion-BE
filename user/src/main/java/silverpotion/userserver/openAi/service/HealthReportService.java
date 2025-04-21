package silverpotion.userserver.openAi.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.openAi.domain.HealthReport;
import silverpotion.userserver.openAi.dto.HealtReportOfDepReqDto;
import silverpotion.userserver.openAi.dto.HealthReportDto;
import silverpotion.userserver.openAi.repository.HealthReportRepository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.dto.UserPromptDto;
import silverpotion.userserver.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class HealthReportService {
    private final WebClient openAiWebClient;
    private final UserRepository userRepository;
    private final HealthDataRepository healthDataRepository;
    private final HealthReportRepository healthReportRepository;

    public HealthReportService(@Qualifier("openAi") WebClient openAiWebClient, UserRepository userRepository, HealthDataRepository healthDataRepository, HealthReportRepository healthReportRepository) {
        this.openAiWebClient = openAiWebClient;
        this.userRepository = userRepository;
        this.healthDataRepository = healthDataRepository;
        this.healthReportRepository = healthReportRepository;
    }

// 1.헬스리포트 생성 및 실시간 조회
    public Mono<String> chatWithGpt(String loginId) {
            User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
            UserPromptDto promtInfo = user.healthPrompt();

            // GPT에 보낼 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 사용자의 나이와 성별을 고려해서 심박수,오늘 걸음수, 걸은 거리, 오늘 소모칼로리,수면기록 에대해 문단별로 각 항목마다 현재까지 현황을 파악하며 이게 어떤의미인지 알려주고 그리고 조언도 해주고, 마지막 문단에는 건강관련한 인사이트를 줬으면 좋겠어. "),
                        Map.of("role", "user", "content", user.healthPrompt().getPrompt())
                },
                "temperature", 0.7 //temperature은 답변의 창의성 정도
        );

        return openAiWebClient.post()
                .uri("/chat/completions")//GPT에게 포스트 요청 보내는데
                .bodyValue(requestBody)//요청 본문에 위에서 구성한 requestBody담고 요청
                .retrieve()//요청에 대한 비동기 응답을 받고
                .bodyToMono(Map.class)//GPT의 JSON응답구조를 Map으로 파싱하는데 비동기적으로 Mono로 감싼다.
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices"); // 응답에서 "choices" 배열 꺼내기
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message"); // 첫 번째 choice 안의 "message" 꺼내기
                    String content = (String) message.get("content"); // 그 안에서 content꺼내서 리턴(content가 GPT가 생성한 답변 문장)
                // 헬스리포트 생성해서 저장

                    LocalDate today = LocalDate.now();
                    HealthData healthData = promtInfo.getHealthData();

                    Optional<HealthReport> healthReportOG = healthReportRepository.findByHealthDataIdAndCreatedDate(healthData.getId(),today);
                    HealthReport healthReport = HealthReport.builder()
                            .text(content)
                            .healthData(healthData)
                            .createdDate(today)
                            .dataType(silverpotion.userserver.openAi.domain.DataType.DAY)
                            .build();

                    if(healthReportOG.isPresent()){ // 이미 오늘자 전 ai리포트 있으면
                        healthReportRepository.delete(healthReportOG.get()); //삭제하고
                        healthReportRepository.save(healthReport); //새로운 거 저장
                    } else{
                        healthReportRepository.save(healthReport);
                    }
                    return content;
                });
    }
    //1-1.<일간>헬스리포트 생성
    public Mono<String> dailyReportMake(String loginId) {
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
        UserPromptDto promtInfo = user.healthPromptForDay();

        // GPT에 보낼 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 내 나이와 성별을 고려해서 어제 건강데이터에 대한 설명을 해줘야 해. 전반적인 요약 , 걸음 수, 심박수, 소모칼로리, 수면에 대해 답을 하고" +
                                "답은  다음 형식의 json 문자열로 응답해줘. 각 카테고리는 반드시 포함되어야 하고 그 외에는 아무 말도 하지마. " + "{\n" +
                                        "  \"걸음\": \"이번 주 평균 걸음 수는 8000보입니다. 적당한 활동량입니다.\",\n" +
                                        "  \"심박수\": \"평균 심박수는 79bpm으로 안정적인 상태입니다.\",\n" +
                                        "  \"소모칼로리\": \"하루 평균 소모 칼로리는 500kcal로 목표에 도달하지 못했습니다. 가벼운 유산소 운동을 늘려보세요.\",\n" +
                                        "  \"수면\": \"평균 수면 시간은 6시간으로 부족합니다. 최소 7시간 이상 수면을 취해보세요.\",\n" +
                                        "  \"종합조언\": \"건강 지표는 전반적으로 양호하지만 수면 개선과 운동량 증가가 필요합니다.\"\n" +
                                        "}\n"
                                "각 카테고리마다 나의 데이터를 나와 비슷한 연령대와 성별을 가진 사람들과 비교해서 상태를 말해주고 또 구체적으로 어떻게 해야 좋을 지, 어제 건강데이터는 이러니까 오늘은 어떻게 하는 걸 추천하는지와 관련해서 답해줘."),
                        Map.of("role", "user", "content", promtInfo.getPrompt())
                },
                "temperature", 0.7 //temperature은 답변의 창의성 정도
        );

        return openAiWebClient.post()
                .uri("/chat/completions")//GPT에게 포스트 요청 보내는데
                .bodyValue(requestBody)//요청 본문에 위에서 구성한 requestBody담고 요청
                .retrieve()//요청에 대한 비동기 응답을 받고
                .bodyToMono(Map.class)//GPT의 JSON응답구조를 Map으로 파싱하는데 비동기적으로 Mono로 감싼다.
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices"); // 응답에서 "choices" 배열 꺼내기
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message"); // 첫 번째 choice 안의 "message" 꺼내기
                    String content = (String) message.get("content"); // 그 안에서 content꺼내서 리턴(content가 GPT가 생성한 답변 문장)
                    // 헬스리포트 생성해서 저장

                    LocalDate today = LocalDate.now();
                    HealthData healthData = promtInfo.getHealthData();

                    HealthReport healthReport = HealthReport.builder()
                            .text(content)
                            .healthData(healthData)
                            .createdDate(today)
                            .dataType(silverpotion.userserver.openAi.domain.DataType.DAY)
                            .build();

                    healthReportRepository.save(healthReport);

                    return content;
                });
    }


    // 1-2.<주간>헬스리포트 생성
    public Mono<String> weeklyReportMake(String loginId) {
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
        UserPromptDto promtInfo = this.createWeekPrompt(user);

        // GPT에 보낼 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 사용자의 나이와 성별을 고려해서 이번 주 평균 심박수,이번 주 평균 걸음수, 이번 주 평균 걸은 거리, 이번 주 평균 소모칼로리,이번 주 평균 수면기록 에대해 문단별로 각 항목마다 어떤 의미인지 알려주고 그리고 조언도 해주고, 마지막 문단에는 이번 주 평균 건강기록을 기반으로 다음주에 생활 양식 혹은 건강,먹거리 등 조언을 해주며 건강관련한 종합적인 인사이트를 줬으면 좋겠어. "),
                        Map.of("role", "user", "content", promtInfo.getPrompt())
                },
                "temperature", 0.7 //temperature은 답변의 창의성 정도
        );

        return openAiWebClient.post()
                .uri("/chat/completions")//GPT에게 포스트 요청 보내는데
                .bodyValue(requestBody)//요청 본문에 위에서 구성한 requestBody담고 요청
                .retrieve()//요청에 대한 비동기 응답을 받고
                .bodyToMono(Map.class)//GPT의 JSON응답구조를 Map으로 파싱하는데 비동기적으로 Mono로 감싼다.
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices"); // 응답에서 "choices" 배열 꺼내기
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message"); // 첫 번째 choice 안의 "message" 꺼내기
                    String content = (String) message.get("content"); // 그 안에서 content꺼내서 리턴(content가 GPT가 생성한 답변 문장)
                    // 헬스리포트 생성해서 저장

                    LocalDate today = LocalDate.now();
                    HealthData healthData = promtInfo.getHealthData();

                    HealthReport healthReport = HealthReport.builder()
                            .text(content)
                            .healthData(healthData)
                            .createdDate(today)
                            .dataType(silverpotion.userserver.openAi.domain.DataType.WEEKAVG)
                            .build();

                    healthReportRepository.save(healthReport);

                    return content;
                });
    }



    // 1-3.<월간>헬스리포트 생성
    public Mono<String> monthlyReportMake(String loginId) {
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
        UserPromptDto promtInfo = user.healthPromptForMonth();

        // GPT에 보낼 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 사용자의 나이와 성별을 고려해서 이번 달 평균 심박수,이번 달 평균 걸음수, 이번 달 평균 걸은 거리, 이번 달 평균 소모칼로리,이번 달 평균 수면기록 에대해 문단별로 각 항목마다 종합적인 진단을 내려줘 그리고 조언도 해주고, 마지막 문단에는 이번 달 평균 건강기록을 기반으로 다음 달에 생활 양식 혹은 건강,먹거리 등 조언을 해주며 건강관련한 종합적인 인사이트를 줬으면 좋겠어. "),
                        Map.of("role", "user", "content", promtInfo.getPrompt())
                },
                "temperature", 0.7 //temperature은 답변의 창의성 정도
        );

        return openAiWebClient.post()
                .uri("/chat/completions")//GPT에게 포스트 요청 보내는데
                .bodyValue(requestBody)//요청 본문에 위에서 구성한 requestBody담고 요청
                .retrieve()//요청에 대한 비동기 응답을 받고
                .bodyToMono(Map.class)//GPT의 JSON응답구조를 Map으로 파싱하는데 비동기적으로 Mono로 감싼다.
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices"); // 응답에서 "choices" 배열 꺼내기
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message"); // 첫 번째 choice 안의 "message" 꺼내기
                    String content = (String) message.get("content"); // 그 안에서 content꺼내서 리턴(content가 GPT가 생성한 답변 문장)
                    // 헬스리포트 생성해서 저장

                    LocalDate today = LocalDate.now();
                    HealthData healthData = promtInfo.getHealthData();

                    HealthReport healthReport = HealthReport.builder()
                            .text(content)
                            .healthData(healthData)
                            .createdDate(today)
                            .dataType(silverpotion.userserver.openAi.domain.DataType.MONTHAVG)
                            .build();

                    healthReportRepository.save(healthReport);

                    return content;
                });
    }















//    2.지난 헬스리포트 조회
    public HealthReportDto pastReport(String loginId, String date){
        LocalDate selectedData = LocalDate.parse(date);

     User loginUser = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
     //일단, 그 날짜의 회원 헬스데이터를 가지고 옴.(그날 만들어진 것중 일일 데이터타입인 것임)
     HealthData healthData = loginUser.getMyHealthData().stream().filter(h->h.getCreatedDate().equals(selectedData)).filter(h->h.getDataType()== DataType.DAY).findFirst().orElseThrow(()->new EntityNotFoundException("당일 기록이 없습니다"));
    HealthReport report = healthReportRepository.findByHealthDataIdAndCreatedDate(healthData.getId(),selectedData).orElseThrow(()->new EntityNotFoundException("당일 ai리포트 기록이 없습니다"));
    return report.toReportDtoFromEntity();

    }

//    3.내 피보호자 헬스리포트 조회
    public HealthReportDto pastReportOfDep(String loginId, HealtReportOfDepReqDto dto){
        User protector = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        System.out.println(protector.getName());
        List<CareRelation> relation = protector.getAsProtectors();
        //내 관계 중 프론트에서 입력받은 피보호자 id를 가지고 있는 관계를 찾고
        System.out.println(relation.size());
        System.out.println(protector.getAsProtectors().get(0).getDependent().getName());
        System.out.println(protector.getAsProtectors().get(0).getDependent().getId());

        System.out.println("요청 받은 dependentId = " + dto.getDependentId());

        relation.forEach(r -> {
            System.out.println(">> 관계에 등록된 dependentId = " + r.getDependent().getId());
        });

        CareRelation targetRelation = relation.stream().filter(c->c.getDependent().getId().equals(dto.getDependentId())).findFirst().orElseThrow(()->new EntityNotFoundException("해당 피보호자가 없습니다"));
        User dependent = userRepository.findByIdAndDelYN(dto.getDependentId(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        LocalDate selectedDate = LocalDate.parse(dto.getSelectedDate());

        HealthData healthData = dependent.getMyHealthData().stream().filter(h->h.getCreatedDate().equals(selectedDate)&&h.getDataType()==DataType.DAY).findFirst().orElseThrow(()->new EntityNotFoundException("당일 건강기록 없습니다"));
        HealthReport healthReport = healthReportRepository.findByHealthDataIdAndCreatedDate(healthData.getId(),selectedDate).orElseThrow(()->new EntityNotFoundException("당일 ai리포트 기록이 없습니다"));
        return healthReport.toReportDtoFromEntity();
    }

//

    public UserPromptDto createWeekPrompt(User user) {
        LocalDate today = LocalDate.now();
        HealthData weekData = healthDataRepository.findByUserIdAndCreatedDateAndDataType(
                user.getId(), today, DataType.WEEKAVG
        ).orElseThrow(() -> new EntityNotFoundException("주간 헬스데이터가 없습니다."));

        String promt ="나이 : " + user.myAge() +", 성별 : " + user.mySex()+
                ", 이번 주 평균 걸음 횟수 " + weekData.getStep() + "이번 주 평균 심박수 :" + weekData.getHeartbeat()
                +", 이번 주 평균 걸은 거리 : " + weekData.getDistance() + "이번 주 평균 소모 칼로리 : " + weekData.getCalory()
                +", 이번 주  평균 총 수면시간(분) : " + weekData.getTotalSleepMinutes() + "이번 주 평균 깊은 수면시간(분) : " +weekData.getDeepSleepMinutes()
                +", 이번 주 평균 렘 수면시간(분) : " + weekData.getRemSleepMinutes() + "이번주 평균 얉은 수면시간(분) : " + weekData.getLightSleepMinutes();

        return UserPromptDto.builder().healthData(weekData).prompt(promt).build();
    }








}
