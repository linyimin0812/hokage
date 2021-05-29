package com.hokage.config;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
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
@Slf4j
@Component
public class SqlScriptInitRunner {

    private final String DDL = "schema";
    private final String DML = "data";

    private final String SCRIPT_ROOT = "/mysql/";
    private final String SQL_SUFFIX = ".sql";

    @Value("${spring.datasource.schema}")
    private String sqlScriptPath;

    private final ScriptRunner runner;

    private List<String> paths = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(SqlScriptInitRunner.class);

    @Autowired
    public SqlScriptInitRunner(DataSource dataSource) throws SQLException {
        this.runner = new ScriptRunner(dataSource.getConnection());
    }

    @PostConstruct
    public void init() throws Exception {

        runner.setLogWriter(new PrintWriter(new LogPrintWriter(logger, Level.INFO)));

        Consumer<String> consumer;

        if (!StringUtils.isBlank(sqlScriptPath)) {

            try {
                consumer = executeSqlFromClasspath(sqlScriptPath);
            } catch (FileNotFoundException e) {
                consumer = executeSqlFromJar(sqlScriptPath);
            }

            executeBatSql(paths, consumer);
        }
    }

    private Consumer<String> executeSqlFromClasspath(String sqlScriptPath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(sqlScriptPath);

        List<String> fileNames = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(File::isFile)
                .map(File::getAbsolutePath).collect(Collectors.toList());

        this.paths.addAll(fileNames);

        return fileName -> {
            try {
                runner.runScript(new BufferedReader(new FileReader(fileName)));
            } catch (FileNotFoundException e) {
                log.error("execute sql from classpath error. {}", JSON.toJSON(e));
            }
        };
    }

    private Consumer<String> executeSqlFromJar(String sqlScriptPath) throws IOException {
        String root = "/";
        String protocol = "jar";

        URL urlDir = this.getClass().getResource(root);
        if (Objects.nonNull(urlDir) && StringUtils.equals(urlDir.getProtocol(), protocol)) {
            final String jarPath = urlDir.getPath().substring(5, urlDir.getPath().indexOf("!"));
            final JarFile jar = new JarFile(URLDecoder.decode(jarPath, String.valueOf(StandardCharsets.UTF_8)));
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    paths.add(entry.getName());
                }
            }
            this.paths = this.paths.stream()
                    .filter(sqlScriptName ->
                            StringUtils.contains(sqlScriptName, SCRIPT_ROOT) && StringUtils.endsWith(sqlScriptName, SQL_SUFFIX))
                    .map(sqlScriptName -> sqlScriptName.substring(sqlScriptName.indexOf(SCRIPT_ROOT)))
                    .collect(Collectors.toList());

            jar.close();
            return fileName -> runner.runScript(new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName))));

        } else {
            throw new UnsupportedOperationException("Cannot list files for dir " + sqlScriptPath);
        }
    }

    private void executeBatSql(List<String> paths, Consumer<String> consumer) {
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

    @Data
    @EqualsAndHashCode(callSuper = true)
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
            if (level == Level.TRACE) {
                logger.trace(mem);
                return;
            }
            if (level == Level.DEBUG) {
                logger.debug(mem);
                return;
            }
            if (level == Level.WARN) {
                logger.warn(mem);
                return;
            }
            if (level == Level.ERROR) {
                logger.error(mem);
                return;
            }
            logger.info(mem);
        }
    }
}
