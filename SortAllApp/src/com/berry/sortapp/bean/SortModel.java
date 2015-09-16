package com.berry.sortapp.bean;

import java.util.List;

public class SortModel {

	/**
	 * 显示的数据
	 */
	private List<AppInfo> apps;
	/**
	 * 显示数据拼音的首字母
	 */
	private String sortLetters;

	public SortModel(List<AppInfo> apps, String sortLetters) {
		super();
		this.apps = apps;
		this.sortLetters = sortLetters;
	}

	public SortModel() {
		// TODO Auto-generated constructor stub
	}

	public List<AppInfo> getApps() {
		return apps;
	}

	public void setApps(List<AppInfo> apps) {
		this.apps = apps;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

}
