package com.example.chichakchessapi.app.common;

public class CustomMessageUtil {
    public static final String GENERAL_NOT_VALID_UUID = "Provided id is not valid UUID";
    public static final String GENERAL_PERSISTENCE_OPERATION_FAILED = "Persistence operation failed";
    public static final String GENERAL_PROVIDED_ID = "Provided ID: ";

    public static final String PLAYER_DOES_NOT_EXIST = "Player doesn't exist";
    public static final String PLAYER_IS_NOT_ADMIN = "You do not have admin rights";
    public static final String PLAYER_ALREADY_EXIST = "This email is already associated with other account";
    public static final String PLAYER_IS_ALREADY_IN_GAME = "Player is already in game, player can be max at one game at the time";
    public static final String PLAYER_WRONG_PASSWORD = "Wrong password";
    public static final String PLAYER_IS_NOT_IN_GAME = "Player doesn't take part in the provided game";
    public static final String PLAYER_ALONE_IN_GAME = "Player is alone in game";
    public static final String PLAYER_ID = "Player ID: ";
    public static final String PLAYER_EMAIL = "Player email: ";

    public static final String GAME_CANNOT_ENROLL_GAME_AS_OTHER_PLAYER = "You cannot enroll to a game as other player";
    public static final String GAME_DOES_NOT_EXIST = "Game doesn't exist";
    public static final String GAME_ID = "Game ID: ";

    private CustomMessageUtil() {
        throw new IllegalStateException("Utility class");
    }
}