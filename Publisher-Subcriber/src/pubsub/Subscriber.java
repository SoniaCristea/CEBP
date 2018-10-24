/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package pubsub;

import java.util.ArrayList;
import java.util.List;

import eventbus.EventBus;
import eventbus.EventBusImpl;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class Subscriber {

  private List<Message> subscriberMessages = new ArrayList<Message>();

  public List getSubscriberMessages() {
    return subscriberMessages;
  }

  public void setSubscriberMessages(List subscriberMessages) {
    this.subscriberMessages = subscriberMessages;
  }

  public void addSubscriber(String topic, EventBus eb) {
    eb.addSubscriber(topic, this);
  }

  public void unsubscribe(String topic, EventBusImpl eventBus) {
    eventBus.removeSubscriber(topic, this);
  }

  public void getEventsForTopic(String topic, EventBusImpl eventBus) {
    eventBus.getEventsForTopic(topic, this);
  }

  public void printMessages() {
    for (Message m : subscriberMessages) {
      System.out.println("Event : " + m.getHeader());

    }
  }

}
