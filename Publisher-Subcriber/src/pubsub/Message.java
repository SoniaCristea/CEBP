/*
 * Copyright (c) 2018 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package pubsub;

/**
 * @author <a href="mailto:Sonia@ssi-schaefer-noell.com">Sonia</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class Message {

  private String header;

  public Message(String type) {
    this.header = type;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String type) {
    this.header = type;
  }

}
