package com.igt.demo.betting.layers.repository;

import java.time.*;

import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.fixtures.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;

import static com.igt.demo.betting.makers.EventMaker.*;
import static org.assertj.core.api.Assertions.*;

@RepositoryTest
class EventRepositoryIT {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventFixture eventFixture;

	@Test
	void eventIsCreated() {
		var event = makeEvent();

		eventRepository.save(event);
		eventRepository.flush();

		var savedEvent = eventRepository.getById(event.getId());
		assertThat(savedEvent).usingRecursiveComparison().isEqualTo(event);
		assertThat(savedEvent.getCreated()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void eventIsUpdated() {
		var event = eventFixture.createEvent();

		var savedEvent = eventRepository.getById(event.getId());
		var version = savedEvent.getVersion();
		savedEvent.setName("Tour de France 2021");
		eventRepository.save(event);
		eventRepository.flush();

		var updatedEvent = eventRepository.getById(savedEvent.getId());
		assertThat(updatedEvent).usingRecursiveComparison().isEqualTo(savedEvent);
		assertThat(updatedEvent.getUpdated()).isAfterOrEqualTo(updatedEvent.getCreated());
		assertThat(updatedEvent.getVersion()).isGreaterThan(version);
	}
}
