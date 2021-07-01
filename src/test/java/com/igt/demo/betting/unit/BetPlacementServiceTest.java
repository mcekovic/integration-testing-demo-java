package com.igt.demo.betting.unit;

import java.math.*;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.domain.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static com.igt.demo.betting.makers.BetMaker.*;
import static com.igt.demo.betting.makers.EventMaker.*;
import static com.igt.demo.betting.makers.MarketMaker.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetPlacementServiceTest {

	@Mock
	private BetRepository betRepository;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private MarketRepository marketRepository;

	@Mock
	private PlayerWallet wallet;

	@Test
	void betIsPlaced() {
		var event = makeEvent();
		var market = makeMarket(event.getId());
		var selection = market.getSelectionByIndex(1);
		var bet = makeSingleBet(market.getId(), selection.getId());
		var price = selection.getPrice();
		bet.getLeg(1).setPrice(price);
		bet.setMaxReturn(price.multiply(bet.getStake()));
		when(eventRepository.getById(event.getId())).thenReturn(event);
		when(marketRepository.getById(market.getId())).thenReturn(market);
		when(betRepository.save(any(Bet.class))).thenAnswer(invocation -> {
			var aBet = (Bet) invocation.getArguments()[0];
			aBet.setId(123L);
			return aBet;
		});
		when(wallet.postTx(eq(bet.getPlayerId()), any(BigDecimal.class))).thenReturn("ABCDMACAPREDEAMISMOTAOKOPLOTA");
		var placementService = new BetPlacementService(betRepository, eventRepository, marketRepository, wallet);

		placementService.placeBet(bet);

		assertThat(bet.getState()).isEqualTo(BetState.OPEN);
		verify(wallet).postTx(bet.getPlayerId(), bet.getStake().negate());
	}

	@Test
	void betIsRejectedWhenPriceIsChanged() {
		assertThatThrownBy(() -> {
			var event = makeEvent();
			var market = makeMarket(event.getId());
			var selection = market.getSelectionByIndex(1);
			var bet = makeSingleBet(market.getId(), selection.getId());
			var price = new BigDecimal("3");
			bet.getLeg(1).setPrice(price);
			bet.setMaxReturn(price.multiply(bet.getStake()));
			when(marketRepository.getById(anyLong())).thenReturn(market);
			var placementService = new BetPlacementService(betRepository, eventRepository, marketRepository, wallet);

			placementService.placeBet(bet);
		}).isInstanceOf(BetPlacementException.class);
		verifyNoMoreInteractions(wallet);
	}
}
