package com.igt.demo.betting.domain.repository;

import com.igt.demo.betting.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {}