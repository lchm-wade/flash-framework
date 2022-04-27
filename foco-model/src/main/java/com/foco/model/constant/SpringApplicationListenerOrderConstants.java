package com.foco.model.constant;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2022/01/04 15:25
 * @since foco2.1.0
 */
public interface SpringApplicationListenerOrderConstants {
    int LOGGING_ORDER = FocoConstants.HIGHEST_PRECEDENCE + 19;
    int SPRING_CONTEXT_ORDER = FocoConstants.HIGHEST_PRECEDENCE + 30;
}
