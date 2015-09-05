package com.vaadin.demo.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

public class PracticeItem {

	private String type;
	private List<Media> materials = new ArrayList<Media>();

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

	public List<Media> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Media> materials) {
		this.materials = materials;
	}

	public void addMaterial(Media material) {
		this.materials.add(material);
	}

	public void deleteMaterial(Media material) {
		this.materials.remove(material);
	}

}
