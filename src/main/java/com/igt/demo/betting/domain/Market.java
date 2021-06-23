package com.igt.demo.betting.domain;

import java.time.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.*;

import org.hibernate.annotations.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity @Table(name = "market")
public class Market {

	@Id @Column(name = "market_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "event_id")
	private long eventId;

	@Column(name = "name")
	private String name;

	@Enumerated(EnumType.STRING) @Column(name = "state")
	private BoState state;

	@OneToMany(mappedBy = "market", fetch = EAGER, cascade = ALL) @OrderBy("index")
	private List<Selection> selections = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created")
	private LocalDateTime created;

	@UpdateTimestamp @Column(name = "updated")
	private LocalDateTime updated;

	@Version @Column(name = "version")
	private int version;

	public Market() {}

	public Market(long eventId, String name, List<Selection> selections) {
		this.eventId = eventId;
		this.name = name;
		state = BoState.DECLARED;
		selections.forEach(this::addSelection);
	}

	public long getId() {
		return id;
	}

	public long getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoState getState() {
		return state;
	}

	public void setState(BoState state) {
		this.state = state;
		for (var selection : selections)
			selection.setState(state);
	}

	public List<Selection> getSelections() {
		return selections;
	}

	public Selection getSelection(long selectionId) {
		return selections.stream().filter(s -> s.getId() == selectionId).findFirst().orElseThrow();
	}

	public Selection getSelectionByIndex(int index) {
		return selections.stream().filter(s -> s.getIndex() == index).findFirst().orElseThrow();
	}

	public void addSelection(Selection selection) {
		selections.add(selection);
		selection.setMarket(this);
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public int getVersion() {
		return version;
	}}
