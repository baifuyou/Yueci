
package com.scratbai.yueci.pojo;

import java.util.List;

import org.mongodb.morphia.annotations.*;

@Entity
public class PartE{
   	private List<String> means;
   	private String part;

 	public List<String> getMeans(){
		return this.means;
	}
	public void setMeans(List<String> means){
		this.means = means;
	}
 	public String getPart(){
		return this.part;
	}
	public void setPart(String part){
		this.part = part;
	}
}
