package com.igt.demo.betting.postgresql;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.test.context.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ExtendWith(PostgreSQLExtension.class)
//@ContextConfiguration(initializers = PostgreSQLDataSourceInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostgreSQLTest {}
