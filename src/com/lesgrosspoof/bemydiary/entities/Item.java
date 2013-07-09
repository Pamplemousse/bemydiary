package com.lesgrosspoof.bemydiary.entities;
import java.util.List;

import com.lesgrosspoof.bemydiary.models.Items;

public class Item {
	protected int id;
	protected int type;
	protected String nom;
	protected String adresse;
	protected String code_postal;
	protected String ville;
	protected String telephone;
	protected String site_web;
	private double longitude;
	protected double latitude;
	protected String handicap;
	protected String details;
	
	public Item(){
		
	}
	
	public static Item getItem(int id){
		return Items.getInstance().get(id);
	}
	
	public static List<Item> getItems(List<Integer> ids) {
		return Items.getInstance().searchById(ids);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getCode_postal() {
		return code_postal;
	}

	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSite_web() {
		return site_web;
	}

	public void setSite_web(String site_web) {
		this.site_web = site_web;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getHandicap() {
		return handicap;
	}

	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public String toString() {
		return (this.id + " " + this.nom);
	}
}
