package com.an.bitcoin.protocol;

/**
 * @ClassName VerificationException
 * @Description VerificationException
 * @Author an
 * @Date 2019/5/15 下午5:38
 * @Version 1.0
 */
public class ScriptException extends RuntimeException {

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptException(Throwable cause) {
        super(cause);
    }

    public ScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static class EmptyInputsOrOutputs extends ScriptException {
        public EmptyInputsOrOutputs() {
            super("Transaction had no inputs or no outputs.");
        }
    }

    public static class DuplicatedOutPoint extends ScriptException {
        public DuplicatedOutPoint() {
            super("Duplicated outpoint");
        }
    }

}
