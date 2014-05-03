package com.scratbai.yueci.dao;

import java.io.InputStream;
import java.util.Properties;

import org.mongodb.morphia.*;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.Mongo;

public class DatastoreFactory extends AbstractFactoryBean<Datastore> {

	private Morphia morphia;
	private Mongo mongo;
	private boolean toEnsureIndexes = false; // 是否确认索引存在，默认false
	private boolean toEnsureCaps = false; // 是否确认caps存在，默认false
	private boolean toEnsureIndexs;
	private static Datastore datastore;

	@Override
	protected Datastore createInstance() throws Exception {
		if (DatastoreFactory.datastore != null)
			return DatastoreFactory.datastore;
		InputStream configStream = this
				.getClass()
				.getClassLoader()
				.getResourceAsStream(
						MongoFactory.DEFAULT_MONGODB_CONFIG_FILE_PATH);
		Properties mongoConfig = new Properties();
		mongoConfig.load(configStream);
		String database = mongoConfig.getProperty("database");
		Datastore datastore = morphia.createDatastore(mongo, database);
		if (toEnsureIndexs) 
			datastore.ensureIndexes();
		if (toEnsureCaps) 
			datastore.ensureCaps();
		DatastoreFactory.datastore = datastore;
		return DatastoreFactory.datastore;
	}

	@Override
	public Class<Datastore> getObjectType() {
		return Datastore.class;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public boolean isToEnsureIndexes() {
		return toEnsureIndexes;
	}

	public void setToEnsureIndexes(boolean toEnsureIndexes) {
		this.toEnsureIndexes = toEnsureIndexes;
	}

	public boolean isToEnsureCaps() {
		return toEnsureCaps;
	}

	public void setToEnsureCaps(boolean toEnsureCaps) {
		this.toEnsureCaps = toEnsureCaps;
	}

}
