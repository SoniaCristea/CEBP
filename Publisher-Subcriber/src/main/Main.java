/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package main;

import eventbus.EventBus;
import eventbus.EventBusImpl;
import pubsub.Message;
import pubsub.Publisher;
import pubsub.Subscriber;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

class Main {

  public static void main(String[] args) {

    EventBusImpl eb = new EventBusImpl();

    Publisher publisher1 = new Publisher();
    Publisher publisher2 = new Publisher();

    Subscriber s1 = new Subscriber();
    Subscriber s2 = new Subscriber();
    Subscriber s3 = new Subscriber();

    Message msg1 = new Message("Street Food Festival");
    Message msg2 = new Message("Opera night");

    publisher1.publishMsg(msg1, eb);
    publisher2.publishMsg(msg2, eb);

    s1.addSubscriber("Street Food Festival", eb);
    s1.addSubscriber("Opera night", eb);
    s2.addSubscriber("Opera night", eb);
    s3.addSubscriber("Street Food Festival", eb);
    s3.addSubscriber("Opera night", eb);

    //TODO getSubscriberMessages return null , line 72 in EventBusImpl class
    eb.broadcastMsg();
    
    System.out.println("Subscriber 1 : ");
    s1.printMessages();
    System.out.println("Subscriber 2 : ");
    s2.printMessages();
    System.out.println("Subscriber 3 : ");
    s3.printMessages();
    
    System.out.println();
    System.out.println("Subscriber 3 after unsubscribing an event: ");
    s3.unsubscribe("Opera night", eb);
    s3.printMessages();

   
  }

}
