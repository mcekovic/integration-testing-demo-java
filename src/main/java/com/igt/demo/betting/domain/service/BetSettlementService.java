package com.igt.demo.betting.domain.service;

import com.igt.demo.betting.api.*;
import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.wallet.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service @Transactional @Slf4j
public class BetSettlementService {

	private BetRepository betRepository;
	private MarketRepository marketRepository;
	private PlayerWallet wallet;

	@Autowired
	public BetSettlementService(BetRepository betRepository, MarketRepository marketRepository, PlayerWallet wallet) {
		this.betRepository = betRepository;
		this.marketRepository = marketRepository;
		this.wallet = wallet;
	}

	public SettlementResult settleMarketBets(long marketId) {
		return betRepository.findAllIdsPlacedByMarketId(marketId)
			.parallel()
			.map(this::safeSettleBet)
			.reduce(SettlementResult.EMPTY, SettlementResult::add);
	}

	private SettlementResult safeSettleBet(long betId) {
		try {
			log.info("Settling bet {}", betId);
			return settleBet(betId);
		}
		catch (Exception ex) {
			log.error("Error settling bet {}", betId, ex);
			return SettlementResult.EMPTY;
		}
	}

	public SettlementResult settleBet(long betId) {
		var bet = betRepository.getById(betId);
		resultLegs(bet);
		var oldReturn = bet.getReturn();
		var newReturn = bet.settle();
		var deltaReturn = oldReturn != null ? newReturn.subtract(oldReturn) : newReturn;
		if (deltaReturn.signum() != 0) {
			var txReference = wallet.postTx(bet.getPlayerId(), deltaReturn);
			bet.addReturnTxReference(txReference);
		}
		return new SettlementResult(bet.getState());
	}

	private void resultLegs(Bet bet) {
		for (var leg : bet.getLegs())
			leg.result(marketRepository.getById(leg.getMarketId()));
	}
}
