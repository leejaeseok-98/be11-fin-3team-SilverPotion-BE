package silverpotion.userserver.careRelation.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.careRelation.domain.LinkStatus;
import silverpotion.userserver.careRelation.dtos.*;
import silverpotion.userserver.careRelation.repository.CareRelationRepository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CareRelationService {
    private final CareRelationRepository careRelationRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;

    public CareRelationService(CareRelationRepository careRelationRepository, UserRepository userRepository, NotificationProducer notificationProducer) {
        this.careRelationRepository = careRelationRepository;
        this.userRepository = userRepository;
        this.notificationProducer = notificationProducer;
    }

//    1.관계요청 보내기(이때 로그인 아이디는 보호자가 될 유저)
    public void sendCareLink(CareRelationCreateDto dto, String loginId){
        User protector = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다1"));
        User dependent = userRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(), DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다.2"));
        if(protector.getHealingPotion()<1){
            throw new IllegalArgumentException("연결을 위해서는 힐링포션 1개가 필요합니다");
        }
        protector.updateMyHealingPotion(-1); //연결할때 힐링포션 1개소모(단 연결이 이루어지지않으면 돌려받음)
        CareRelation careRelation = careRelationRepository.save(dto.toEntityFromCreateDto(protector,dependent));
        // 🔔 알림 발송
        NotificationMessageDto notification = NotificationMessageDto.builder()
                .loginId(dependent.getLoginId()) // 피보호자에게 발송
                .title("보호 요청 도착")
                .content(protector.getNickName() + "님이 보호 관계 요청을 보냈습니다.")
                .type("CARE_REQUEST")
                .referenceId(careRelation.getId()) // 상세보기 등 라우팅에 활용 가능
                .build();

        notificationProducer.sendNotification(notification);
    }

//    1-2.관계요청 보내기(이때 로그인 아이디는 피보호자가 될 유저)
    public void sendCareLinkFromDependent(CareRelationCreateDto dto, String loginId){
        User dependent = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        User protector = userRepository.findByPhoneNumberAndDelYN(dto.getPhoneNumber(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다2"));
        if(dependent.getHealingPotion()<1){
            throw new IllegalArgumentException("연결을 위해서는 힐링포션 1개가 필요합니다");
        }
        dependent.updateMyHealingPotion(-1);
        CareRelation careRelation = careRelationRepository.save(dto.toEntityFromCreateDto(protector,dependent));
        // 🔔 알림 발송
        NotificationMessageDto notification = NotificationMessageDto.builder()
                .loginId(protector.getLoginId()) // 피보호자에게 발송
                .title("보호 요청 도착")
                .content(protector.getNickName() + "님이 보호 관계 요청을 보냈습니다.")
                .type("CARE_REQUEST")
                .referenceId(careRelation.getId()) // 상세보기 등 라우팅에 활용 가능
                .build();

        notificationProducer.sendNotification(notification);
    }

//    2.내게 온 연결 요청 조회(이때 로그인 아이디는 피보호자가 될 유저)
    public List<CareRelationListDto> checkLink(String loginId){
       User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        System.out.println(user.getName());
       List<CareRelation> receivedList = careRelationRepository.findByDependentIdAndLinkStatus(user.getId(), LinkStatus.PENDING);
       return receivedList.stream().map(c->c.toRecievedListFromEntity()).toList();
    }

//    3.연결 요청 수락 혹은 거절(이때 로그인 아이디는 피보호자가 되는 유저)
    public Map<Long,String> acceptOrNot(CareRelationAcceptOrNotDto dto,String loginId){
        Map<Long,String> map = new HashMap<>();// 키값에 관계요청id 밸류값에 관계를 수락했는지 거절했는지

        CareRelation careRelation = careRelationRepository.findByIdAndLinkStatus(dto.getCareRelationId(),LinkStatus.PENDING).orElseThrow(()->new EntityNotFoundException("관계요청이 없습니다"));
        //loginUser = 피보호자로 요청받은 사람 senderUser= 보호자로서 요청 제안한 사람
        User loginUser = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        User senderUser = userRepository.findByLoginIdAndDelYN(dto.getSenderId(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        if(dto.getYesOrNo().equals("yes")){
            careRelation.changeMyStatus(dto.getYesOrNo()); //status가 accepted로 바뀜
//            loginUser.getAsDependents().add(careRelation);
//            senderUser.getAsProtectors().add(careRelation); //이 두개 코드가 사실상 의미가 없다. careRelation이 생성될때 양방향 매핑으로 인해 자동으로 먼저 리스트에 추가되기때문
            map.put(careRelation.getId(),"accept");
        } else{
            careRelation.changeMyStatus(dto.getYesOrNo()); //status가 rejected로 바뀜
            map.put(careRelation.getId(),"rejected");
            senderUser.updateMyHealingPotion(1); // 연결이 거부당하면 다시 힐링포션 1개 요청자에게 돌려줌
        }

        return map;
    }


//    4.보호자 연결 끊기
    public String disconnect(CareRelationDisconnectDto dto, String loginId){
//        dto의 id는 보호자의 id
//        로그인한 유저는 피보호자
        User loginUser = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        User protector = userRepository.findByIdAndDelYN(dto.getId(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        String x="";
        List<CareRelation> myRelation = loginUser.getAsDependents();
        for(CareRelation c: myRelation ){
            if(c.getProtector().getId().equals(protector.getId())){
                c.disconnectStatus();
                x=c.getProtector().getName();
            }
        }
        return x; //연결이 끊긴 보호자의 이름을 리턴
    }

//    5.피보호자 연결 끊기
    public String disconnectDependent(CareRelationDisconnectDto dto, String loginId){
//        dto의 id는 보호자의 id, 로그인한 유저는 보호자
        User loginUser = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        User dependent = userRepository.findByIdAndDelYN(dto.getId(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        String x="";
        List<CareRelation> myRelation = loginUser.getAsProtectors();
        for(CareRelation c: myRelation){
            if(c.getDependent().getId().equals(dependent.getId())){
                c.disconnectStatus();
                x=c.getDependent().getName();
            }
        }
        return x; //연결이 끊긴 보호자의 이름을 리턴
    }



}
