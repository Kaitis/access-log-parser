package com.ef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class Parser  {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Parser.class);

        List<String> argsList = new ArrayList<>();

        Properties properties = new Properties();

        for(String s : args) {
            if (s.startsWith("--")){

                String[] keyValue = s.substring(2).split("=");
                properties.put(keyValue[0], keyValue.length == 2 ? keyValue[1] : "0");

            }else{

                argsList.add(s);
            }
        }

        application.setDefaultProperties(properties);
        application.run(argsList.toArray(new String[0]));
    }


}
