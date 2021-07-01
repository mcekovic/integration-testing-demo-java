package com.igt.demo.betting.component;

import java.math.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.igt.demo.betting.api.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.domain.service.*;
import com.igt.demo.betting.fixtures.*;
import com.igt.demo.betting.util.*;
import com.igt.demo.betting.wallet.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.util.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.context.*;
import org.springframework.http.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.entity.ContentType.*;
import static org.assertj.core.api.Assertions.*;

@ComponentTest
class BettingApplicationIT {

	static class AwesomeWalletInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override public void initialize(ConfigurableApplicationContext context) {
			TestPropertyValues.of("awesome.wallet.url=" + WireMockExtension.baseUrl())
				.applyTo(context.getEnvironment());
		}
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EventFixture eventFixture;

	@Autowired
	private MarketFixture marketFixture;

	@Autowired
	private BetRepository betRepository;

	@Autowired
	private MarketResultingService resultingService;

	@Autowired
	private ObjectMapper objectMapper;

	private long marketId;
	private long selectionId;
	private BigDecimal price;
	private Long betId;

	private static final String BETTING_API_URL = "/v1/betting";
	private static final String WALLET_API_URL = "/v1/wallet";
	private static final long PLAYER_ID = 123L;


	@BeforeAll
	void setUp() {
		var eventId = eventFixture.createEvent().getId();
		var market = marketFixture.createMarket(eventId);
		marketId = market.getId();
		var selection = market.getSelectionByIndex(1);
		selectionId = selection.getId();
		price = selection.getPrice();
	}

   @AfterAll
   void cleanUp() {
	   betRepository.deleteById(betId);
   }

	@Test @Order(1)
	void contextLoads() {}

	@Test @Order(2)
	void betIsPlaced() {
		var stake = new BigDecimal("10");
		var request = new PlaceBetRequest(stake, stake.multiply(price), PLAYER_ID, List.of(
			new PlaceBetLeg(marketId, selectionId, price)
		));
		stubWalletPostTx();

		var response = restTemplate.postForEntity(BETTING_API_URL + "/placeBet", request, PlaceBetResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		betId = response.getBody().getBetId();
		assertThat(betId).isNotNegative();
	}

	@Test @Order(3)
	public void marketIsResulted() {
		resultingService.resultMarket(marketId, selectionId);
	}

	@Test @Order(4)
	void marketBetsAreSettled() {
		var request = new SettlementRequest(marketId);
		stubWalletPostTx();

		var response = restTemplate.postForEntity(BETTING_API_URL + "/settleMarketBets", request, SettlementResult.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		var result = response.getBody();
		assertThat(result.getSettledCount()).isEqualTo(1);
		assertThat(result.getOpenCount()).isEqualTo(0);
	}

	@Test @Order(5)
	void playerBetsAreReturned() {
		var response = restTemplate.getForEntity(BETTING_API_URL + "/playerBets?playerId=" + PLAYER_ID, BetData[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		var bets = response.getBody();
		assertThat(bets).hasSize(1);
		assertThat(bets[0].getId()).isEqualTo(betId);
	}

	@SneakyThrows
	private void stubWalletPostTx() {
		stubFor(post(urlEqualTo(WALLET_API_URL + "/postTx"))
			.willReturn(aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
				.withBody(objectMapper.writeValueAsString(new TxResponse(UUID.randomUUID().toString())))
			)
		);
	}
}
