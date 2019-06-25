package com.ef.parser.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        // This way spring batch will not store any metadata (and create the needed table structure for them)
        // initialize will use a Map based JobRepository (instead of database)
    }

}