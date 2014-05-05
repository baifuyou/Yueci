package com.scratbai.yueci.dao;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.*;
import com.scratbai.yueci.commons.CommonUtils;

public class MongoFactory extends AbstractFactoryBean<Mongo> {

	public final static String DEFAULT_MONGODB_CONFIG_FILE_PATH = "mongoConfig.properties";
	private static Mongo mongo;

	@Override
	protected Mongo createInstance() throws Exception {
		if (MongoFactory.mongo != null)
			return MongoFactory.mongo;
		InputStream configStream = this.getClass().getClassLoader()
				.getResourceAsStream(DEFAULT_MONGODB_CONFIG_FILE_PATH);
		Properties mongoConfig = new Properties();
		mongoConfig.load(configStream);
		String username = CommonUtils.getConfigValue(
				DEFAULT_MONGODB_CONFIG_FILE_PATH, "username");
		String password = CommonUtils.getConfigValue(
				DEFAULT_MONGODB_CONFIG_FILE_PATH, "password");
		String database = CommonUtils.getConfigValue(
				DEFAULT_MONGODB_CONFIG_FILE_PATH, "database");
		String serverAddressStr = CommonUtils.getConfigValue(
				DEFAULT_MONGODB_CONFIG_FILE_PATH, "server_address");
		int serverPort = Integer.parseInt(CommonUtils.getConfigValue(
				DEFAULT_MONGODB_CONFIG_FILE_PATH, "server_port"));
		logger.debug("username:" + username);
		logger.debug("password:" + password);
		logger.debug("database:" + database);
		MongoCredential mongoCredential = MongoCredential
				.createMongoCRCredential(username, database,
						password.toCharArray());
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(mongoCredential);
		ServerAddress serverAddress = new ServerAddress(serverAddressStr,
				serverPort);
		Mongo mongo = new MongoClient(serverAddress, credentials);
		MongoFactory.mongo = mongo;
		return MongoFactory.mongo;
	}

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

}
