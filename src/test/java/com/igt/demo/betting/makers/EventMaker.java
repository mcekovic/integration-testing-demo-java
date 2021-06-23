package com.igt.demo.betting.makers;

import com.igt.demo.betting.domain.*;

public abstract class EventMaker {

	public static Event makeEvent() {
		var event = new Event("Tour de France");
		event.setState(BoState.OPEN);
		return event;
	}
}
