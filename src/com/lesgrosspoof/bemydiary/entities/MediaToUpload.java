package com.lesgrosspoof.bemydiary.entities;

public class MediaToUpload 
{
	private Media media;
	private String id_board;
	private String id_lieu;
	
	public MediaToUpload(Media media, String id_board, String id_lieu)
	{
		this.media = media;
		this.id_board = id_board;
		this.id_lieu = id_lieu;
	}

	public Media getMedia() {
		return media;
	}

	public String getId_board() {
		return id_board;
	}

	public String getId_lieu() {
		return id_lieu;
	}
	
	
}
