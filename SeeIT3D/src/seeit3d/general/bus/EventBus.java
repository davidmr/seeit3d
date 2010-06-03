/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.general.bus;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Bus to handle interactions between components in application
 * 
 * @author David Montaño
 * 
 */
public class EventBus {

	private static final Multimap<Class<? extends IEvent>, IEventListener> listeners;

	private EventBus() {}

	static {
		listeners = ArrayListMultimap.create();
	}

	public synchronized static void registerListener(Class<? extends IEvent> eventClass, IEventListener listener) {
		listeners.put(eventClass, listener);
	}

	// TODO try to use a queue where the bus reads events to avoid possible deadlock if this method is synchronized
	public static void publishEvent(IEvent event) {
		Collection<IEventListener> listenersByClass = listeners.get(event.getClass());

		if (listenersByClass == null || listenersByClass.isEmpty()) {
			System.err.println("No listeners for class: " + event.getClass());
		} else {
			for (IEventListener listener : listenersByClass) {
				listener.processEvent(event);
			}
		}
	}

}
