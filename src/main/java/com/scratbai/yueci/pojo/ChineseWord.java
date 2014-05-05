
package com.scratbai.yueci.pojo;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

@Entity
public class ChineseWord{
   	private List<SymbolC> symbols;
   	private String word_id;
   	private String word_name;

 	public List<SymbolC> getSymbols(){
		return this.symbols;
	}
	public void setSymbols(List<SymbolC> symbols){
		this.symbols = symbols;
	}
 	public String getWord_id(){
		return this.word_id;
	}
	public void setWord_id(String word_id){
		this.word_id = word_id;
	}
 	public String getWord_name(){
		return this.word_name;
	}
	public void setWord_name(String word_name){
		this.word_name = word_name;
	}
}
