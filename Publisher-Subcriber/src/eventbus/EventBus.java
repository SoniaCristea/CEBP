/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package eventbus;

import pubsub.Message;
import pubsub.Subscriber;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public interface EventBus {

  public void addMsgToList(Message msg);

  public void addSubscriber(String topic, Subscriber subscriber);

  public void removeSubscriber(String topic, Subscriber subscriber);

  public void broadcastMsg();

  public void getEventsForTopic(String topic, Subscriber subscriber);

}
