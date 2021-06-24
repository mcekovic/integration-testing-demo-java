package com.igt.demo.betting.domain;

import java.math.*;
import java.time.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

import org.hibernate.annotations.*;

@Entity @Table(name = "selection")
public class Selection {

	@Id @Column(name = "selection_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne @JoinColumn(name = "market_id", updatable = false)
	private Market market;

	@Column(name = "index")
	private int index;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "state")
	private BoState state;

	@Column(name = "result")
	private SelectionResult result;

	@CreationTimestamp @Column(name = "created")
	private LocalDateTime created;

	@UpdateTimestamp @Column(name = "updated")
	private LocalDateTime updated;

	public Selection() {}

	public Selection(int index, String name, BigDecimal price) {
		this.index = index;
		this.name = name;
		this.price = price;
		state = BoState.DECLARED;
	}

	public long getId() {
		return id;
	}

	public Market getMarket() {
		return market;
	}

	void setMarket(Market market) {
		this.market = market;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BoState getState() {
		return state;
	}

	public void setState(BoState state) {
		this.state = state;
	}

	public SelectionResult getResult() {
		return result;
	}

	public void setResult(SelectionResult result) {
		this.result = result;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}
}
