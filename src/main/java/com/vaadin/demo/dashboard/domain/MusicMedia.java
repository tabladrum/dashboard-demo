package com.vaadin.demo.dashboard.domain;

public class MusicMedia {
	private String url;
	private String name;
	private String desc;
	private MusicMediaType type;
	


	public MusicMediaType getType() {
		return type;
	}

	public void setType(MusicMediaType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
