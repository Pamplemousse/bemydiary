package com.lesgrosspoof.bemydiary.entities;

public class ItemSelection extends Item {
	
	private int id_selection;
	private String id_site;
	private boolean isVisited;
	private int position;
	
	public int getId_selection() {
		return id_selection;
	}

	public void setId_selection(int id_selection) {
		this.id_selection = id_selection;
	}

	public String getId_site() {
		return id_site;
	}

	public void setId_site(String id_site) {
		this.id_site = id_site;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	private ItemSelection() {
	}
	
	public static ItemSelection copyFromItem(Item item) {
		
		ItemSelection itemSelection = new ItemSelection();
		
		itemSelection.setId(item.getId());
		itemSelection.setType(item.getType());
		itemSelection.setNom(item.getNom());
		itemSelection.setAdresse(item.getAdresse());
		itemSelection.setCode_postal(item.getCode_postal());
		itemSelection.setVille(item.getVille());
		itemSelection.setTelephone(item.getTelephone());
		itemSelection.setSite_web(item.getSite_web());
		itemSelection.setLatitude(item.getLatitude());
		itemSelection.setLongitude(item.getLongitude());
		itemSelection.setHandicap(item.getHandicap());
		itemSelection.setDetails(item.getDetails());
		
		itemSelection.setId_site(null);
		
		return itemSelection;
	}
	
	public String toString() {
		return (id + " -  " + nom + " - position: " + position);
	}
}
