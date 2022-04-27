package com.foco.internation.parser;

import com.foco.internation.LocaleEntity;

public interface PropertiesParser {
    String getProperty(LocaleEntity localeEntity, String key);
}
