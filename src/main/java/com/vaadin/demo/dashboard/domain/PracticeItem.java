package com.vaadin.demo.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

public class PracticeItem {

	private String type;
	private List<MusicMedia> materials = new ArrayList<MusicMedia>();

	private String name;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MusicMedia> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MusicMedia> materials) {
		this.materials = materials;
	}

	public void addMaterial(MusicMedia material) {
		this.materials.add(material);
	}

	public void deleteMaterial(MusicMedia material) {
		this.materials.remove(material);
	}

}
