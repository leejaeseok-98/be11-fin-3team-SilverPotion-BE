package silverpotion.userserver.healthData.domain;

public enum DataType {
    //일별데이터, 주간 평균 데이터, 월별 평균 데이터
//    숫자가 작을수록 우선순위가 높게
//    열거형상수들을 정의하면서 ()생성자에 정수 값을 전달하고 있는 형태
    DAY(0),
    WEEKAVG(1),
    MONTHAVG(2);

//    각 enum값에 부여된 priority값을 저장하는 필드
    private final int priority;
//  enum생성자 각 상수를 선언할 때 넘긴 숫자값(0,1,2)을 이 생성자로 받아서 저장
    DataType(int priority) {
        this.priority = priority;
    }


    public int getPriority(){
        return priority;
    }
}
