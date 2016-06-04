package com.project;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class StorePojo {
@Persistent
private String update;

public String getUpdate() {
	return update;
}
public void setUpdate(String update) {
	this.update = update;
}
@Persistent
private String date;

public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
@Persistent

String email;

public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

}
