
package com.scratbai.yueci.pojo;

import java.util.List;

public class SymbolC{
   	private List<PartC> parts;
   	private String ph_am_mp3;
   	private String ph_en_mp3;
   	private String ph_other;
   	private String ph_tts_mp3;
   	private String symbol_id;
   	private String symbol_mp3;
   	private String word_id;
   	private String word_symbol;

 	public List<PartC> getParts(){
		return this.parts;
	}
	public void setParts(List<PartC> parts){
		this.parts = parts;
	}
 	public String getPh_am_mp3(){
		return this.ph_am_mp3;
	}
	public void setPh_am_mp3(String ph_am_mp3){
		this.ph_am_mp3 = ph_am_mp3;
	}
 	public String getPh_en_mp3(){
		return this.ph_en_mp3;
	}
	public void setPh_en_mp3(String ph_en_mp3){
		this.ph_en_mp3 = ph_en_mp3;
	}
 	public String getPh_other(){
		return this.ph_other;
	}
	public void setPh_other(String ph_other){
		this.ph_other = ph_other;
	}
 	public String getPh_tts_mp3(){
		return this.ph_tts_mp3;
	}
	public void setPh_tts_mp3(String ph_tts_mp3){
		this.ph_tts_mp3 = ph_tts_mp3;
	}
 	public String getSymbol_id(){
		return this.symbol_id;
	}
	public void setSymbol_id(String symbol_id){
		this.symbol_id = symbol_id;
	}
 	public String getSymbol_mp3(){
		return this.symbol_mp3;
	}
	public void setSymbol_mp3(String symbol_mp3){
		this.symbol_mp3 = symbol_mp3;
	}
 	public String getWord_id(){
		return this.word_id;
	}
	public void setWord_id(String word_id){
		this.word_id = word_id;
	}
 	public String getWord_symbol(){
		return this.word_symbol;
	}
	public void setWord_symbol(String word_symbol){
		this.word_symbol = word_symbol;
	}
}
