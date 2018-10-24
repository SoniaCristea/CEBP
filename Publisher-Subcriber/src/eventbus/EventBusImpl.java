/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package eventbus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pubsub.Message;
import pubsub.Subscriber;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class EventBusImpl implements EventBus {

	private HashMap<String, Set<Subscriber>> subscriberTopic = new HashMap<>();
	private LinkedList<Message> messages = new LinkedList<>();

	public void addMsgToList(Message msg) {
		messages.add(msg);

	}

	public void addSubscriber(String topic, Subscriber subscriber) {

		if (subscriberTopic.containsKey(topic)) {
			Set<Subscriber> subs = subscriberTopic.get(topic);
			subs.add(subscriber);
			subscriberTopic.put(topic, subs);

		} else {
			Set<Subscriber> subs = new HashSet<>();
			subs.add(subscriber);
			subscriberTopic.put(topic, subs);

		}
	}

	public void removeSubscriber(String topic, Subscriber subscriber) {

		if (subscriberTopic.containsKey(topic)) {
			Set<Subscriber> subs = subscriberTopic.get(topic);
			subs.remove(subscriber);
			subscriberTopic.put(topic, subs);

			removeEventFromSubMsgList(topic, subscriber);

		}

	}

	private void removeEventFromSubMsgList(String topic, Subscriber subscriber) {
		List subscriberMessages = subscriber.getSubscriberMessages();
		for (Iterator iterator = subscriberMessages.iterator(); iterator.hasNext();) {
			Message msg = (Message) iterator.next();
			if (msg.getHeader().equals(topic)) {
				subscriberMessages.remove(msg);
				break;
			}

		}
	}

	public void broadcastMsg() {

		if (messages.isEmpty()) {
			System.out.println("There are no events available");
		} else {

			while (!messages.isEmpty()) {

				Message msg = messages.remove();
				String topic = msg.getHeader();

				Set<Subscriber> subs = subscriberTopic.get(topic);

				for (Subscriber s : subs) {
					List<Message> subsForMsg = s.getSubscriberMessages();
					subsForMsg.add(msg);
					s.setSubscriberMessages(subsForMsg);
				}
			}
		}
	}

	public void getEventsForTopic(String topic, Subscriber subscriber) {

		if (messages.isEmpty()) {
			System.out.println("There are no events available");
		} else {

			while (!messages.isEmpty()) {
				Message msg = messages.remove();
				if (msg.getHeader().equals(topic)) {

					Set<Subscriber> subs = subscriberTopic.get(topic);

					for (Subscriber s : subs) {
						if (s.equals(subscriber)) {
							List<Message> msgList = s.getSubscriberMessages();
							msgList.add(msg);
							s.setSubscriberMessages(msgList);
						}
					}
				}
			}
		}

	}

}
