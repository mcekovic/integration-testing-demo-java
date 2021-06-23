package com.igt.demo.betting.domain;

import java.time.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

import org.hibernate.annotations.*;

@Entity @Table(name = "event")
public class Event {

	@Id @Column(name = "event_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Enumerated(EnumType.STRING) @Column(name = "state")
	private BoState state;

	@CreationTimestamp @Column(name = "created")
	private LocalDateTime created;

	@UpdateTimestamp @Column(name = "updated")
	private LocalDateTime updated;

	@Version @Column(name = "version")
	private int version;

	public Event() {}

	public Event(String name) {
		this.name = name;
		state = BoState.DECLARED;
	}

	public long getId() {
		return id;
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
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public int getVersion() {
		return version;
	}
}
