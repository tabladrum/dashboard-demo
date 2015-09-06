package com.vaadin.demo.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

public class SongItem {
	private String type;
	private String songName;
	private List<MusicMedia> materials = new ArrayList<MusicMedia>();
	
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
	public List<MusicMedia> getMaterials() {
		return materials;
	}
	public void setMaterials(List<MusicMedia> materials) {
		this.materials = materials;
	}

}
