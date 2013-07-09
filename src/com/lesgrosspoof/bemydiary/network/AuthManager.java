package com.lesgrosspoof.bemydiary.network;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lesgrosspoof.bemydiary.AbstractActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.models.Boards;

public class AuthManager 
{
	private static AuthManager _instance;
	private AbstractActivity parent;
	private WebView webview;
	
	private String cookie;
	private String csrf_token;
	
	private boolean isLoading;
	
	public static AuthManager getInstance() {
		if (_instance == null)
			_instance = new AuthManager();
		return _instance;
	}
	
	private AuthManager()
	{
		cookie = "";
	}
	
	public void authenticate()
	{
		System.out.println("auth");
	}
	public void authenticate(AbstractActivity _parent)
	{		
		isLoading = true;
		
		parent = _parent;
		
		notifyLoading();
		
		System.out.println("auth");
		webview = new WebView(parent);
		webview.loadUrl("http://dev.bemydiary.fr/users/auth/google_oauth2");
		webview.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        if(!url.equals("http://dev.bemydiary.fr/"))
		        	view.loadUrl(url);
		        else{
		        	view.setVisibility(View.GONE);
		        	CookieManager cookieManager = CookieManager.getInstance();
		        	String brutCookie = cookieManager.getCookie("http://dev.bemydiary.fr/");
		        	String cookies[] = brutCookie.split(";");
		        	
		        	for (String _cookie : cookies) {
						if(_cookie.contains("_web_session"))
							cookie = _cookie.split("=")[1];
					}
	                
		        	parent.getCsrf();
		        	
		        	deleteNotification();
		        	isLoading = false;
		        }
		        System.out.println(url);
		        return false; // then it is not handled by default action
		   }
		});
		webview.getSettings().setJavaScriptEnabled(true);
		parent.addContentView(webview,  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		System.out.println("setCookie(String cookie) => NOPE !");
		//this.cookie = cookie;
	}

	public String getCsrf_token() {
		return csrf_token;
	}

	public void setCsrf_token(String csrf_token) {
		this.csrf_token = csrf_token;
	}
	
	private final void notifyLoading()
	{
		if(parent == null)
		{
			System.out.println("Parent activity is null");
		}
		else
		{
			Notification notification = new Notification(R.drawable.wheelanim, null, System.currentTimeMillis());

			PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0,
			    new Intent(), 0);

			notification.flags |= Notification.FLAG_NO_CLEAR;

			notification.setLatestEventInfo(parent, "Authentification",
			    "Authentification de l'utilisateur en cours...", pendingIntent);

			((NotificationManager) parent.getSystemService(parent.NOTIFICATION_SERVICE)).notify(
			    1336, notification);
		}
	}
	
	private final void deleteNotification()
	{
		((NotificationManager) parent.getSystemService(parent.NOTIFICATION_SERVICE)).cancel(1336);
	}

	public boolean isLoading() {
		return isLoading;
	}
}
