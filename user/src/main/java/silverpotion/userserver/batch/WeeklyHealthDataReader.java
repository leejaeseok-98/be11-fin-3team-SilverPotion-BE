package silverpotion.userserver.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.Iterator;
import java.util.List;

@Component
public class WeeklyHealthDataReader implements ItemReader<User> { //ItemReader<T> 읽어올 데이터 타입 T를 지정

    private final UserRepository userRepository;
    private Iterator<User> userIterator; //Iterator는 리스트 등의 컬렉션에 대해 반복해서 하나씩 꺼낼 수 있게 해주는 도구

    public WeeklyHealthDataReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    //스프링 배치는 이 read()를 반복해서 실행하면서 데이터를 하나씩 읽어가므로 한 번 호출될 때 마다 한 명의 유저를 반환
    public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
       //userIterator를 초기화하기 위한 코드로 제일 처음 한 번실행할때만 초기화 되면 되니까 userIterator == null이라는 조건 담
        if(userIterator == null){
            List<User> users = userRepository.findAll();
            userIterator = users.iterator(); //전체 유저리스트에서 하나씩 꺼내기 위해 서 iterator를 만들어줌
        }
        return userIterator.hasNext() ? userIterator.next() : null; //꺼낼 다음 유저가 있는지 확인하고 있으면 다음거 꺼내고 없으면 null리턴

    }
}
