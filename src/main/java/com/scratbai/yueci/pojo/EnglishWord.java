
package com.scratbai.yueci.pojo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import com.fasterxml.jackson.annotation.*;

@Entity
public class EnglishWord{
	@Id
	@JsonIgnore
	private ObjectId id;
   	private ExchangeE exchange;
   	private int is_CRI;
   	@Embedded
   	private List<String> items;
   	@Embedded
   	private List<SymbolE> symbols;
   	private String word_name;

 	public ExchangeE getExchange(){
		return this.exchange;
	}
	public void setExchange(ExchangeE exchange){
		this.exchange = exchange;
	}
 	public int getIs_CRI(){
		return this.is_CRI;
	}
	public void setIs_CRI(int is_CRI){
		this.is_CRI = is_CRI;
	}
 	public List<String> getItems(){
		return this.items;
	}
	public void setItems(List<String> items){
		this.items = items;
	}
 	public List<SymbolE> getSymbols(){
		return this.symbols;
	}
	public void setSymbols(List<SymbolE> symbols){
		this.symbols = symbols;
	}
 	public String getWord_name(){
		return this.word_name;
	}
	public void setWord_name(String word_name){
		this.word_name = word_name;
	}
}
