package silverpotion.postserver.gathering.domain;

public enum Status {
    WAIT,   //가입대기
    ACTIVATE,   //활성화
    DEACTIVATE,  //비활성화(탈퇴 or 해체된모임)
    BAN //추방
}
