package com.banzhe.hokage.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author linyimin
 * @date 2020/11/12 17:54
 * @email linyimin520812@gmail.com
 * @description Initialize database after the application starts
 */
@Order(1)
@Component
public class SqlScriptInitRunner implements CommandLineRunner {

    private final String DDL = "schema";
    private final String DML = "data";

    @Value("${spring.datasource.schema}")
    private String sqlScriptPath;

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final Logger logger = LoggerFactory.getLogger(SqlScriptInitRunner.class);

    @Override
    public void run(String... args) throws Exception {

        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());

        runner.setLogWriter(new PrintWriter(new LogPrintWriter(logger, Level.INFO)));

        if (!StringUtils.isBlank(sqlScriptPath)) {

            File file = ResourceUtils.getFile(sqlScriptPath);

            // run the DDL script first
            Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .filter(File::isFile)
                    .filter(sqlScriptFile -> StringUtils.contains(sqlScriptFile.getName(), DDL))
                    .forEach(sqlScriptFile -> {
                        try {
                            runner.runScript(new BufferedReader(new FileReader(sqlScriptFile)));
                        } catch (FileNotFoundException ignored) {
                            // The Exception will never be triggered
                        }
                    });

            // Then run the DML script
            Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .filter(File::isFile)
                    .filter(sqlScriptFile -> StringUtils.contains(sqlScriptFile.getName(), DML))
                    .forEach(sqlScriptFile -> {
                        try {
                            runner.runScript(new BufferedReader(new FileReader(sqlScriptFile)));
                        } catch (FileNotFoundException ignored) {
                            // The Exception will never be triggered
                        }
                    });

            // Last run the remaining scripts
            Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .filter(File::isFile)
                    .filter(sqlScriptFile -> !StringUtils.containsAny(sqlScriptFile.getName(), DDL, DML))
                    .forEach(sqlScriptFile -> {
                        try {
                            runner.runScript(new BufferedReader(new FileReader(sqlScriptFile)));
                        } catch (FileNotFoundException ignored) {
                            // The Exception will never be triggered
                        }
                    });
        }
    }
    @Data
    private class LogPrintWriter extends Writer {

        private Logger logger;
        private Level level;

        private StringBuilder mem;

        public LogPrintWriter(Logger logger, Level level) {
            this.logger = logger;
            this.level = level;
            mem = new StringBuilder();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            IntStream.range(0, len)
                    .filter(i -> !StringUtils.equals(String.valueOf(cbuf[i+off]), StringUtils.LF))
                    .forEach(i -> mem.append(cbuf[i+off]));
        }

        @Override
        public void flush() throws IOException {
            log(mem.toString());
            mem = new StringBuilder();
        }

        @Override
        public void close() throws IOException {}

        private void log(String mem) {
            switch (level) {
                case INFO:
                    logger.info(mem);
                    break;
                case TRACE:
                    logger.trace(mem);
                    break;
                case DEBUG:
                    logger.debug(mem);
                    break;
                case WARN:
                    logger.warn(mem);
                    break;
                case ERROR:
                    logger.error(mem);
                    break;
                default:
                    logger.info(mem);
                    break;
            }
        }
    }
}
