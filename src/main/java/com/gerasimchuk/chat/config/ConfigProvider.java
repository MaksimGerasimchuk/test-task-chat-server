/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public final class ConfigProvider {

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final Map<String, Object> PROPS;

    static {
        final Yaml yaml = new Yaml();
        InputStream inputStream = ConfigProvider.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        PROPS = Collections.unmodifiableMap(yaml.load(inputStream));
    }

    private ConfigProvider() {
        throw new IllegalStateException("Not to be instantiated");
    }

    public static Map<String, Object> getProps() {
        return PROPS;
    }


}
