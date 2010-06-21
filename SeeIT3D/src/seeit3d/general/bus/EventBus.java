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
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Bus to handle interactions between components in the plugin
 * 
 * @author David Montaño
 * 
 */
public class EventBus {

	private static final ArrayBlockingQueue<IEvent> eventQueue;

	private static final Multimap<Class<? extends IEvent>, IEventListener> listeners;

	private EventBus() {
	}

	static {
		eventQueue = new ArrayBlockingQueue<IEvent>(30);
		listeners = ArrayListMultimap.create();
		Thread eventDispatcherThread = new Thread(new EventDispatcher(), "SeeIT3DEventDispatcher");
		eventDispatcherThread.start();
	}

	public static void registerListener(Class<? extends IEvent> eventClass, IEventListener listener) {
		synchronized (listeners) {
			listeners.put(eventClass, listener);
			System.out.println("Register listener for class: " + eventClass);
		}
	}

	public static void unregisterListener(Class<? extends IEvent> eventClass, IEventListener listener) {
		synchronized (listeners) {
			Collection<IEventListener> listenersByClass = listeners.get(eventClass);
			listenersByClass.remove(listener);
			System.out.println("UnRegister listener for class: " + eventClass);
		}
	}

	public static void publishEvent(IEvent event) {
		try {
			eventQueue.put(event);
		} catch (InterruptedException e) {
			System.err.println("Event " + event.getClass() + " couldn't be registered");
			e.printStackTrace();
		}
	}

	private static final class EventDispatcher implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					IEvent event = eventQueue.take();
					dispatchEvent(event);
				} catch (InterruptedException e) {
					System.err.println("Error retrieving event from queue");
					e.printStackTrace();
				}
			}
		}

		private void dispatchEvent(IEvent event) {
			synchronized (listeners) {
				System.out.println("Dispatching event: " + event);
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

	}

}
