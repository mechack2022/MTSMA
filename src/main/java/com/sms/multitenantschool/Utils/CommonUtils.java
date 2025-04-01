package com.sms.multitenantschool.Utils;

import java.util.UUID;

public class CommonUtils {


    public static String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public static UUID generateNewUuid() {
        return UUID.randomUUID();
    }

    public static boolean isTokenValid(String token) {
        return token != null && !token.isEmpty(); // Can be extended for real verification logic
    }

    public static String generateStudentIdNumber(String tenantAbbr, String firstName, String middleName, String lastName) {
        if (tenantAbbr == null || tenantAbbr.length() != 5) {
            throw new IllegalArgumentException("Tenant abbreviation must be exactly 5 characters long.");
        }
        String firstInitial = (firstName != null && !firstName.isEmpty()) ? firstName.substring(0, 1).toUpperCase() : "X";
        String middleInitial = (middleName != null && !middleName.isEmpty()) ? middleName.substring(0, 1).toUpperCase() : "X";
        String lastInitial = (lastName != null && !lastName.isEmpty()) ? lastName.substring(0, 1).toUpperCase() : "X";
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 10000);
        return tenantAbbr.toUpperCase() + "-" + firstInitial + middleInitial + lastInitial + "-" + uniqueSuffix;
    }


}
