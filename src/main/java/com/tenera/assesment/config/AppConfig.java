package com.tenera.assesment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean("isoCountryCodes")
    public Map<String,String> isoCountryCodes(){
       Map<String,String> isoCountryCodesMap = new HashMap<>();
        Arrays.stream(Locale.getISOCountries())
               .forEach(country->{Locale locale = new Locale("",country);
               isoCountryCodesMap.put(locale.getISO3Country(),country);
               });
        return isoCountryCodesMap;


    }
}
