package com.barbutov.network_of_giving.util;

public class Constants {
    public static final String CHARITIES_FILE_NAME = "charities";
    public static final String CHARITY_FILE_NAME = "charity";
    public static final String PROFILE_FILE_NAME = "profile";
    public static final String LOGIN_FILE_NAME = "login";
    public static final String REGISTER_FILE_NAME = "register";
    public static final String ERROR_TEMPLATE_NAME = "error";
    public static final String CHARITY_DETAILS_TEMPLATE_NAME = "charityDetails";
    public static final String CREATE_CHARITY_TEMPLATE_NAME = "createCharity";
    public static final String EDIT_CHARITY_TEMPLATE_NAME = "editCharity";

    public static final String NULL_CHARITY = "Charity can't be null";
    public static final String NULL_USER = "User can't be null";
    public static final String NULL_OR_EMPTY_FIELDS = "Input contains null or empty fields";

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
