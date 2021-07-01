package com.igt.demo.betting.component;

import java.lang.annotation.*;

import com.igt.demo.betting.postgresql.*;
import com.igt.demo.betting.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

@PostgreSQLTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(WireMockExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = BettingApplicationIT.AwesomeWalletInitializer.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentTest {}
