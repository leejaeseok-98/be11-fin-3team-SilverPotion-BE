package silverpotion.postserver.gathering.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
//import org.opensearch.client.RestHighLevelClient;
//import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.common.service.ImageService;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.domain.GatheringPeople;
import silverpotion.postserver.gathering.domain.Status;
import silverpotion.postserver.gathering.dto.*;
import silverpotion.postserver.gathering.repository.GatheringPeopleRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategoryDetail;
import silverpotion.postserver.gatheringCategory.domain.GatheringDetail;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryDetailRepository;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryRepository;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;
import silverpotion.postserver.gatheringCategory.repository.GatheringDetailRepository;
import silverpotion.postserver.opensearch.*;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final UserClient userClient;
    private final GatheringCategoryDetailRepository gatheringCategoryDetailRepository;
    private final GatheringDetailRepository gatheringDetailRepository;
    private final GatheringPeopleRepository gatheringPeopleRepository;
    private final ImageService imageService;
//    private final OpenSearchService openSearchService;
//    @Autowired
//    private RestHighLevelClient client;


    public GatheringService(GatheringRepository gatheringRepository, GatheringCategoryRepository gatheringCategoryRepository, UserClient userClient, GatheringCategoryDetailRepository gatheringCategoryDetailRepository, GatheringDetailRepository gatheringDetailRepository, GatheringPeopleRepository gatheringPeopleRepository, ImageService imageService
//            , OpenSearchService openSearchService
    ) {
        this.gatheringRepository = gatheringRepository;
        this.gatheringCategoryRepository = gatheringCategoryRepository;
        this.userClient = userClient;
        this.gatheringCategoryDetailRepository = gatheringCategoryDetailRepository;
        this.gatheringDetailRepository = gatheringDetailRepository;
        this.gatheringPeopleRepository = gatheringPeopleRepository;
        this.imageService = imageService;
//        this.openSearchService = openSearchService;
    }

