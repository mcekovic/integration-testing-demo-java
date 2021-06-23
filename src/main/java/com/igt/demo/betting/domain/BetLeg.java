package com.igt.demo.betting.domain;

import java.math.*;
import javax.persistence.*;

@Entity @Table(name = "bet_leg")
public class BetLeg {

	@EmbeddedId
	private BetLegId id = new BetLegId();

	@Column(name = "market_id")
	private long marketId;

	@Column(name = "selection_id")
	private long selectionId;

	@Column(name = "price")
	private BigDecimal price;

	@Enumerated(EnumType.STRING) @Column(name = "result")
	private SelectionResult result;

	public BetLeg() {}

	public BetLeg(int index, long marketId, long selectionId, BigDecimal price) {
		id.setIndex(index);
		this.marketId = marketId;
		this.selectionId = selectionId;
		this.price = price;
	}

	public BetLegId getId() {
		return id;
	}

	public long getBetId() {
		return id.getBetId();
	}

	public Bet getBet() {
		return id.getBet();
	}

	public void setBet(Bet bet) {
		id.setBet(bet);
	}

	public int getIndex() {
		return id.getIndex();
	}

	public void setIndex(int index) {
		id.setIndex(index);
	}

	public long getMarketId() {
		return marketId;
	}

	public void setMarketId(long marketId) {
		this.marketId = marketId;
	}

	public long getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(long selectionId) {
		this.selectionId = selectionId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public SelectionResult getResult() {
		return result;
	}

	public void setResult(SelectionResult result) {
		this.result = result;
	}

	public void result(Market market) {
		if (market.getState() == BoState.CLOSED) {
			var selection = market.getSelection(getSelectionId());
			if (selection.getState() == BoState.CLOSED)
				result = selection.getResult();
		}
	}
}
