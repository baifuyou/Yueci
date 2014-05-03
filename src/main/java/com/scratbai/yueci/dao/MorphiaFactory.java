package com.scratbai.yueci.dao;

import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class MorphiaFactory extends AbstractFactoryBean<Morphia> {

	private String[] mapPackages;
	private String[] mapClasses;
	private boolean ignoreInvalidClasses;
	private static Morphia morphia;

	@Override
	protected Morphia createInstance() throws Exception {
		if (MorphiaFactory.morphia != null)
			return MorphiaFactory.morphia;
		Morphia morphia = new Morphia();
		if (mapPackages != null) {
			for (String pack : mapPackages) {
				morphia.mapPackage(pack, ignoreInvalidClasses);
			}
		}
		if (mapClasses != null) {
			for (String className : mapClasses) {
				morphia.map(Class.forName(className));
			}
		}
		MorphiaFactory.morphia = morphia;
		return MorphiaFactory.morphia;
	}

	@Override
	public Class<?> getObjectType() {
		return Morphia.class;
	}

	public String[] getMapPackages() {
		return mapPackages;
	}

	public void setMapPackages(String[] mapPackages) {
		this.mapPackages = mapPackages;
	}

	public String[] getMapClasses() {
		return mapClasses;
	}

	public void setMapClasses(String[] mapClasses) {
		this.mapClasses = mapClasses;
	}

	public boolean isIgnoreInvalidClasses() {
		return ignoreInvalidClasses;
	}

	public void setIgnoreInvalidClasses(boolean ignoreInvalidClasses) {
		this.ignoreInvalidClasses = ignoreInvalidClasses;
	}

}
