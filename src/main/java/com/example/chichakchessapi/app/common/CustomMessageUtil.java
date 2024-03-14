package com.example.chichakchessapi.app.common;

public class CustomMessageUtil {
    public static final String GENERAL_NOT_VALID_UUID = "Provided id is not valid UUID";
    public static final String GENERAL_INFORMATION_NOT_FOUND = "Information not found";
    public static final String GENERAL_PROVIDED_ID = "Provided ID: ";

    public static final String PLAYER_DOES_NOT_EXIST = "Player doesn't exist";
    public static final String PLAYER_IS_NOT_ADMIN = "You do not have admin rights";
    public static final String PLAYER_ALREADY_EXIST = "This email is already associated with other account";
    public static final String PLAYER_WRONG_PASSWORD = "Wrong password";
    public static final String PLAYER_ID = "Player ID: ";
    public static final String PLAYER_EMAIL = "Player email: ";

    public static final String GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER = "You cannot enroll to a game as other player";

    private CustomMessageUtil() {
        throw new IllegalStateException("Utility class");
    }
}