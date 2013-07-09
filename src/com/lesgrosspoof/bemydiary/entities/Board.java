package com.lesgrosspoof.bemydiary.entities;

import java.util.List;

import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Medias;
import com.lesgrosspoof.bemydiary.models.Selections;

public class Board {
	
	public static int currentIndexBoard;

	private int id;
	private String id_site;
	private String name;
	private String date_create;
	private String url;
	private String date_end;
	private String password;
	
	private int isFinish;
	private int isPublish;
	private static int currentItemId;
	
	private List<ItemSelection> selection;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdSite() {
		return id_site;
	}

	public void setIdSite(String id_site) {
		this.id_site = id_site;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}

	public int getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(int isPublish) {
		this.isPublish = isPublish;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return date_create + " " + date_end;
	}

	public String getDate_create() {
		return date_create;
	}

	public void setDate_create(String date_create) {
		this.date_create = date_create;
	}

	public String getDate_end() {
		return date_end;
	}

	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}

	public void refreshSelection() {
		selection = Selections.getInstance().get(currentIndexBoard);
	}
	
	public List<ItemSelection> getSelection() {
		return selection;
	}
	
	public void add(int id_liste) {
		Selections.getInstance().add(id, id_liste);
		
		refreshSelection();
	}
	
	public void remove(int id_selection) {
		Selections.getInstance().delete(id, id_selection);
	}
	
	public void up(int id_selection){
		Selections.getInstance().up(id, id_selection);
		
		refreshSelection();
	}
	
	public void down(int id_selection){
		Selections.getInstance().down(id, id_selection);
		
		refreshSelection();
	}
	
	public static Board getCurrentBoard() {
		return Boards.getInstance().get(currentIndexBoard);
	}
	
	public void debug() {
		System.out.println("-- debug --");
		for(ItemSelection item : selection) {
			System.out.println(item.getNom() + " position : " + item.getPosition());
		}
	}
	
	public static void setCurrentItemId(int id) {
		currentItemId = id;
	}
	
	public static int getCurrentItemId() {
		return currentItemId;
	}
	
	public void addMedia(int id_lieu, String file) {
		Medias.getInstance().add(id, id_lieu, Medias.PHOTO ,file);
	}
	
	public void updateInDb() {
		Boards.getInstance().update(this);
	}
	
	public String toString()
	{
		return "(Board) id : "+id+", id_site : "+id_site+", name : "+name;
	}
	
	
}