package com.lesgrosspoof.bemydiary;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.MapActivity;
import com.lesgrosspoof.bemydiary.R.id;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.ItemSelectionToUpload;
import com.lesgrosspoof.bemydiary.entities.Media;
import com.lesgrosspoof.bemydiary.entities.MediaToUpload;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Medias;
import com.lesgrosspoof.bemydiary.models.Selections;
import com.lesgrosspoof.bemydiary.network.AsyncResultListener;
import com.lesgrosspoof.bemydiary.network.AuthManager;
import com.lesgrosspoof.bemydiary.network.AsyncRequest;
import com.lesgrosspoof.bemydiary.network.ItemSelectionUploader;
import com.lesgrosspoof.bemydiary.network.MediaUploader;

public abstract class AbstractActivity extends MapActivity implements AsyncResultListener {
	
	private int placeCounter = 0;
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		System.out.println("preparing menu");
	    if (AuthManager.getInstance().isLoading() || ItemSelectionUploader.getInstance().isUploading() || MediaUploader.getInstance().isUploading())
	    {
	    	System.out.println("disabling publish button");
	    	menu.findItem(R.id.menu_publish).setEnabled(false);
	    	//menu.findItem(R.id.menu_publish).get .setTextColor(Color.WHITE);
	    }
	    	
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;

		switch (item.getItemId()) {
			case R.id.menu_settings:
				System.out.println("settings");
				myIntent = new Intent(getBaseContext(), SettingsActivity.class);
				startActivityForResult(myIntent, 0);
				return true;
			case R.id.menu_help:
				System.out.println("help");
				myIntent = new Intent(getBaseContext(), HelpActivity.class);
				startActivityForResult(myIntent, 0);
				return true;
			case R.id.menu_close:
				System.out.println("close");
				Boards.getInstance().closeCurrentBoard();
				myIntent = new Intent(getBaseContext(), OldListActivity.class);
				startActivityForResult(myIntent, 0);
				return true;
			case R.id.menu_publish:
				System.out.println("publish");
				AuthManager.getInstance().authenticate(this);
				return true;
		}
		return false;
	}
	
	public void callback(String result, int type) {
		try {
			JSONObject json = new JSONObject(result);
			
			switch(type){
			case AsyncRequest.GET_CSRF:
				
				AuthManager.getInstance().setCsrf_token(json.getString("csrf_token"));
				
				uploadCurrentBoard();
				
				break;
				
			case AsyncRequest.GET_NEW_BOARD:
				Board currentBoard = Board.getCurrentBoard();
				
				currentBoard.setIdSite(json.getString("_id"));
				currentBoard.setUrl(json.getString("url"));
				currentBoard.setName(json.getString("name"));
				
				//System.out.println(json.toString());
				
				currentBoard.updateInDb();
				
				uploadCurrentPlaces();
				
				break;
				
			case AsyncRequest.UPLOAD_MEDIA:
				
				System.out.println("response : " + json.toString());
				
				
				break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void uploadCurrentBoard(){
		// 1. Ask for a new board
		// 		GET on http://www.bemydiary.fr/boards/new
		//
		// 2. Parse the JSON response to retrieve _id field.
		
		Board currentBoard = Board.getCurrentBoard();
		
		String id_site = currentBoard.getIdSite();
		
		if(id_site != null)
		{
			if(id_site.length() > 0)
			{
				System.out.println("Board already uploaded ! updating places");
				
				uploadCurrentPlaces();
			}
		}
		else
		{
			System.out.println("id_site is null, creating new board");
			
			AsyncRequest request = new AsyncRequest(this, AsyncRequest.GET_NEW_BOARD, "http://dev.bemydiary.fr/boards/new", "GET", AuthManager.getInstance().getCookie());
			HashMap params = new HashMap<String, String>();
			request.execute(params);
		}
		
	}
	
	public void uploadCurrentPlaces() {
		
		ItemSelectionUploader.getInstance().setCallbackActivity(this);
		
		int board_id = Boards.getInstance().getCurrentBoardId();
		String board_id_site = Boards.getInstance().get(board_id).getIdSite();
		
		List<ItemSelectionToUpload> selectionsToUpload = new ArrayList<ItemSelectionToUpload>();		
		List<ItemSelection> selections = Selections.getInstance().get(Boards.getInstance().getCurrentBoardId());
		
		boolean uploadASelection = false;
		
		for(ItemSelection selection : selections)
		{
			if(selection.getId_site() != null)
			{
				if(selection.getId_site().length() > 0)
				{
					System.out.println("selection already uploaded !");
				}
			}
			else
			{
				System.out.println("uploading new place...");
				uploadASelection = true;
				placeCounter++;
				selectionsToUpload.add(new ItemSelectionToUpload(selection, String.valueOf(board_id_site), String.valueOf(selection.getId())));
			}	
		}
		
		ItemSelectionUploader.getInstance().addToQueue(selectionsToUpload.toArray(new ItemSelectionToUpload[selectionsToUpload.size()]));
		
		if(!uploadASelection)
		{
			System.out.println("places are already up to date, don't wait to upload medias !");
			uploadCurrentMedias();
		}
	}
	
	public void placeUploaded()
	{
		placeCounter--;
		
		System.out.println("placecounter-- ("+placeCounter+")");
		
		if(placeCounter <= 0)
		{
			System.out.println("placecounter = 0, uploading medias");
			uploadCurrentMedias();
		}
	}
	
	public void uploadCurrentMedias() {
		// POST params :
		// medium[board_id]
		// medium[place_id]
		// medium[upload] (base64 string)
		// authenticity_token (csrf-token)
		
		MediaUploader.getInstance().setCallbackActivity(this);
		
		int board_id = Boards.getInstance().getCurrentBoardId();
		String board_id_site = Boards.getInstance().get(board_id).getIdSite();
		
		List<ItemSelection> lieux = Selections.getInstance().get(Boards.getInstance().getCurrentBoardId());
		
		for(ItemSelection lieu : lieux)
		{
			List<Media> medias = Medias.getInstance().getPhotos(board_id, lieu.getId());
			
			for(Media media : medias)
			{
				if(media.getId_site() != null)
				{
					if(media.getId_site().length() > 0)
					{
						System.out.println("media already uploaded !");
					}
				}
				else
				{
					System.out.println("uploading new media...");
					
					MediaUploader.getInstance().addToQueue(new MediaToUpload(media, String.valueOf(board_id_site), String.valueOf(lieu.getId())));
					
					
				}	
			}
		}	
	}
	
	public void getCsrf()
	{
		AsyncRequest request = new AsyncRequest(this, AsyncRequest.GET_CSRF, "http://dev.bemydiary.fr/csrf-token", "GET", AuthManager.getInstance().getCookie());
		HashMap params = new HashMap<String, String>();
		request.execute(params);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
