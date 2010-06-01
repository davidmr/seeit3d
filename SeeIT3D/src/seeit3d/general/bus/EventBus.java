package seeit3d.general.bus;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EventBus {

	private static final Multimap<EventType, IEventListener> listeners;

	static {
		listeners = ArrayListMultimap.create();
	}

	public synchronized static void registerListener(EventType eventType, IEventListener listener) {
		listeners.put(eventType, listener);
	}

	public synchronized static void publishEvent(Event event) {
		Collection<IEventListener> listenersByType = listeners.get(event.getEventType());
		for (IEventListener listener : listenersByType) {
			listener.processEvent(event);
		}
	}

}
