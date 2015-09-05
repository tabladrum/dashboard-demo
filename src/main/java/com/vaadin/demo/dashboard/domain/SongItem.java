package com.vaadin.demo.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

public class SongItem {
	private String type;
	private String songName;
	private List<Media> materials = new ArrayList<Media>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public List<Media> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Media> materials) {
		this.materials = materials;
	}

}
