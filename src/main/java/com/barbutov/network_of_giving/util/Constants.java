package com.barbutov.network_of_giving.util;

public class Constants {
    public static final String NULL_CHARITY = "Charity can't be null";
    public static final String NULL_USER = "User can't be null";
    public static final String NULL_FIELDS = "Input contains null fields that can't be null";
    public static final String VALID = "VALID";

    public static final int CHARITY_MAX_DESCRIPTION_LENGTH = 1000;
    public static final int CHARITY_NAME_MAX_LENGTH = 60;
    public static final int USER_FIRST_NAME_MAX_LENGTH = 20;
    public static final int USER_LAST_NAME_MAX_LENGTH = 20;
    public static final int USER_USERNAME_MAX_LENGTH = 25;
    public static final int USER_PASSWORD_MAX_LENGTH = 60;
    public static final int USER_LOCATION_MAX_LENGTH = 200;
    public static final int USER_AGE_MAX_VALUE = 150;
    public static final int USER_AGE_MIN_VALUE = 1;

    private Constants() {
    }
}
