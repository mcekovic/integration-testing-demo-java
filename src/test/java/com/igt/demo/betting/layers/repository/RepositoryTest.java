package com.igt.demo.betting.layers.repository;

import java.lang.annotation.*;

import com.igt.demo.betting.postgresql.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.*;

@PostgreSQLTest
@DataJpaTest
@ComponentScan(basePackages = "com.igt.demo.betting.fixtures")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryTest {}
