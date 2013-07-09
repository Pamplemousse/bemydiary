package com.lesgrosspoof.bemydiary.entities;

import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Medias;

public class Media {
	private int _id;
	private int id_board;
	private int id_list;
	private String id_site;
	private int type;
	private String content;
	
	public Media(int _id, int id_board, int id_list, int type, String content, String _id_site) {
		super();
		this._id = _id;
		this.id_site = _id_site;
		this.id_board = id_board;
		this.id_list = id_list;
		this.type = type;
		this.content = content;
	}
	
	public int get_id() {
		return _id;
	}
	public int getId_board() {
		return id_board;
	}
	public int getId_list() {
		return id_list;
	}
	public int getType() {
		return type;
	}
	public String getContent() {
		return content;
	}

	public String getId_site() {
		return id_site;
	}

	public void setId_site(String id_site) {
		this.id_site = id_site;
	}
	
	public void updateInDb() {
		Medias.getInstance().update(this);
	}
}
