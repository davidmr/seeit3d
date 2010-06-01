package seeit3d.general.bus;

public abstract class Event {

	private final EventType eventType;

	public Event(EventType type) {
		eventType = type;
	}

	public EventType getEventType() {
		return eventType;
	}

}
