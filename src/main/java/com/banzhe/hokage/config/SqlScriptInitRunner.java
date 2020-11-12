package com.banzhe.hokage.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public void run(String... args) throws Exception {



        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());

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
                    .filter(sqlScriptFile -> !StringUtils.contains(sqlScriptFile.getName(), DDL))
                    .filter(sqlScriptFile -> !StringUtils.contains(sqlScriptFile.getName(), DML))
                    .forEach(sqlScriptFile -> {
                        try {
                            runner.runScript(new BufferedReader(new FileReader(sqlScriptFile)));
                        } catch (FileNotFoundException ignored) {
                            // The Exception will never be triggered
                        }
                    });

        }
    }
}
