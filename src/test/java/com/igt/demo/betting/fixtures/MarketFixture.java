package com.igt.demo.betting.fixtures;

import java.util.*;

import javax.annotation.*;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import static com.igt.demo.betting.makers.MarketMaker.*;

@Component
public class MarketFixture {

	@Autowired
	private MarketRepository marketRepository;

	private final List<Long> marketIds = new ArrayList<>();

	public Market createMarket(long eventId) {
		var market = makeMarket(eventId);
		marketRepository.save(market);
		marketRepository.flush();
		marketIds.add(market.getId());
		return market;
	}

	@PreDestroy
	public void cleanUp() {
		for (var marketId : marketIds)
			marketRepository.deleteById(marketId);
	}
}
