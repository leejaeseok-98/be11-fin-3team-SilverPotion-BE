package silverpotion.postserver.gatheringVector.domain;

import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gatheringVector.dtos.GatheringVectorCreateDto;

public class GatheringVectorUtils {

    // 스코어는 현재점수, 맥스는 그 항목의 최대 점수
//    정규화하는 함수
    public static double normalize(double score, int max){
        if(max<=0.0) return 0.0;
        return Math.min(Math.max(score / (double)max, 0.0),1.0);
    }

//    모임의 정규화된 벡터값 만들어주는 함수
    public static GatheringVectorCreateDto makingGatheringVector(Gathering gathering){
        double empathySupport = 0;
        double acheivementSupport =0;
        double connectivitySupport =0;
        double energySupport=0;

        if(gathering.getGatheringDetails().size()==1){
            empathySupport = gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getEmpathySupport();
            acheivementSupport =gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getAchievementSupport();
            connectivitySupport = gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getConnectivitySupport();
            energySupport =gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getActivitySupport();
        } else{ //세부카테고리가 2개일때
            empathySupport = (gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getEmpathySupport()
                    +gathering.getGatheringDetails().get(1).getGatheringCategoryDetail().getEmpathySupport()) / 2.0;
            acheivementSupport =(gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getAchievementSupport()
                    +gathering.getGatheringDetails().get(1).getGatheringCategoryDetail().getAchievementSupport())/2.0;
            connectivitySupport = (gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getConnectivitySupport()
                    +gathering.getGatheringDetails().get(1).getGatheringCategoryDetail().getConnectivitySupport())/2.0;
            energySupport =(gathering.getGatheringDetails().get(0).getGatheringCategoryDetail().getActivitySupport()
                    +gathering.getGatheringDetails().get(1).getGatheringCategoryDetail().getActivitySupport())/2.0;
        }

       return GatheringVectorCreateDto.builder().empathySupport(empathySupport).achievementSupport(acheivementSupport).connectivitySupport(connectivitySupport).energySupport(energySupport).build();
    }


}
