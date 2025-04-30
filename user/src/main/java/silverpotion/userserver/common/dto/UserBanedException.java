package silverpotion.userserver.common.dto;

public class UserBanedException extends RuntimeException {
    public UserBanedException(String message) {
        super(message);
    }
}
