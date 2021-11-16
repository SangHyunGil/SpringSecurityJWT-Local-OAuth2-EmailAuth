package project.SangHyun.advice.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
