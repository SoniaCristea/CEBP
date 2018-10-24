/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package pubsub;

import eventbus.EventBus;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class Publisher {

  public void publishMsg(Message msg, EventBus eb) {
    eb.addMsgToList(msg);
  }

}
