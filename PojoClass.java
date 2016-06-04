package com.project;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PojoClass {
	
	@Persistent
	public String name;
	
	
	public String getUname() {
		return name;
	}


	public void setUname(String name) {
		this.name = name;
	}
	
	
	@Persistent
	@PrimaryKey
	public String email;
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Persistent
	public String id;
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
}
