package com.igt.demo.betting.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;

@Embeddable
public class BetLegId implements Serializable {

	@ManyToOne @JoinColumn(name = "bet_id", updatable = false)
	private Bet bet;

	@Column(name = "index")
	private int index;

	public Bet getBet() {
		return bet;
	}

	void setBet(Bet bet) {
		this.bet = bet;
	}

	public long getBetId() {
		return bet.getId();
	}

	public int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BetLegId)) return false;
		var id = (BetLegId) o;
		return getBetId() == id.getBetId() && index == id.index;
	}

	@Override public int hashCode() {
		return Objects.hash(getBetId(), index);
	}

	@Override public String toString() {
		return "BetLegId{" +
			"betId=" + getBetId() +
			", legIndex=" + index +
		'}';
	}
}