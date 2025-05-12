package silverpotion.userserver.admin.utils;

import org.bouncycastle.pqc.crypto.util.PQCOtherInfoGenerator;

public class MaskingUtils {
    public static String maskName(String name){
        if (name == null || name.length() < 2) return name;
        return name.substring(0,1) + "*" + name.substring(name.length()-1);
    }

    public static String maskEmail(String email){
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String id = parts[0];
        String domain = parts[1];
        int maskLength = Math.max(1, id.length() - 2);

        return id.substring(0, 1) + "*".repeat(maskLength) + id.substring(id.length() - 1) + "@" + domain;
    }

    public static String maskPhoneNumber(String phoneNumber){
        if (phoneNumber == null || phoneNumber.length() < 7) return phoneNumber;
        return phoneNumber.substring(0,3) + "***" + phoneNumber.substring(phoneNumber.length()-4);
    }

    public static String maskBirthday(String birthday){
        if (birthday == null || birthday.length() < 4) return birthday;
        return birthday.substring(0,2) + "***" + birthday.substring(birthday.length()-2);
    }

    public static String maskLoginId(String loginId){
        if (loginId == null || loginId.length() < 4) return loginId;
        return loginId.substring(0,2) + "***" + loginId.substring(loginId.length()-2);
    }
}
