package com.lesgrosspoof.bemydiary.entities;

public class ItemSelectionToUpload 
{
	private ItemSelection selection;
	private String id_board;
	private String id_lieu;
	
	public ItemSelectionToUpload(ItemSelection selection, String id_board, String id_lieu)
	{
		this.selection = selection;
		this.id_board = id_board;
		this.id_lieu = id_lieu;
	}

	public ItemSelection getSelection() {
		return selection;
	}

	public String getId_board() {
		return id_board;
	}

	public String getId_lieu() {
		return id_lieu;
	}
	
	
}
