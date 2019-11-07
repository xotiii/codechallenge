package com.danchua.codechallenge.network.utils;

public class ErrorUtils {
    public static final String ERROR_MESSAGE = "A problem has occured. Please try again.";

    public static String getErrorMessage(Throwable error) {
        return ERROR_MESSAGE;
    }

}
