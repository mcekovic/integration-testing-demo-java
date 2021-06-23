package com.igt.demo.betting.fixtures;

import java.util.*;

import javax.annotation.*;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import static com.igt.demo.betting.makers.EventMaker.*;

@Component
public class EventFixture {

	@Autowired
	private EventRepository eventRepository;

	private final List<Long> eventIds = new ArrayList<>();

	public Event createEvent() {
		var event = makeEvent();
		eventRepository.save(event);
		eventRepository.flush();
		eventIds.add(event.getId());
		return event;
	}

	@PreDestroy @Transactional
	public void cleanUp() {
		for (var eventId : eventIds)
			eventRepository.deleteById(eventId);
	}
}
