package com.igt.demo.betting.util;

import java.io.*;
import java.nio.file.*;
import javax.sql.*;

import org.postgresql.ds.*;
import org.springframework.core.io.*;
import org.springframework.jdbc.core.*;

public class SqlExec {

	private final JdbcTemplate jdbc;

	public SqlExec(String dbUrl, String user, String password) {
		this(createDataSource(dbUrl, user, password));
	}

	public SqlExec(DataSource dataSource) {
		jdbc = new JdbcTemplate(dataSource);
	}

	private static PGSimpleDataSource createDataSource(String dbUrl, String user, String password) {
		var dataSource = new PGSimpleDataSource();
		dataSource.setURL(dbUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		return dataSource;
	}

	public void execute(String path) throws IOException {
		var sql = Files.readString(Path.of(new ClassPathResource(path).getFile().getAbsolutePath()));
		jdbc.execute(sql);
	}
}
