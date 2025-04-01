package com.sms.multitenantschool.enums;

import lombok.Getter;


@Getter
public enum EnrollmentStatus {
    ACTIVE("The student is currently attending classes and meeting all requirements.", true),
    PENDING("The student has started the enrollment process but has not completed all requirements.", false),
    INACTIVE("The student is not currently attending classes but has not formally withdrawn.", false),
    WITHDRAWN("The student has formally withdrawn from the institution or program.", false),
    GRADUATED("The student has completed their program and graduated.", false),
    ON_LEAVE("The student is on an approved leave of absence.", false),
    SUSPENDED_EXPELLED("The student is barred from attending due to disciplinary or academic reasons.", false);

    private final String description;
    private final boolean isActive;

    EnrollmentStatus(String description, boolean isActive) {
        this.description = description;
        this.isActive = isActive;
    }
}