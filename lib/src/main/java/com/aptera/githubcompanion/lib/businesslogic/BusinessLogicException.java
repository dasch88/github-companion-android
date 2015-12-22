package com.aptera.githubcompanion.lib.businesslogic;

/**
 * Created by daschliman on 12/21/2015.
 */
public class BusinessLogicException extends Exception {

    public BusinessLogicException() {
        super();
    }
    public BusinessLogicException(String message) {
        super(message);
    }
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
    public BusinessLogicException(Throwable cause) {
        super(cause);
    }
    protected BusinessLogicException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static BusinessLogicException BuildWrapper(String action, Exception baseException) {
        String wholeMsg = (action != null ? "An error occurred while " + action + "." : "An error occurred.");
        return new BusinessLogicException(wholeMsg, baseException);
    }


}
