package ru.nsk.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.nsk.CryptoTool;

@Configuration
public class RestServiceConfiguration {
    @Value("${salt}")
    private String salt;

    public CryptoTool getCryptoTool(){
        return new CryptoTool(salt);
    }

}