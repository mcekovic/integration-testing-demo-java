package com.igt.demo.betting.domain.service;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service @Transactional
public class MarketResultingService {

	private MarketRepository marketRepository;

	@Autowired
	public MarketResultingService(MarketRepository marketRepository) {
		this.marketRepository = marketRepository;
	}

	public void resultMarket(long marketId, long wonSelectionId) {
		var market = marketRepository.getById(marketId);
		market.setState(BoState.CLOSED);
		for (var selection : market.getSelections()) {
			selection.setState(BoState.CLOSED);
			selection.setResult(selection.getId() == wonSelectionId ? SelectionResult.WON : SelectionResult.LOST);
		}
		marketRepository.save(market);
	}
}
