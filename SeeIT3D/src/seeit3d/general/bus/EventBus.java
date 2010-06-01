package seeit3d.general.bus;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EventBus {

	private static final Multimap<Class<? extends IEvent>, IEventListener> listeners;

	static {
		listeners = ArrayListMultimap.create();
	}

	public synchronized static void registerListener(Class<? extends IEvent> eventClass, IEventListener listener) {
		listeners.put(eventClass, listener);
	}

	// TODO try to use a queue where the bus reads events to avoid possible deadlock if this method is synchronized
	public static void publishEvent(IEvent event) {
		Collection<IEventListener> listenersByClass = listeners.get(event.getClass());
		for (IEventListener listener : listenersByClass) {
			listener.processEvent(event);
		}
	}

}
