package com.foco.mq.exception;

import com.foco.mq.model.Msg;
import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

/**
 * @author ChenMing
 * @date 2021/11/4
 */
@SuppressWarnings("serial")
public class MessagingException extends NestedRuntimeException {

    @Nullable
    private final Msg failedMessage;


    public MessagingException(Msg message) {
        super(null, null);
        this.failedMessage = message;
    }

    public MessagingException(String description) {
        super(description);
        this.failedMessage = null;
    }

    public MessagingException(@Nullable String description, @Nullable Throwable cause) {
        super(description, cause);
        this.failedMessage = null;
    }

    public MessagingException(Msg message, String description) {
        super(description);
        this.failedMessage = message;
    }

    public MessagingException(Msg message, Throwable cause) {
        super(null, cause);
        this.failedMessage = message;
    }

    public MessagingException(Msg message, @Nullable String description, @Nullable Throwable cause) {
        super(description, cause);
        this.failedMessage = message;
    }


    @Nullable
    public Msg getFailedMessage() {
        return this.failedMessage;
    }

    @Override
    public String toString() {
        return super.toString() + (this.failedMessage == null ? ""
                : (", failedMessage=" + this.failedMessage));
    }

}