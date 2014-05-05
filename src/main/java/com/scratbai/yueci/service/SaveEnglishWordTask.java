package com.scratbai.yueci.service;

import java.io.IOException;
import java.net.*;
import java.util.List;

import org.json.JSONException;
import org.slf4j.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.*;
import com.qiniu.api.rs.PutPolicy;
import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.dao.UserDao;
import com.scratbai.yueci.pojo.*;

public class SaveEnglishWordTask implements Runnable {

	private UserDao userDao;
	private String protoData;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public SaveEnglishWordTask(String protoData, UserDao userDao) {
		this.protoData = protoData;
		this.userDao = userDao;
	}

	@Override
	public void run() {
		saveWordData();
	}

	private void saveWordData() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			EnglishWord eWord = mapper.readValue(protoData, EnglishWord.class);
			saveMp3ToCDN(eWord);
			userDao.addEnglishWord(eWord);
		} catch (JsonParseException e) {
			logger.error("json to EnglishWord parse error,Exception Message:\n" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.error("json to EnglishWord mapping error,Exception Message:\n" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("json to IO error,Exception Message:\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * 保存eWord的所有mp3内容到CDN上，在eWord中用新的路径覆盖之前的路径
	 */
	private void saveMp3ToCDN(EnglishWord eWord) {
		String wordName = eWord.getWord_name();
		List<SymbolE> symbols = eWord.getSymbols();
		for (int i = 0; i < symbols.size(); i++) {
			SymbolE symbol = symbols.get(i);
			saveMp3ToCDN(wordName, symbol, i, "en");
			saveMp3ToCDN(wordName, symbol, i, "am");
			saveMp3ToCDN(wordName, symbol, i, "tts");
		}
	}

	/*
	 * 保存指定symbol中指定类型（en，am，tts）的mp3内容
	 * 如果在指定symbol中没有指定类型的mp3内容，不做任何处理，直接返回
	 */
	private void saveMp3ToCDN(String wordName, SymbolE symbol, int symbolIndex,
			String mp3Type) {
		String saveKey = generateMp3SaveKey(wordName, symbolIndex, mp3Type);
		String savePath = generateMP3SavePath(saveKey);
		String protoPath = "";
		if (mp3Type.equals("en")) {
			protoPath = symbol.getPh_en_mp3();
			if (protoPath == null || protoPath.equals("")) {
				return;
			}
			symbol.setPh_en_mp3(savePath);
		}
		if (mp3Type.equals("am")) {
			protoPath = symbol.getPh_am_mp3();
			if (protoPath == null || protoPath.equals("")) {
				return;
			}
			symbol.setPh_am_mp3(savePath);
		}
		if (mp3Type.equals("tts")) {
			protoPath = symbol.getPh_tts_mp3();
			if (protoPath == null || protoPath.equals("")) {
				return;
			}
			symbol.setPh_tts_mp3(savePath);
		}
		try {
			saveDataToCDN(saveKey, protoPath);
		} catch (AuthException e) {
			logger.error("cdn auth error,Message:" + e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			logger.error("cdn json error,Message:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("cdn io error:not connection cdn server, Message:" + e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * 访问cdn api，把指定path（http path）的数据存储到cdn上
	 */
	private void saveDataToCDN(String saveKey, String protoPath)
			throws AuthException, JSONException, IOException {
		String cdnConfig = "cdnConfig.properties";
		Config.ACCESS_KEY = CommonUtils.getConfigValue(cdnConfig, "access_key");
		Config.SECRET_KEY = CommonUtils.getConfigValue(cdnConfig, "secret_key");
		logger.debug("assess_key:" + Config.ACCESS_KEY);
		logger.debug("secret_key:" + Config.SECRET_KEY);
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		String bucketName = CommonUtils.getConfigValue(cdnConfig, "bucket_name");
		PutPolicy putPolicy = new PutPolicy(bucketName);
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		URL url = new URL(protoPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		PutRet ret = IoApi.Put(uptoken, saveKey, connection.getInputStream(),
				extra);
		if (!ret.ok()) {
			logger.error("save data to cdn error\n error code:"
					+ ret.statusCode + "\nException:" + ret.getException());
		}
	}

	/*
	 * 生成存储路径，生成规则是： domain + key domain来自配置文件cdnConfig
	 */
	private String generateMP3SavePath(String saveKey) {
		String cdnDomain = CommonUtils.getConfigValue("cdnConfig.properties",
				"domain");
		return cdnDomain + saveKey;
	}

	/*
	 * 生成mp3在cdn上的存储key，按照以下方式生成key：
	 * pronounce/{wordName}/{symbolIndex}/{mp3Type}/{md5hex(wordName)}
	 */
	private String generateMp3SaveKey(String wordName, int symbolIndex,
			String mp3Type) {
		String mp3MD5 = CommonUtils.md5Hex(wordName);
		return String.format("pronounce/%s/%d/%s/%s.mp3", wordName,
				symbolIndex, mp3Type, mp3MD5);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
