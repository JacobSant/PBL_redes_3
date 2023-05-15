package br.uefs.pbl_redes_3.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberAccountGenerationConfiguration {
    @Bean
    public NumberAccountGenerator numberAccountGenerator(){
        return new NumberAccountGenerator();
    }
}
