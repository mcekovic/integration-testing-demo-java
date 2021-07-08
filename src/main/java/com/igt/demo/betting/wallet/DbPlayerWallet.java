package com.igt.demo.betting.wallet;

import java.math.*;
import java.sql.*;
import java.util.*;

import com.igt.demo.betting.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.jdbc.core.*;

@ConditionalOnProperty(name = "wallet.type", havingValue = "db")
public class DbPlayerWallet implements PlayerWallet {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String POST_TX_CALL = "{ :txReference = CALL PostTx(:playerId, :amount) }";

	@Override public String postTx(long playerId, BigDecimal amount) {
		var params = List.of(
			new SqlOutParameter("txReference", Types.VARCHAR),
			new SqlParameter("playerId", Types.BIGINT),
			new SqlParameter("amount", Types.NUMERIC)
		);
		var paramValues = Map.of(
			"playerId", playerId,
			"amount", amount
		);
		var csc = new CallableStatementCreatorFactory(POST_TX_CALL, params).newCallableStatementCreator(paramValues);
		var result = jdbcTemplate.call(csc, params);
		return String.valueOf(result.get("txReference"));
	}
}
