package com.igt.demo.betting.wallet;

import java.math.*;

import com.igt.demo.betting.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.*;

@Component
@ConditionalOnProperty(name = "wallet.type", havingValue = "awesome")
public class AwesomePlayerWallet implements PlayerWallet {

	private final AwesomePlayerWalletClient client;

	@Autowired
	public AwesomePlayerWallet(@Value("${awesome.wallet.url}") String uri) {
		client = AwesomePlayerWalletClient.client(uri);
	}

	@Override public String postTx(long playerId, BigDecimal amount) {
		return client.postTx(new TxRequest(playerId, amount)).getTxReference();
	}
}
