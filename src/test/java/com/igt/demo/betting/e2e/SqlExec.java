package com.igt.demo.betting.e2e;

import java.io.*;
import java.nio.file.*;

import org.postgresql.ds.*;
import org.springframework.core.io.*;
import org.springframework.jdbc.core.*;

public abstract class SqlExec {

	public static void sqlExec(String path) throws IOException {
		var dataSource = new PGSimpleDataSource();
		dataSource.setURL("jdbc:postgresql://localhost:5433/betting");
		dataSource.setUser("betting");
		dataSource.setPassword("betting");
		var jdbc = new JdbcTemplate(dataSource);
		var sql = Files.readString(Path.of(new ClassPathResource(path).getFile().getAbsolutePath()));
		jdbc.execute(sql);
	}
}
