
package com.scratbai.yueci.pojo;

import java.util.List;

import org.mongodb.morphia.annotations.*;

@Entity
public class ExchangeE{
   	private List<String> word_done;
   	private List<String> word_er;
   	private List<String> word_est;
   	private List<String> word_ing;
   	private List<String> word_past;
   	private List<String> word_pl;
   	private List<String> word_third;

 	public List<String> getWord_done(){
		return this.word_done;
	}
	public void setWord_done(List<String> word_done){
		this.word_done = word_done;
	}
 	public List<String> getWord_er(){
		return this.word_er;
	}
	public void setWord_er(List<String> word_er){
		this.word_er = word_er;
	}
 	public List<String> getWord_est(){
		return this.word_est;
	}
	public void setWord_est(List<String> word_est){
		this.word_est = word_est;
	}
 	public List<String> getWord_ing(){
		return this.word_ing;
	}
	public void setWord_ing(List<String> word_ing){
		this.word_ing = word_ing;
	}
 	public List<String> getWord_past(){
		return this.word_past;
	}
	public void setWord_past(List<String> word_past){
		this.word_past = word_past;
	}
 	public List<String> getWord_pl(){
		return this.word_pl;
	}
	public void setWord_pl(List<String> word_pl){
		this.word_pl = word_pl;
	}
 	public List<String> getWord_third(){
		return this.word_third;
	}
	public void setWord_third(List<String> word_third){
		this.word_third = word_third;
	}
}
