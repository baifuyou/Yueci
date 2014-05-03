package com.scratbai.yueci.dao;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.*;

public class DBFactory extends AbstractFactoryBean<DB> {
	
	private Mongo mongo;
	private static DB db;

	@Override
	protected DB createInstance() throws Exception {
		if (db != null) 
			return db;
		InputStream configStream = this.getClass().getClassLoader()
				.getResourceAsStream(MongoFactory.DEFAULT_MONGODB_CONFIG_FILE_PATH);
		Properties mongoConfig = new Properties();
		mongoConfig.load(configStream);
		String database = mongoConfig.getProperty("database");
		DB db = mongo.getDB(database);
		DBFactory.db = db;
		return DBFactory.db;
	}

	@Override
	public Class<DB> getObjectType() {
		return DB.class;
	}

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}


}
