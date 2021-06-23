package com.igt.demo.betting.domain.repository;

import java.util.*;
import java.util.stream.*;

import com.igt.demo.betting.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

	List<Bet> findAllByPlayerId(long playerId);

	@Query("SELECT DISTINCT b.id FROM Bet b JOIN b.legs l WHERE b.state <> 'O' AND l.marketId = :marketId")
	Stream<Long> findAllIdsPlacedByMarketId(long marketId);
}