package silverpotion.postserver.gatheringVector.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.postserver.gatheringVector.domain.GatheringVector;
import silverpotion.postserver.gatheringVector.dtos.GatheringVectorForUserServiceDto;
import silverpotion.postserver.gatheringVector.repository.GatheringVectorRepository;
import java.util.List;

@Service
@Transactional
public class GatheringVectorService {

    private final GatheringVectorRepository gatheringVectorRepository;

    public GatheringVectorService(GatheringVectorRepository gatheringVectorRepository) {
        this.gatheringVectorRepository = gatheringVectorRepository;
    }


    public List<GatheringVectorForUserServiceDto> getAllGatherings(){
        List<GatheringVector> gatheringVectors = gatheringVectorRepository.findAll();
        List<GatheringVectorForUserServiceDto> dtoList = gatheringVectors.stream().map(g->g.toDtoForUserService()).toList();
        return dtoList;

    }
}
