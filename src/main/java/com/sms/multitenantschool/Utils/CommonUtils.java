package com.sms.multitenantschool.Utils;

import java.util.UUID;

public class CommonUtils {


    public static String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean isTokenValid(String token) {
        return token != null && !token.isEmpty(); // Can be extended for real verification logic
    }

}
