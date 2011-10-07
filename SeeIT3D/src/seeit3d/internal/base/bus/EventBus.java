/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.bus;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

import seeit3d.internal.base.error.ErrorHandler;

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

	private EventBus() {}

	static {
		eventQueue = new ArrayBlockingQueue<IEvent>(10);
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
		Thread publisher = new Thread(new EventPublisher(event), "Event publisher");
		publisher.start();
	}

	private static final class EventPublisher implements Runnable {

		private final IEvent event;

		public EventPublisher(IEvent event) {
			this.event = event;
		}

		@Override
		public void run() {
			try {
				// System.out.println("Publishing event: " + event);
				eventQueue.put(event);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
				try {
					// System.out.println("Dispatching event: " + event);
					Collection<IEventListener> listenersByClass = listeners.get(event.getClass());

					if (listenersByClass == null || listenersByClass.isEmpty()) {
						System.err.println("No listeners for class: " + event.getClass());
					} else {
						for (IEventListener listener : listenersByClass) {
							listener.processEvent(event);
						}
					}
				} catch (Exception e) {
					ErrorHandler.error(e);
				}
			}
		}

	}

}