//    @PostConstruct
//    public void init() {
//        try {
//            OpenSearchIndexUtil util = new OpenSearchIndexUtil(client);
//            util.createGatheringIndexIfNotExists();
//            util.createMeetingIndexIfNotExists();
//            System.out.println("✅ OpenSearch 인덱스 확인 완료 (gathering, meeting)");
//        } catch (Exception e) {
//            System.err.println("❌ OpenSearch 인덱스 생성 중 오류 발생: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }


    // 모임 생성
    public Long gatheringCreate(GatheringCreateDto dto, String loginId, List<Long> gatheringCategoryDetailIds) {
        if(gatheringRepository.findByGatheringNameAndDelYN(dto.getGatheringName(), DelYN.N).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 모임명입니다");
        }

        GatheringCategory gatheringCategory = gatheringCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다."));

        Long leaderId = userClient.getUserIdByLoginId(loginId);

        // 사용자가 생성한 모임 개수 조회
        long gatheringCount = gatheringRepository.countByLeaderId(leaderId);
        if (gatheringCount >= 8) {
            throw new IllegalArgumentException("하나의 사용자가 생성할 수 있는 최대 모임 개수(8개)를 초과하였습니다.");
        }

        Gathering gathering = gatheringRepository.save(dto.toEntity(gatheringCategory, leaderId));

        // 선택한 GatheringCategoryDetail을 기반으로 GatheringDetail 생성
        List<GatheringDetail> gatheringDetails = new ArrayList<>();
        for (Long gatheringCategoryDetailId : gatheringCategoryDetailIds) {
            GatheringCategoryDetail gatheringCategoryDetail = gatheringCategoryDetailRepository.findById(gatheringCategoryDetailId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 GatheringCategoryDetail ID입니다."));

            GatheringDetail gatheringDetail = new GatheringDetail(gathering, gatheringCategoryDetail);
            gatheringDetails.add(gatheringDetail);
        }

        // GatheringDetail 저장
        gatheringDetailRepository.saveAll(gatheringDetails);

        // OpenSearch index 저장
//        openSearchService.indexGathering(gathering);

        return gathering.getId();
    }

    // 모임 수정
    public void updateGathering(String loginId, Long gatheringId, GatheringUpdateDto dto) {
        // 로그인 ID로 userId 조회
        Long userId = userClient.getUserIdByLoginId(loginId);

        // Gathering 조회 및 모임장 검증
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        if (!gathering.getLeaderId().equals(userId)) {
            throw new IllegalArgumentException("모임장만 수정할 수 있습니다.");
        }

        // 이미지 업로드 (새로운 이미지가 있는 경우 S3 업데이트)
        String imageUrl = gathering.getImageUrl();
        MultipartFile imageFile = dto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = imageService.uploadImage(imageFile);
        }

        // Meeting 정보 업데이트 (null 체크 후 수정)
        if (dto.getGatheringName() != null) gathering.setGatheringName(dto.getGatheringName());
        if (dto.getIntroduce() != null) gathering.setIntroduce(dto.getIntroduce());
        if (dto.getMaxPeople() != null) gathering.setMaxPeople(dto.getMaxPeople());
        gathering.setImageUrl(imageUrl);

        gatheringRepository.save(gathering);

        // OpenSearch Index 저장
//        openSearchService.indexGathering(gathering);
    }

    // 내 모임 조회
    public List<GatheringInfoDto> getMyGatherings(String loginId) {
        // loginId로 userId 조회
        Long userId = userClient.getUserIdByLoginId(loginId);

        // GatheringPeople 테이블에서 사용자가 가입한 gatheringId 가져오기
        List<Long> gatheringIds = gatheringPeopleRepository.findByUserId(userId)
                .stream()
                .map(gp -> gp.getGathering().getId())
                .collect(Collectors.toList());

        // Gathering 테이블에서 해당 gathering 정보 가져오기
        return gatheringRepository.findByIdIn(gatheringIds)
                .stream()
                .map(gathering -> {
                    // 모임 카테고리명 가져오기
                    String category = gathering.getGatheringCategory() != null
                            ? gathering.getGatheringCategory().getName()
                            : "미분류";

                    // 현재 모임 인원 수 가져오기
                    Long peopleCount = gatheringPeopleRepository.countByGatheringIdAndStatusActivate(gathering.getId());

                    return new GatheringInfoDto(
                            gathering.getId(),
                            gathering.getGatheringName(),
                            gathering.getImageUrl(),
                            gathering.getRegion(),
                            gathering.getMaxPeople(),
                            category,
                            gathering.getIntroduce(),
                            peopleCount
                    );
                })
                .collect(Collectors.toList());
    }

    // 모임 참여자 수 조회
    public GatheringPeopleCountDto getActivePeopleCount(Long gatheringId) {
        Long count = gatheringPeopleRepository.countByGatheringIdAndStatusActivate(gatheringId);
        return new GatheringPeopleCountDto(gatheringId, count);
    }

    // 모임 상세조회
    public GatheringInfoDto getGatheringById(Long gatheringId) {
        Gathering gathering = gatheringRepository.findByIdAndDelYN(gatheringId,DelYN.N)
                .orElseThrow(() -> new EntityNotFoundException("해당 모임을 찾을 수 없습니다."));

        // 카테고리명 조회
        String category = gathering.getGatheringCategory() != null
                ? gathering.getGatheringCategory().getName()
                : "미분류";

        // 현재 모임 인원 수 조회
        Long peopleCount = gatheringPeopleRepository.countByGatheringIdAndStatusActivate(gatheringId);

        return new GatheringInfoDto(
                gathering.getId(),
                gathering.getGatheringName(),
                gathering.getImageUrl(),
                gathering.getRegion(),
                gathering.getMaxPeople(),
                category,
                gathering.getIntroduce(),
                peopleCount
        );
    }

    // 모임 검색
    public List<GatheringInfoDto> searchGatherings(String category, String gatheringName) {
        List<Gathering> gatherings;

        if (category != null && gatheringName != null) {
            // 카테고리와 모임명 모두 검색
            gatherings = gatheringRepository.findByGatheringCategoryNameAndGatheringNameContaining(category, gatheringName);
        } else if (category != null) {
            // 카테고리명만 검색
            gatherings = gatheringRepository.findByGatheringCategoryName(category);
        } else if (gatheringName != null) {
            // 모임명만 검색
            gatherings = gatheringRepository.findByGatheringNameContaining(gatheringName);
        } else {
            // 검색 조건 없으면 전체 목록 조회
            gatherings = gatheringRepository.findAll();
        }

        return gatherings.stream()
                .map(gathering -> new GatheringInfoDto(
                        gathering.getId(),
                        gathering.getGatheringName(),
                        gathering.getImageUrl(),
                        gathering.getRegion(),
                        gathering.getMaxPeople(),
                        gathering.getGatheringCategory() != null ? gathering.getGatheringCategory().getName() : "미분류",
                        gathering.getIntroduce(),
                        gatheringPeopleRepository.countByGatheringIdAndStatusActivate(gathering.getId())
                ))
                .collect(Collectors.toList());
    }

    // 모임별 userList
    public List<GatheringPeopleDto> getGatheringUserList(Long gatheringId) {
        List<GatheringPeople> gatheringPeopleList = gatheringPeopleRepository.findByGatheringId(gatheringId);

        return gatheringPeopleList.stream().map(gatheringPeople -> {
            // User 정보 조회
            UserProfileInfoDto profileInfo = userClient.getUserProfileInfo(gatheringPeople.getUserId());

            return new GatheringPeopleDto(
                    gatheringPeople.getGathering().getId(),
                    gatheringPeople.getUserId(),
                    profileInfo.getNickname(),
                    profileInfo.getProfileImage(),
                    gatheringPeople.getGreetingMessage(),
                    gatheringPeople.getStatus().name(),  // Enum -> String 변환
                    gatheringPeople.getCreatedTime()
            );
        }).collect(Collectors.toList());
    }

    // 모임 가입
    public void createGatheringPeople(GatheringPeopleCreateDto dto, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        Gathering gathering = gatheringRepository.findById(dto.getGatheringId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Gathering ID입니다."));

        Optional<GatheringPeople> existingMembership = gatheringPeopleRepository.findByGatheringIdAndUserId(dto.getGatheringId(), userId);

        if (existingMembership.isPresent()) {
            GatheringPeople gatheringPeople = existingMembership.get();
            Status status = gatheringPeople.getStatus();

            switch (status) {
                case WAIT:
                    throw new IllegalStateException("가입 대기중입니다.");
                case ACTIVATE:
                    throw new IllegalStateException("이미 가입된 모임입니다.");
                case BAN:
                    throw new IllegalStateException("추방된 모임입니다.");
                case DEACTIVATE:
                    // DEACTIVATE 상태면 상태를 WAIT으로 바꾸고 저장
                    gatheringPeople.setStatus(Status.WAIT);
                    gatheringPeople.setGreetingMessage(dto.getGreetingMessage()); // 인사말도 갱신 가능
                    gatheringPeopleRepository.save(gatheringPeople);
                    return;
            }
        }

        GatheringPeople gatheringPeople = GatheringPeople.builder()
                .gathering(gathering)
                .userId(userId)
                .greetingMessage(dto.getGreetingMessage())
                .status(Status.WAIT) // 기본 상태
                .build();

        gatheringPeopleRepository.save(gatheringPeople);
    }

    // 모임원 상태 변경
    public void updateGatheringPeopleStatus(Long gatheringPeopleId, String loginId, GatheringPeopleUpdateDto dto) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        // GatheringPeople 조회
        GatheringPeople gatheringPeople = gatheringPeopleRepository.findById(gatheringPeopleId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 GatheringPeople ID입니다."));

        // 모임 조회
        Gathering gathering = gatheringPeople.getGathering();

        // 요청자가 해당 모임의 모임장인지 검증
        if (!gathering.getLeaderId().equals(userId)) {
            throw new IllegalStateException("해당 모임의 모임장만 상태를 변경할 수 있습니다.");
        }

        // 상태 변경
        gatheringPeople.updateStatus(dto.getStatus());

        // 저장
        gatheringPeopleRepository.save(gatheringPeople);
    }

    // 모임장 양도
    public void changeLeader(Long gatheringId, String loginId, LeaderChangeDto dto) {
        // 로그인한 사용자의 ID 조회
        Long currentLeaderId = userClient.getUserIdByLoginId(loginId);

        // 해당 모임 조회
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Gathering ID입니다."));

        // 현재 로그인한 사용자가 모임장인지 검증
        if (!gathering.getLeaderId().equals(currentLeaderId)) {
            throw new IllegalArgumentException("모임장만이 모임장을 변경할 수 있습니다.");
        }

        // 새로운 모임장이 될 사람이 ACTIVATE 상태인지 검증
        boolean isActiveMember = gatheringPeopleRepository.existsByGatheringIdAndUserIdAndStatus(
                gatheringId, dto.getUserId(), Status.ACTIVATE);

        if (!isActiveMember) {
            throw new IllegalArgumentException("새로운 모임장은 해당 모임의 활성화된 멤버여야 합니다.");
        }

        // 새로운 모임장으로 변경
        gathering.changeLeader(dto.getUserId());
        gatheringRepository.save(gathering);
    }

    // 모임 탈퇴
    public void withdrawFromGathering(Long gatheringId, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        GatheringPeople gatheringPeople = gatheringPeopleRepository.findByGatheringIdAndUserId(gatheringId, userId)
                .orElseThrow(() -> new IllegalArgumentException("참여 정보가 존재하지 않습니다."));

        gatheringPeople.setStatus(Status.DEACTIVATE);
        gatheringPeople.setUpdatedTime(LocalDateTime.now());
        // save 생략 가능 (영속성 컨텍스트 관리하므로)
    }

    // 모임 해체
    public void disbandGathering(Long gatheringId, String loginId) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("모임이 존재하지 않습니다."));

        if (!gathering.getLeaderId().equals(userId)) {
            throw new IllegalStateException("해당 모임의 모임장만 모임을 해체할 수 있습니다.");
        }

        // 모임 해체 처리
        gathering.setDelYN(DelYN.Y);
        gathering.setUpdatedTime(LocalDateTime.now());

        // 해당 모임의 모든 참가자 상태 DEACTIVATE 처리
        List<GatheringPeople> peopleList = gatheringPeopleRepository.findAllByGatheringId(gatheringId);
        for (GatheringPeople person : peopleList) {
            person.setStatus(Status.DEACTIVATE);
            person.setUpdatedTime(LocalDateTime.now());
        }

        // OpenSearch Index 저장
//        openSearchService.indexGathering(gathering);
    }

//    // opensearch
//    public List<GatheringSearchResultDto> searchGatherings(GatheringSearchRequest request) {
//        return openSearchService.searchGatherings(request);
//    }

}
