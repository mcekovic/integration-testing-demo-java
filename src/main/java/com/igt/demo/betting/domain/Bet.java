package com.igt.demo.betting.domain;

import java.math.*;
import java.time.*;
import java.util.*;
import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity @Table(name = "bet")
public class Bet {

	@Id @Column(name = "bet_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING) @Column(name = "bet_type")
	private BetType betType;

	@Column(name = "stake")
	private BigDecimal stake;

	@Column(name = "max_return")
	private BigDecimal maxReturn;

	@Column(name = "return")
	private BigDecimal _return;

	@Enumerated(EnumType.STRING) @Column(name = "state")
	private BetState state;

	@Column(name = "player_id")
	private long playerId;

	@Column(name = "stake_tx_ref")
	private String stakeTxReference;

	@Column(name = "return_tx_refs")
	private String returnTxReferences;

	@OneToMany(mappedBy = "id.bet", fetch = EAGER, cascade = ALL) @OrderBy("id.index")
	private List<BetLeg> legs = new ArrayList<>();

	@Column(name = "attempted")
	private LocalDateTime attempted;

	@Column(name = "placed")
	private LocalDateTime placed;

	@Column(name = "settled")
	private LocalDateTime settled;

	@Version @Column(name = "version")
	private int version;

	public Bet() {}

	public Bet(BigDecimal stake, List<BetLeg> legs) {
		this.stake = stake;
		legs.forEach(this::addLeg);
		determineBetType();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BetType getBetType() {
		return betType;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public BigDecimal getMaxReturn() {
		return maxReturn;
	}

	public void setMaxReturn(BigDecimal maxReturn) {
		this.maxReturn = maxReturn;
	}

	public BigDecimal getReturn() {
		return _return;
	}

	public void setReturn(BigDecimal _return) {
		this._return = _return;
	}

	public BetState getState() {
		return state;
	}

	public void setState(BetState state) {
		this.state = state;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getStakeTxReference() {
		return stakeTxReference;
	}

	public void setStakeTxReference(String stakeTxReference) {
		this.stakeTxReference = stakeTxReference;
	}

	public String getReturnTxReferences() {
		return returnTxReferences;
	}

	public void addReturnTxReference(String returnTxReference) {
		returnTxReferences = returnTxReferences != null ? returnTxReferences + "|" + returnTxReference : returnTxReference;
	}

	public List<BetLeg> getLegs() {
		return legs;
	}

	public BetLeg getLeg(int index) {
		return legs.stream().filter(leg -> leg.getIndex() == index).findFirst().orElseThrow();
	}

	public void addLeg(BetLeg betLeg) {
		legs.add(betLeg);
		betLeg.setBet(this);
	}

	public LocalDateTime getAttempted() {
		return attempted;
	}

	public LocalDateTime getPlaced() {
		return placed;
	}

	public LocalDateTime getSettled() {
		return settled;
	}

	public int getVersion() {
		return version;
	}

	public void attempt() {
		determineBetType();
		state = BetState.ATTEMPTED;
		attempted = LocalDateTime.now();
	}

	private void determineBetType() {
		if (Objects.requireNonNull(legs).isEmpty())
			throw new IllegalArgumentException("Legs cannot be empty");
		betType = legs.size() == 1 ? BetType.SINGLE : BetType.ACCUMULATOR;
	}

	public void place() {
		state = BetState.OPEN;
		placed = LocalDateTime.now();
	}

	public BigDecimal settle() {
		var price = BigDecimal.ONE;
		loop:
		for (var leg : legs) {
			var result = leg.getResult();
			if (result == null) {
				state = BetState.OPEN;
				return null;
			}
			switch (result) {
				case WON:
					price = price.multiply(leg.getPrice());
					break;
				case VOID:
					// Do nothing
					break;
				case LOST:
					price = BigDecimal.ZERO;
					break loop;
			}
		}
		_return = stake.multiply(price);
		state = BetState.SETTLED;
		settled = LocalDateTime.now();
		return _return;
	}

	public void calculateMaxReturn() {
		maxReturn = stake.multiply(calculateCumulativePrice());
	}

	private BigDecimal calculateCumulativePrice() {
		switch (betType) {
			case SINGLE:
				return legs.get(0).getPrice();
			case ACCUMULATOR:
				return legs.stream()
						.map(BetLeg::getPrice)
						.reduce(BigDecimal.ONE, BigDecimal::multiply);
			default:
				throw new IllegalStateException();
		}
	}
}
