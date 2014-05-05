package com.scratbai.yueci.pojo;

import java.util.*;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("words")
public class WordReference {
	@Id
	private ObjectId id;
	private String word;
	private Set<String> relatedWords;
	private String uid;
	private boolean isChinese;
	
	public WordReference() {
		
	}
	
	public WordReference(String word, String uid) {
		this.word = word;
		this.uid = uid;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Set<String> getRelatedWords() {
		return relatedWords;
	}
	public void setRelatedWords(Set<String> relatedWords) {
		this.relatedWords = relatedWords;
	}
	
	@Override
	public boolean equals(Object object) {
		WordReference wordRef = object == null ? null : (WordReference) object;
		return wordRef == null ? false : object instanceof WordReference && wordRef.getUid().equals(uid) && wordRef.getWord().equals(word);
	}
	
	@Override
	public int hashCode() {
		return (uid + word).hashCode();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean getIsChinese() {
		return isChinese;
	}

	public void setIsChinese(boolean isChinese) {
		this.isChinese = isChinese;
	}
}
