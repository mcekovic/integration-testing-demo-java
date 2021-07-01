package com.igt.demo.betting.contract.provider;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.*;
import au.com.dius.pact.provider.junitsupport.loader.*;
import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.service.*;
import com.igt.demo.betting.postgresql.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.web.server.*;

@PostgreSQLTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("BettingService")
@PactFolder("src/test/resources/pacts")
@Tag("Contract")
class BettingApiContractIT {

	@LocalServerPort
	private int port;

	@MockBean
	private BetPlacementService placementService;

	@MockBean
	private PlayerWallet playerWallet;

	@BeforeEach
	void setUp(PactVerificationContext context) {
		context.setTarget(new HttpTestTarget("localhost", port));
	}

	@TestTemplate
	@ExtendWith(PactVerificationInvocationContextProvider.class)
	void verifyPact(PactVerificationContext context) {
		context.verifyInteraction();
	}
}
