package it.univaq.disim.sa.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "InhibitEvent")
public class InhibitEvent {
	private Inserted inserted;
	private Deleted deleted;

	public InhibitEvent() {
		super();
	}

	public InhibitEvent(Inserted inserted, Deleted deleted) {
		super();
		this.inserted = inserted;
		this.deleted = deleted;
	}

	@XmlElement(name = "Inserted")
	public Inserted getInserted() {
		return inserted;
	}

	public void setInserted(Inserted inserted) {
		this.inserted = inserted;
	}

	@XmlElement(name = "Deleted")
	public Deleted getDeleted() {
		return deleted;
	}

	public void setDeleted(Deleted deleted) {
		this.deleted = deleted;
	}

}
