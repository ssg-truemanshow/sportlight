package com.tms.sportlight.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    COURSE_IMG("course_img/"),
    COURSE_THUMB("course_thumb/"),
    USER_PROFILE_ICON("user_profile/"),
    COMMUNITY_PROFILE_ICON("community_profile/"),
    HOST_CERTIFICATION_FILE("host_certification/");

    private final String path;
}
