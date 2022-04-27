package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.action.NOPAction;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import com.foco.context.logging.FocoPropertiesAction;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.core.env.Environment;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 新增自定义logback标签解析FocoPropertiesAction
 * @date 2021/12/09 22:03
 * @since foco2.1.0
 */
public class FocoSpringBootJoranConfigurator extends JoranConfigurator {

    private LoggingInitializationContext initializationContext;

    public FocoSpringBootJoranConfigurator(LoggingInitializationContext initializationContext) {
        this.initializationContext = initializationContext;
    }
    @Override
    public void addInstanceRules(RuleStore rs) {
        super.addInstanceRules(rs);
        Environment environment = this.initializationContext.getEnvironment();
        rs.addRule(new ElementSelector("configuration/springProperty"), new SpringPropertyAction(environment));
        rs.addRule(new ElementSelector("*/springProfile"), new SpringProfileAction(environment));
        rs.addRule(new ElementSelector("*/springProfile/*"), new NOPAction());
        rs.addRule(new ElementSelector("*/focoProfile"), new FocoPropertiesAction(environment));
        rs.addRule(new ElementSelector("*/focoProfile/*"), new NOPAction());
    }

}

