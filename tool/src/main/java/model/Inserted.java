package model;

import javax.xml.bind.annotation.XmlElement;

public class Inserted {
	private String equip_OID;
	private String recipe_OID;
	private String step_OID;
	private String hold_type = "ProcessEquipHold";
	private String hold_flag;

	public Inserted(String equip_OID, String recipe_OID, String step_OID, String hold_flag) {
		super();
		this.equip_OID = equip_OID;
		this.recipe_OID = recipe_OID;
		this.step_OID = step_OID;
		this.hold_flag = hold_flag;
	}

	@XmlElement
	public String getEquip_OID() {
		return equip_OID;
	}

	public void setEquip_OID(String equip_OID) {
		this.equip_OID = equip_OID;
	}

	@XmlElement
	public String getRecipe_OID() {
		return recipe_OID;
	}

	public void setRecipe_OID(String recipe_OID) {
		this.recipe_OID = recipe_OID;
	}

	@XmlElement
	public String getStep_OID() {
		return step_OID;
	}

	public void setStep_OID(String step_OID) {
		this.step_OID = step_OID;
	}

	@XmlElement
	public String getHold_type() {
		return hold_type;
	}

	public void setHold_type(String hold_type) {
		this.hold_type = hold_type;
	}

	@XmlElement
	public String getHold_flag() {
		return hold_flag;
	}

	public void setHold_flag(String hold_flag) {
		this.hold_flag = hold_flag;
	}

}
