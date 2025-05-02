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
                    GatheringCategoryDetail.builder().name("게이트볼").gatheringCategory(sports).empathySupport(15).connectivitySupport(12).activitySupport(15).achievementSupport(11).build(),
                    GatheringCategoryDetail.builder().name("골프").gatheringCategory(sports).empathySupport(19).connectivitySupport(14).activitySupport(12).achievementSupport(11).build(),
                    GatheringCategoryDetail.builder().name("당구/포켓볼").gatheringCategory(sports).empathySupport(14).connectivitySupport(8).activitySupport(6).achievementSupport(11).build(),
                    GatheringCategoryDetail.builder().name("러닝/마라톤").gatheringCategory(sports).empathySupport(14).connectivitySupport(12).activitySupport(20).achievementSupport(23).build(),
                    GatheringCategoryDetail.builder().name("배드민턴").gatheringCategory(sports).empathySupport(14).connectivitySupport(10).activitySupport(13).achievementSupport(19).build(),
                    GatheringCategoryDetail.builder().name("수영").gatheringCategory(sports).empathySupport(12).connectivitySupport(6).activitySupport(13).achievementSupport(18).build(),
                    GatheringCategoryDetail.builder().name("자전거").gatheringCategory(sports).empathySupport(14).connectivitySupport(8).activitySupport(20).achievementSupport(18).build(),
                    GatheringCategoryDetail.builder().name("족구").gatheringCategory(sports).empathySupport(15).connectivitySupport(9).activitySupport(18).achievementSupport(16).build(),
                    GatheringCategoryDetail.builder().name("축구/풋살").gatheringCategory(sports).empathySupport(14).connectivitySupport(20).activitySupport(20).achievementSupport(18).build(),
                    GatheringCategoryDetail.builder().name("탁구").gatheringCategory(sports).empathySupport(14).connectivitySupport(9).activitySupport(7).achievementSupport(15).build(),

                    GatheringCategoryDetail.builder().name("책/독서").gatheringCategory(book).empathySupport(24).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("인문학").gatheringCategory(book).empathySupport(24).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("심리학").gatheringCategory(book).empathySupport(24).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("철학").gatheringCategory(book).empathySupport(24).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("역사").gatheringCategory(book).empathySupport(24).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("시사/경제").gatheringCategory(book).empathySupport(18).connectivitySupport(5).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("작문/글쓰기").gatheringCategory(book).empathySupport(28).connectivitySupport(5).activitySupport(2).achievementSupport(5).build(),

                    GatheringCategoryDetail.builder().name("가구/목공예").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(7).achievementSupport(25).build(),
                    GatheringCategoryDetail.builder().name("가죽공예").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(6).achievementSupport(25).build(),
                    GatheringCategoryDetail.builder().name("도자/점토공예").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(6).achievementSupport(25).build(),
                    GatheringCategoryDetail.builder().name("미술/그림").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(4).achievementSupport(25).build(),
                    GatheringCategoryDetail.builder().name("소품공예").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(4).achievementSupport(23).build(),
                    GatheringCategoryDetail.builder().name("자수/뜨개질").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(3).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("천연비누/화장품").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(3).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("캔들/디퓨저").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(3).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("플라워아트").gatheringCategory(craft).empathySupport(15).connectivitySupport(2).activitySupport(3).achievementSupport(21).build(),

                    GatheringCategoryDetail.builder().name("농구KBL").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(6).achievementSupport(4).build(),
                    GatheringCategoryDetail.builder().name("배구V리그").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(2).achievementSupport(4).build(),
                    GatheringCategoryDetail.builder().name("야구KBO").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(17).achievementSupport(4).build(),
                    GatheringCategoryDetail.builder().name("축구K리그").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(15).achievementSupport(4).build(),
                    GatheringCategoryDetail.builder().name("해외축구").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(10).achievementSupport(4).build(),
                    GatheringCategoryDetail.builder().name("e스포츠").gatheringCategory(watching).empathySupport(13).connectivitySupport(8).activitySupport(2).achievementSupport(4).build(),

                    GatheringCategoryDetail.builder().name("강아지").gatheringCategory(pet).empathySupport(23).connectivitySupport(3).activitySupport(6).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("고양이").gatheringCategory(pet).empathySupport(23).connectivitySupport(3).activitySupport(6).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("물고기").gatheringCategory(pet).empathySupport(15).connectivitySupport(3).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("파충류").gatheringCategory(pet).empathySupport(15).connectivitySupport(3).activitySupport(2).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("설치류").gatheringCategory(pet).empathySupport(15).connectivitySupport(3).activitySupport(2).achievementSupport(3).build(),

                    GatheringCategoryDetail.builder().name("국악/사물놀이").gatheringCategory(music).empathySupport(14).connectivitySupport(9).activitySupport(5).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("기타/베이스").gatheringCategory(music).empathySupport(14).connectivitySupport(8).activitySupport(4).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("노래/보컬").gatheringCategory(music).empathySupport(14).connectivitySupport(7).activitySupport(4).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("바이올린").gatheringCategory(music).empathySupport(14).connectivitySupport(8).activitySupport(4).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("밴드/합주").gatheringCategory(music).empathySupport(14).connectivitySupport(16).activitySupport(5).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("오카리나").gatheringCategory(music).empathySupport(14).connectivitySupport(8).activitySupport(4).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("재즈").gatheringCategory(music).empathySupport(16).connectivitySupport(8).activitySupport(4).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("찬양/CCM").gatheringCategory(music).empathySupport(23).connectivitySupport(17).activitySupport(5).achievementSupport(24).build(),
                    GatheringCategoryDetail.builder().name("클래식").gatheringCategory(music).empathySupport(16).connectivitySupport(10).activitySupport(5).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("피아노").gatheringCategory(music).empathySupport(14).connectivitySupport(6).activitySupport(4).achievementSupport(24).build(),

                    GatheringCategoryDetail.builder().name("국내여행").gatheringCategory(travel).empathySupport(27).connectivitySupport(6).activitySupport(15).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("낚시").gatheringCategory(travel).empathySupport(24).connectivitySupport(7).activitySupport(11).achievementSupport(10).build(),
                    GatheringCategoryDetail.builder().name("등산").gatheringCategory(travel).empathySupport(24).connectivitySupport(11).activitySupport(17).achievementSupport(17).build(),
                    GatheringCategoryDetail.builder().name("산책/트래킹").gatheringCategory(travel).empathySupport(26).connectivitySupport(11).activitySupport(17).achievementSupport(11).build(),
                    GatheringCategoryDetail.builder().name("캠핑/백패킹").gatheringCategory(travel).empathySupport(26).connectivitySupport(7).activitySupport(15).achievementSupport(9).build(),
                    GatheringCategoryDetail.builder().name("해외여행").gatheringCategory(travel).empathySupport(26).connectivitySupport(6).activitySupport(15).achievementSupport(3).build(),

                    GatheringCategoryDetail.builder().name("고궁/문화재탐방").gatheringCategory(culture).empathySupport(13).connectivitySupport(6).activitySupport(9).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("공연/연극").gatheringCategory(culture).empathySupport(16).connectivitySupport(6).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("뮤지컬/오페라").gatheringCategory(culture).empathySupport(16).connectivitySupport(6).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("영화").gatheringCategory(culture).empathySupport(16).connectivitySupport(6).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("전시회").gatheringCategory(culture).empathySupport(17).connectivitySupport(6).activitySupport(7).achievementSupport(3).build(),

                    GatheringCategoryDetail.builder().name("라틴댄스").gatheringCategory(dance).empathySupport(14).connectivitySupport(14).activitySupport(9).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("밸리댄스").gatheringCategory(dance).empathySupport(14).connectivitySupport(14).activitySupport(9).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("사교댄스").gatheringCategory(dance).empathySupport(16).connectivitySupport(18).activitySupport(9).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("스윙댄스").gatheringCategory(dance).empathySupport(14).connectivitySupport(14).activitySupport(9).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("재즈댄스").gatheringCategory(dance).empathySupport(14).connectivitySupport(13).activitySupport(8).achievementSupport(22).build(),
                    GatheringCategoryDetail.builder().name("한국무용").gatheringCategory(dance).empathySupport(14).connectivitySupport(10).activitySupport(8).achievementSupport(21).build(),
                    GatheringCategoryDetail.builder().name("현대무용").gatheringCategory(dance).empathySupport(14).connectivitySupport(10).activitySupport(8).achievementSupport(21).build(),

                    GatheringCategoryDetail.builder().name("결혼").gatheringCategory(social).empathySupport(20).connectivitySupport(8).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("나이").gatheringCategory(social).empathySupport(29).connectivitySupport(15).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("맛집/미식회").gatheringCategory(social).empathySupport(24).connectivitySupport(9).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("와인/커피/차").gatheringCategory(social).empathySupport(24).connectivitySupport(12).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("지역").gatheringCategory(social).empathySupport(26).connectivitySupport(11).activitySupport(4).achievementSupport(3).build(),
                    GatheringCategoryDetail.builder().name("파티").gatheringCategory(social).empathySupport(23).connectivitySupport(11).activitySupport(4).achievementSupport(3).build(),

                    GatheringCategoryDetail.builder().name("디지털카메라").gatheringCategory(photo).empathySupport(15).connectivitySupport(5).activitySupport(12).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("영상제작").gatheringCategory(social).empathySupport(15).connectivitySupport(5).activitySupport(12).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("필름카메라").gatheringCategory(social).empathySupport(15).connectivitySupport(5).activitySupport(12).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("DSLR").gatheringCategory(social).empathySupport(15).connectivitySupport(5).activitySupport(12).achievementSupport(15).build(),

                    GatheringCategoryDetail.builder().name("한식").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("양식").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("중식").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("일식").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("제과제빵").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build(),
                    GatheringCategoryDetail.builder().name("주류제조/칵테일").gatheringCategory(cook).empathySupport(14).connectivitySupport(2).activitySupport(3).achievementSupport(15).build()
            ));
        }
    }
}
