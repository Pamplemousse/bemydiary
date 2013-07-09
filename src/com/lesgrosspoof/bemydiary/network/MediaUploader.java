package com.lesgrosspoof.bemydiary.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.lesgrosspoof.bemydiary.AbstractActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.Media;
import com.lesgrosspoof.bemydiary.entities.MediaToUpload;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Medias;
import com.lesgrosspoof.bemydiary.models.Selections;

public class MediaUploader implements AsyncResultListener
{
	private static MediaUploader _instance;
	
	private ArrayList<MediaToUpload> queue;
	
	private boolean isUploading;
	
	private AbstractActivity activity;
	
	public static MediaUploader getInstance() {
		if (_instance == null)
			_instance = new MediaUploader();
		return _instance;
	}
	
	public void setCallbackActivity(AbstractActivity activity)
	{
		this.activity = activity;
	}
	
	private MediaUploader()
	{
		queue = new ArrayList<MediaToUpload>();
	}
	
	public void addToQueue(MediaToUpload m)
	{
		queue.add(m);
		
		if(!isUploading)
		{
			System.out.println("connection is not busy, let's upload !");
			notifyLoading();
			uploadNext();
		}
		else
		{
			System.out.println("oops, must wait until previous upload finishes...");
		}
		
		System.out.println("queue : "+queue.toString());
	}
	
	private void uploadNext()
	{
		if(queue.size() > 0)
		{
			System.out.println("beginning upload...");
			isUploading = true;
			
			MediaToUpload media = queue.get(0);
			
			AsyncRequest request = new AsyncRequest(this, AsyncRequest.UPLOAD_MEDIA, "http://dev.bemydiary.fr/media.json", "POST", AuthManager.getInstance().getCookie());
			HashMap<String, String> params = new HashMap<String, String>();
			
			String id_selection_site = null;
			
			List<ItemSelection> selections = Selections.getInstance().get(Boards.getInstance().getCurrentBoardId());
			
			for(ItemSelection item : selections)
			{
				if(item.getId() == Integer.parseInt(media.getId_lieu()))
				{
					id_selection_site = item.getId_site();
					break;
				}
			}

			params.put("medium[selection_id]", id_selection_site);
			params.put("authenticity_token", AuthManager.getInstance().getCsrf_token());
			
			System.out.println("csrf : "+AuthManager.getInstance().getCsrf_token());
			
			params.put("medium[upload]", media.getMedia().getContent());
			
			request.execute(params);
		}
		else
		{
			System.out.println("Queue is empty, my job here is done !");
			this.deleteNotification();
		}
	}
	
	private void uploadFinished()
	{
		System.out.println("upload finished.");

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
		
		if(type == AsyncRequest.UPLOAD_MEDIA)
		{
			if(json != null)
			{
				System.out.println("Response : "+json.toString());
				
				Media lastMedia = queue.get(0).getMedia();
				
				try 
				{
					lastMedia.setId_site(json.getString("_id"));
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				Medias.getInstance().update(lastMedia);
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
			    "Mise à jour des médias...", pendingIntent);

			((NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE)).notify(
			    1338, notification);
	}
	
	private final void deleteNotification()
	{
		((NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE)).cancel(1338);
	}
	
	public boolean isUploading() {
		return isUploading;
	}
	
}
