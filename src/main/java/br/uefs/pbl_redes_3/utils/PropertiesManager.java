package br.uefs.pbl_redes_3.utils;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = "classpath:application.properties")
public class PropertiesManager {
    private final Environment env;

    public PropertiesManager(final Environment env) {
        this.env = env;
    }

    public String getProperty(String key){
        return env.getProperty(key);
    }
}
