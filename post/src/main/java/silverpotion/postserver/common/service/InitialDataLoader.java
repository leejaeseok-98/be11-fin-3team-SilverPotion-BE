package silverpotion.postserver.common.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategoryDetail;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryDetailRepository;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryRepository;

import java.util.List;

@Component
public class InitialDataLoader implements CommandLineRunner {


    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final GatheringCategoryDetailRepository gatheringCategoryDetailRepository;

    public InitialDataLoader(GatheringCategoryRepository gatheringCategoryRepository, GatheringCategoryDetailRepository gatheringCategoryDetailRepository) {
        this.gatheringCategoryRepository = gatheringCategoryRepository;
        this.gatheringCategoryDetailRepository = gatheringCategoryDetailRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createCategories();
    }

    public void createCategories(){

        if (gatheringCategoryRepository.count() == 0) {
            GatheringCategory sports = gatheringCategoryRepository.save(GatheringCategory.builder().name("운동/스포츠").build());
            GatheringCategory book = gatheringCategoryRepository.save(GatheringCategory.builder().name("책/글").build());
            GatheringCategory craft = gatheringCategoryRepository.save(GatheringCategory.builder().name("공예").build());
            GatheringCategory volunteer = gatheringCategoryRepository.save(GatheringCategory.builder().name("봉사활동").build());
            GatheringCategory watching = gatheringCategoryRepository.save(GatheringCategory.builder().name("스포츠관람").build());
            GatheringCategory pet = gatheringCategoryRepository.save(GatheringCategory.builder().name("반려동물").build());
            GatheringCategory music = gatheringCategoryRepository.save(GatheringCategory.builder().name("음악/악기").build());
            GatheringCategory travel = gatheringCategoryRepository.save(GatheringCategory.builder().name("여행").build());
            GatheringCategory culture = gatheringCategoryRepository.save(GatheringCategory.builder().name("문화/공연").build());
            GatheringCategory dance = gatheringCategoryRepository.save(GatheringCategory.builder().name("댄스/무용").build());
            GatheringCategory social = gatheringCategoryRepository.save(GatheringCategory.builder().name("사교/인맥").build());
            GatheringCategory photo = gatheringCategoryRepository.save(GatheringCategory.builder().name("사진/영상").build());
            GatheringCategory cook = gatheringCategoryRepository.save(GatheringCategory.builder().name("요리").build());

            gatheringCategoryDetailRepository.saveAll(List.of(
                    GatheringCategoryDetail.builder().name("게이트볼").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("골프").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("당구/포켓볼").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("러닝/마라톤").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("배드민턴").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("수영").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("자전거").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("족구").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("축구/풋살").gatheringCategory(sports).build(),
                    GatheringCategoryDetail.builder().name("탁구").gatheringCategory(sports).build(),

                    GatheringCategoryDetail.builder().name("책/독서").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("인문학").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("심리학").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("철학").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("역사").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("시사/경제").gatheringCategory(book).build(),
                    GatheringCategoryDetail.builder().name("작문/글쓰기").gatheringCategory(book).build(),

                    GatheringCategoryDetail.builder().name("가구/목공예").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("가죽공예").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("도자/점토공예").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("미술/그림").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("소품공예").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("자수/뜨개질").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("천연비누/화장품").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("캔들/디퓨저").gatheringCategory(craft).build(),
                    GatheringCategoryDetail.builder().name("플라워아트").gatheringCategory(craft).build(),


                    GatheringCategoryDetail.builder().name("교육/재능나눔").gatheringCategory(volunteer).build(),
                    GatheringCategoryDetail.builder().name("보육원").gatheringCategory(volunteer).build(),
                    GatheringCategoryDetail.builder().name("사회봉사").gatheringCategory(volunteer).build(),
                    GatheringCategoryDetail.builder().name("양로원").gatheringCategory(volunteer).build(),
                    GatheringCategoryDetail.builder().name("유기동물보호").gatheringCategory(volunteer).build(),
                    GatheringCategoryDetail.builder().name("환경봉사").gatheringCategory(volunteer).build(),

                    GatheringCategoryDetail.builder().name("농구KBL").gatheringCategory(watching).build(),
                    GatheringCategoryDetail.builder().name("배구V리그").gatheringCategory(watching).build(),
                    GatheringCategoryDetail.builder().name("야구KBO").gatheringCategory(watching).build(),
                    GatheringCategoryDetail.builder().name("축구K리그").gatheringCategory(watching).build(),
                    GatheringCategoryDetail.builder().name("해외축구").gatheringCategory(watching).build(),
                    GatheringCategoryDetail.builder().name("e스포츠").gatheringCategory(watching).build(),

                    GatheringCategoryDetail.builder().name("강아지").gatheringCategory(pet).build(),
                    GatheringCategoryDetail.builder().name("고양이").gatheringCategory(pet).build(),
                    GatheringCategoryDetail.builder().name("물고기").gatheringCategory(pet).build(),
                    GatheringCategoryDetail.builder().name("파충류").gatheringCategory(pet).build(),
                    GatheringCategoryDetail.builder().name("설치류").gatheringCategory(pet).build(),

                    GatheringCategoryDetail.builder().name("국악/사물놀이").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("기타/베이스").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("노래/보컬").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("바이올린").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("밴드/합주").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("오카리나").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("재즈").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("찬양/CCM").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("클래식").gatheringCategory(music).build(),
                    GatheringCategoryDetail.builder().name("피아노").gatheringCategory(music).build(),

                    GatheringCategoryDetail.builder().name("국내여행").gatheringCategory(travel).build(),
                    GatheringCategoryDetail.builder().name("낚시").gatheringCategory(travel).build(),
                    GatheringCategoryDetail.builder().name("등산").gatheringCategory(travel).build(),
                    GatheringCategoryDetail.builder().name("산책/트래킹").gatheringCategory(travel).build(),
                    GatheringCategoryDetail.builder().name("캠핑/백패킹").gatheringCategory(travel).build(),
                    GatheringCategoryDetail.builder().name("해외여행").gatheringCategory(travel).build(),

                    GatheringCategoryDetail.builder().name("고궁/문화재탐방").gatheringCategory(culture).build(),
                    GatheringCategoryDetail.builder().name("공연/연극").gatheringCategory(culture).build(),
                    GatheringCategoryDetail.builder().name("뮤지컬/오페라").gatheringCategory(culture).build(),
                    GatheringCategoryDetail.builder().name("영화").gatheringCategory(culture).build(),
                    GatheringCategoryDetail.builder().name("전시회").gatheringCategory(culture).build(),

                    GatheringCategoryDetail.builder().name("라틴댄스").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("밸리댄스").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("사교댄스").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("스윙댄스").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("재즈댄스").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("한국무용").gatheringCategory(dance).build(),
                    GatheringCategoryDetail.builder().name("현대무용").gatheringCategory(dance).build(),

                    GatheringCategoryDetail.builder().name("결혼").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("나이").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("맛집/미식회").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("와인/커피/차").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("지역").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("파티").gatheringCategory(social).build(),

                    GatheringCategoryDetail.builder().name("디지털카메라").gatheringCategory(photo).build(),
                    GatheringCategoryDetail.builder().name("영상제작").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("필름카메라").gatheringCategory(social).build(),
                    GatheringCategoryDetail.builder().name("DSLR").gatheringCategory(social).build(),

                    GatheringCategoryDetail.builder().name("한식").gatheringCategory(cook).build(),
                    GatheringCategoryDetail.builder().name("양식").gatheringCategory(cook).build(),
                    GatheringCategoryDetail.builder().name("중식").gatheringCategory(cook).build(),
                    GatheringCategoryDetail.builder().name("일식").gatheringCategory(cook).build(),
                    GatheringCategoryDetail.builder().name("제과제빵").gatheringCategory(cook).build(),
                    GatheringCategoryDetail.builder().name("주류제조/칵테일").gatheringCategory(cook).build()
            ));
        }
    }
}
