package com.igt.demo.betting.fixtures;

import java.util.*;

import javax.annotation.*;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import static com.igt.demo.betting.makers.BetMaker.*;

@Component
public class BetFixture {

	@Autowired
	private BetRepository betRepository;

	private final List<Long> betIds = new ArrayList<>();

	public Bet createSingleBet(long marketId, long selectionId) {
		var bet = makeSingleBet(marketId, selectionId);
		betRepository.save(bet);
		betRepository.flush();
		betIds.add(bet.getId());
		return bet;
	}

	@PreDestroy
	public void cleanUp() {
		for (var betId : betIds)
			betRepository.deleteById(betId);
	}
}
