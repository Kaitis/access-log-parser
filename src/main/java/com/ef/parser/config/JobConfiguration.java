package com.ef.parser.config;

import com.ef.parser.domain.AnalysisResult;
import com.ef.parser.domain.BlockedAddress;
import com.ef.parser.domain.LogEntry;
import com.ef.parser.enums.Duration;
import com.ef.parser.utils.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static com.ef.parser.utils.DateUtils.calculateEndDate;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    static final String DATE_PATTERN= "yyyy-MM-dd.HH:mm:ss";

    @Value("${accesslog: 0}")
    String path;

    @Value("${startDate}")
    @DateTimeFormat(pattern = DATE_PATTERN)
    Date startDate;

    @Value(("${duration}"))
    String duration;

    @Value(("${threshold: 0}"))
    Integer threshold;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    //CSV Processing and Persistence

    @Bean
    public FlatFileItemReader<LogEntry> reader() {

        return new FlatFileItemReaderBuilder<LogEntry>()
                .name("csvItemReader")
                .resource( path.equalsIgnoreCase("0") ? new ClassPathResource("access.log") : new FileSystemResource(FileUtils.existsAndReadable(path)))
                .delimited()
                .delimiter("|")
                .names(new String[]{"logDate", "ip", "request", "status", "userAgent"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<LogEntry>() {{
                    setTargetType(LogEntry.class);
                }})
                .build();
    }



    @Bean
    public JdbcBatchItemWriter<LogEntry> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<LogEntry>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO log_entry (logDate, ip, request, status, userAgent) VALUES (:logDate, :ip, :request, :status, :userAgent)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step logParsing(JdbcBatchItemWriter<LogEntry> writer) {
        return stepBuilderFactory.get("logParsing")
                .<LogEntry, LogEntry> chunk(10)
                .reader(reader())
                .faultTolerant()
                .skip(SQLException.class)
                .writer(writer)
                .build();
    }

    //Blocked addresses finder

    @Bean
    public JdbcCursorItemReader<AnalysisResult> analyzer(DataSource dataSource) {

        if (threshold == 0) {
            threshold = duration.equalsIgnoreCase("daily") ? Duration.DAILY.getValue() : Duration.HOURLY.getValue();
        }

        String sql = "SELECT l.ip AS ip, COUNT(l.logDate) AS total " +
                     "FROM log_entry l " +
                        "WHERE l.logDate BETWEEN ? AND ? " +
                     "GROUP BY l.ip " +
                        "HAVING COUNT(l.logDate) >= ? " +
                     "ORDER BY COUNT(l.logDate) DESC";

        Date endDate = calculateEndDate(startDate, duration);

        JdbcCursorItemReader<AnalysisResult> reader = new JdbcCursorItemReader<>();

        reader.setDataSource(dataSource);
        reader.setRowMapper(new BeanPropertyRowMapper<>(AnalysisResult.class));
        reader.setSql(sql);
        reader.setConnectionAutoCommit(true);

        reader.setPreparedStatementSetter(preparedStatement -> {

            preparedStatement.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            preparedStatement.setInt(3, threshold);

        });

        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<BlockedAddress> blockedAddressesWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<BlockedAddress>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO blocked_address (ip, comment) VALUES (:ip, :comment)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step blockedAddressProcessing(JdbcBatchItemWriter<BlockedAddress> blockedAddressesWriter,
                                         JdbcCursorItemReader<AnalysisResult> analyzer) {

        return stepBuilderFactory.get("blockedAddressProcessing")
                .<AnalysisResult, BlockedAddress> chunk(10)
                .reader(analyzer)
                .processor((ItemProcessor<AnalysisResult, BlockedAddress>) analysisResult -> {
                    System.out.println(analysisResult.getIp());

                    String comment = "More than " + analysisResult.getTotal()
                            + " starting from " + dateFormat.format(startDate)
                            + " to " + dateFormat.format(calculateEndDate(startDate, duration));

                    return new BlockedAddress(analysisResult.getIp(), comment);
                })
                .writer(blockedAddressesWriter)
                .build();
    }

    @Bean
    public Job importLogJob(Step logParsing, Step blockedAddressProcessing) {

        return jobBuilderFactory.get("importLogJob")
                .incrementer(new RunIdIncrementer())
                .flow(logParsing)
                .next(blockedAddressProcessing)
                .end()
                .build();
    }



}


