package com.scratbai.yueci.pojo;

import org.mongodb.morphia.annotations.Entity;

public class WordsTableItem {
	private String name;
	private int frequency;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
