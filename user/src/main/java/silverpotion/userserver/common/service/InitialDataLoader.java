package silverpotion.userserver.common.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.Role;
import silverpotion.userserver.user.domain.Sex;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;
import silverpotion.userserver.user.service.UserService;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createAccounts();
    }

    public void createAccounts(){

        if(!userRepository.findByLoginIdAndDelYN("user",DelYN.N).isPresent()){
            User user = User.builder()
                    .sex(Sex.FEMALE)
                    .role(Role.USER)
                    .phoneNumber("01076084578")
                    .name("홍진영")
                    .birthday("19500818")
                    .loginId("user")
                    .password(passwordEncoder.encode("12341234"))
                    .nickName("홍홍홍")
                    .email("riri")
                    .address("010")
                    .streetAddress("010")
                    .detailAddress("010")
                    .build();

            userRepository.save(user);
        }
        if(!userRepository.findByLoginIdAndDelYN("admin",DelYN.N).isPresent()){
            User admin = User.builder()
                    .sex(Sex.FEMALE)
                    .role(Role.ADMIN)
                    .phoneNumber("01055881234")
                    .name("신림00년생여자")
                    .birthday("19990102")
                    .loginId("admin")
                    .password(passwordEncoder.encode("12341234"))
                    .nickName("홍홍홍")
                    .email("riri")
                    .address("010")
                    .streetAddress("010")
                    .detailAddress("010")
                    .build();

            userRepository.save(admin);
        }
        if(!userRepository.findByLoginIdAndDelYN("user1",DelYN.N).isPresent()){
            User user1 = User.builder()
                    .sex(Sex.FEMALE)
                    .role(Role.USER)
                    .phoneNumber("01095551234")
                    .name("고등학생")
                    .birthday("20000401")
                    .loginId("user1")
                    .password(passwordEncoder.encode("12341234"))
                    .nickName("보딩고지")
                    .email("qwer")
                    .address("010")
                    .streetAddress("010")
                    .detailAddress("010")
                    .build();
            userRepository.save(user1);
        }


    }
}
