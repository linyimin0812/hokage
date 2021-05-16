package com.hokage.config;

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
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
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

    private final String PROTOCOL = "jar";
    private final String SCRIPT_ROOT = "/mysql/";
    private final String SQL_SUFFIX = ".sql";
    private final String CHAR_ENCODING = "UTF-8";
    private final String ROOT = "/";

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

        List<String> paths = new ArrayList<>();

        Consumer<String> consumer;

        if (!StringUtils.isBlank(sqlScriptPath)) {

            try {
                File file = ResourceUtils.getFile(sqlScriptPath);

                List<String> fileNames = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                        .filter(File::isFile)
                        .map(File::getAbsolutePath).collect(Collectors.toList());

                paths.addAll(fileNames);

                consumer = fileName -> {
                    try {
                        runner.runScript(new BufferedReader(new FileReader(fileName)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                };

            } catch (FileNotFoundException e) {
                URL urlDir = this.getClass().getResource(ROOT);

                if (Objects.nonNull(urlDir) && StringUtils.equals(urlDir.getProtocol(), PROTOCOL)) {
                    final String jarPath = urlDir.getPath().substring(5, urlDir.getPath().indexOf("!"));
                    final JarFile jar = new JarFile(URLDecoder.decode(jarPath, CHAR_ENCODING));
                    final Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (!entry.isDirectory()) {
                            paths.add(entry.getName());
                        }
                    }

                    paths = paths.stream()
                            .filter(sqlScriptName ->
                                    StringUtils.contains(sqlScriptName, SCRIPT_ROOT) && StringUtils.endsWith(sqlScriptName, SQL_SUFFIX))
                            .map(sqlScriptName -> sqlScriptName.substring(sqlScriptName.indexOf(SCRIPT_ROOT)))
                            .collect(Collectors.toList());

                    consumer = fileName -> runner.runScript(new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName))));

                    jar.close();
                } else {
                    throw new UnsupportedOperationException("Cannot list files for dir " + sqlScriptPath);
                }
            }

            // run the DDL script first
            paths.stream()
                    .filter(sqlScriptFile -> StringUtils.contains(sqlScriptFile, DDL))
                    .forEach(consumer);

            // Then run the DML script
            paths.stream()
                    .filter(sqlScriptFile -> StringUtils.contains(sqlScriptFile, DML))
                    .forEach(consumer);

            // Last run the remaining scripts
            paths.stream()
                    .filter(sqlScriptFile -> !StringUtils.containsAny(sqlScriptFile, DDL, DML))
                    .forEach(consumer);
        }
    }
    @Data
    private static class LogPrintWriter extends Writer {

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
