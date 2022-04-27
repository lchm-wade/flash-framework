package com.foco.context.logging;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.util.OptionHelper;
import cn.hutool.core.util.StrUtil;
import com.foco.context.core.Env;
import com.foco.context.core.EnvHelper;
import com.foco.model.constant.FocoConstants;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.xml.sax.Attributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/09 20:24
 * @since foco2.1.0
 */
public class FocoPropertiesAction extends Action implements InPlayListener {

    private final Environment environment;

    private int depth = 0;

    private boolean acceptsProfile;

    private List<SaxEvent> events;

    public FocoPropertiesAction(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
        this.depth++;
        if (this.depth != 1) {
            return;
        }
        ic.pushObject(this);
        this.acceptsProfile = acceptsProfiles(ic, attributes);
        this.events = new ArrayList<>();
        ic.addInPlayListener(this);
    }

    /**
     * 优化从foco.env中获取环境标识
     * @param ic
     * @param attributes
     * @return
     */
    private boolean acceptsProfiles(InterpretationContext ic, Attributes attributes) {
        String propertyName = attributes.getValue(NAME_ATTRIBUTE);
        String property= environment.getProperty(FocoConstants.FOCO_ENV);
        if (StrUtil.isBlank(property)) {
            property = environment.getProperty(FocoConstants.CURRENT_ENV);
        }
        if(StrUtil.isBlank(property)){
            property= Env.LOCAL.getEnv();
        }
        propertyName = OptionHelper.substVars(propertyName, ic, this.context);
        if (propertyName.startsWith("!")) {
            return !("!" + property).equals(propertyName)&&isWindow(attributes);
        }
        return propertyName.equals(property);
    }
    private boolean isWindow( Attributes attributes){
        String osName = System.getProperty("os.name").toLowerCase();
        String osNameVal = attributes.getValue(VALUE_ATTRIBUTE);
        if(osNameVal.startsWith("!")){
            return !("!"+osName).startsWith(osNameVal);
        }
        return osName.startsWith(osNameVal);
    }
    @Override
    public void end(InterpretationContext ic, String name) throws ActionException {
        this.depth--;
        if (this.depth != 0) {
            return;
        }
        ic.removeInPlayListener(this);
        verifyAndPop(ic);
        if (this.acceptsProfile) {
            addEventsToPlayer(ic);
        }
    }

    private void verifyAndPop(InterpretationContext ic) {
        Object o = ic.peekObject();
        Assert.state(o != null, "Unexpected null object on stack");
        Assert.isInstanceOf(FocoPropertiesAction.class, o, "logback stack error");
        Assert.state(o == this, "ProfileAction different than current one on stack");
        ic.popObject();
    }

    private void addEventsToPlayer(InterpretationContext ic) {
        Interpreter interpreter = ic.getJoranInterpreter();
        this.events.remove(0);
        this.events.remove(this.events.size() - 1);
        interpreter.getEventPlayer().addEventsDynamically(this.events, 1);
    }

    @Override
    public void inPlay(SaxEvent event) {
        this.events.add(event);
    }

}
