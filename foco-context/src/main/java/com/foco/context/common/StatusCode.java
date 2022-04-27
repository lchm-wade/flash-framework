package com.foco.context.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public interface StatusCode {
    String getCode();

    String getMessage();
    default void setMessage(String message){}

    static List<StatusCode> toKVPair(Class enumClass) {
        EnumSet enums = EnumSet.allOf(enumClass);
        List<StatusCode> result = new ArrayList<>();
        for (Object obj : enums) {
            if (obj instanceof StatusCode) {
                result.add(new StatusCode() {
                    @Override
                    public String getCode() {
                        return ((StatusCode) obj).getCode();
                    }
                    @Override
                    public String getMessage() {
                        return ((StatusCode) obj).getMessage();
                    }

                    @Override
                    public void setMessage(String message) {
                        ((StatusCode) obj).setMessage(message);
                    }

                });
            }
        }
        return result;
    }
}
