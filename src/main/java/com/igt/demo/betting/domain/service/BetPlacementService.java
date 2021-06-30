package com.igt.demo.betting.domain.service;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.wallet.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service @Transactional
public class BetPlacementService {

	private BetRepository betRepository;
	private EventRepository eventRepository;
	private MarketRepository marketRepository;
	private PlayerWallet wallet;

	@Autowired
	public BetPlacementService(BetRepository betRepository, EventRepository eventRepository, MarketRepository marketRepository, PlayerWallet wallet) {
		this.betRepository = betRepository;
		this.eventRepository = eventRepository;
		this.marketRepository = marketRepository;
		this.wallet = wallet;
	}

	public void placeBet(Bet bet) {
		var stake = bet.getStake();
		if (stake == null || stake.signum() <= 0)
			throw new BetPlacementException("Bet stake %1$f must be positive", stake);

		bet.attempt();

		bet.getLegs().forEach(this::checkIfLegIsPlaceable);
		var channelMaxReturn = bet.getMaxReturn();

		bet.calculateMaxReturn();
		if (channelMaxReturn != null && channelMaxReturn.compareTo(bet.getMaxReturn()) != 0)
			throw new BetPlacementException("Bet maxReturn %1$f is not the same as calculated maxReturn %2$f", channelMaxReturn, bet.getMaxReturn());

		var txReference = wallet.postTx(bet.getPlayerId(), bet.getStake().negate());
		bet.setStakeTxReference(txReference);

		bet.place();
		betRepository.save(bet);
	}

	private void checkIfLegIsPlaceable(BetLeg leg) {
		// Check Market
		var market = marketRepository.getById(leg.getMarketId());
		if (market.getState() != BoState.OPEN)
			throw new BetPlacementException("Market %d is not open", market.getId());
		// Check Selection
		var selection = market.getSelection(leg.getSelectionId());
		if (selection.getState() != BoState.OPEN)
			throw new BetPlacementException("Selection %d is not open", selection.getId());
		if (leg.getPrice().compareTo(selection.getPrice()) != 0)
			throw new BetPlacementException("Price %1$f on selection %2$d is different from %3$f", leg.getPrice(), selection.getId(), selection.getPrice());
		// Check Event
		var event = eventRepository.getById(market.getEventId());
		if (event.getState() != BoState.OPEN)
			throw new BetPlacementException("Event %d is not open", event.getId());
	}
}
