package edu.gdkm.weixin.domain.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.gdkm.weixin.domain.InMessage;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventinMessage extends InMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Event")
	private String event;
	@JsonProperty("EventKey")
	private String eventKey;
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	@Override
	public String toString() {
		return "EventinMessage [event=" + event + ", eventKey=" + eventKey + ", getToUserName()=" + getToUserName()
				+ ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
				+ ", getMsgType()=" + getMsgType() + ", getMsgId()=" + getMsgId() + "]";
	}
	
	
	}
	


