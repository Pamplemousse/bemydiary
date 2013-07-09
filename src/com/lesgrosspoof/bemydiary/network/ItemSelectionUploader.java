package com.lesgrosspoof.bemydiary.network;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.lesgrosspoof.bemydiary.AbstractActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.ItemSelectionToUpload;
import com.lesgrosspoof.bemydiary.entities.Media;
import com.lesgrosspoof.bemydiary.entities.MediaToUpload;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Medias;
import com.lesgrosspoof.bemydiary.models.Selections;

public class ItemSelectionUploader implements AsyncResultListener
{
	//private static final int NOTIFICATION_SERVICE = 1337;

	private static ItemSelectionUploader _instance;
	
	private ArrayList<ItemSelectionToUpload> queue;
	
	private boolean isUploading;
	
	private AbstractActivity activity;
	
	public static ItemSelectionUploader getInstance() {
		if (_instance == null)
			_instance = new ItemSelectionUploader();
		return _instance;
	}
	
	private ItemSelectionUploader()
	{
		queue = new ArrayList<ItemSelectionToUpload>();
	}
	
	public void setCallbackActivity(AbstractActivity activity)
	{
		this.activity = activity;
	}
	
	public void addToQueue(ItemSelectionToUpload... m) // pour balancer tous les lieux à la fois
	{
		for(int i = 0; i < m.length; i++)
		{
			queue.add(m[i]);

			if(!isUploading)
			{
				System.out.println("connection is not busy, let's upload this selection !");
				notifyLoading();
				uploadNext();
			}
			else
			{
				System.out.println("oops, must wait until previous upload finishes...");
			}
		}
	}
	
	private void uploadNext()
	{
		if(queue.size() > 0)
		{
			isUploading = true;
			
			ItemSelectionToUpload item = queue.get(0);

			System.out.println("beginning upload of selection");
			
			AsyncRequest request = new AsyncRequest(this, AsyncRequest.UPLOAD_SELECTION, "http://dev.bemydiary.fr/selections.json", "POST", AuthManager.getInstance().getCookie());
			HashMap<String, String> params = new HashMap<String, String>();

			params.put("selection[position]", String.valueOf(item.getSelection().getPosition()));
			params.put("selection[isVisited]", String.valueOf(item.getSelection().isVisited()));
			params.put("selection[board_id]", item.getId_board());
			params.put("selection[place_id]", item.getId_lieu());
			
			params.put("authenticity_token", AuthManager.getInstance().getCsrf_token());
			
			System.out.println("csrf : "+AuthManager.getInstance().getCsrf_token());
			
			request.execute(params);
		}
		else
		{
			System.out.println("Selection queue is empty, my job here is done !");
			this.deleteNotification();
		}
	}
	
	private void uploadFinished()
	{
		System.out.println("selection upload finished.");
		
		activity.placeUploaded();

		isUploading = false;
		
		queue.remove(0);
		
		uploadNext();
	}

	public void callback(String result, int type) 
	{
		JSONObject json = null;
		
		try 
		{
			json = new JSONObject(result);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		if(type == AsyncRequest.UPLOAD_SELECTION)
		{
			if(json != null)
			{
				System.out.println("Response : "+json.toString());
				
				ItemSelection selection = queue.get(0).getSelection();
				
				try 
				{
					selection.setId_site(json.getString("_id"));
					Selections.getInstance().setSiteId(json.getString("_id"), selection.getId());
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
			
			uploadFinished();
		}
	}
	
	private final void notifyLoading()
	{
		Notification notification = new Notification(R.drawable.wheelanim, null, System.currentTimeMillis());

			PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0,
			    new Intent(), 0);

			notification.flags |= Notification.FLAG_NO_CLEAR;

			notification.setLatestEventInfo(activity, "Publication du carnet",
			    "Mise à jour de la liste des lieux...", pendingIntent);

			((NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE)).notify(
			    1337, notification);
	}
	
	private final void deleteNotification()
	{
		((NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE)).cancel(1337);
	}

	public boolean isUploading() {
		return isUploading;
	}
	
}
