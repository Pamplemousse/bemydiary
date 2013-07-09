package com.lesgrosspoof.bemydiary.network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;

public class AsyncRequest extends AsyncTask<HashMap<String, String>, Void, String>
{
	public final static int GET_NEW_BOARD = 1;
	public final static int UPLOAD_MEDIA = 2;
	public final static int UPLOAD_SELECTION = 3;
	public final static int GET_CSRF = 4;
    private AsyncResultListener listener;
    private int type;
    private String url;
    private String method;
    private String cookie;

    public AsyncRequest(AsyncResultListener listener, int type, String url, String method, String cookie) {
    	this.listener = listener;
    	this.url = url;
    	this.type = type;
    	this.method = method;
    	this.cookie = cookie;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(String result) {
    	listener.callback(result, type);
    }

	@Override
	protected String doInBackground(HashMap<String, String>... params) {
		
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		
		HttpGet getRequest = null;
		HttpPost postRequest = null; 
		
		if(method.equals("POST"))
		{
			postRequest = new HttpPost(url);
			
			MultipartEntity entity = new MultipartEntity();

			
			if(cookie.length() > 0)
			{
				postRequest.setHeader("Cookie", "_web_session="+cookie);
			}
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        
	        Iterator it = params[0].entrySet().iterator();
	        
	        while (it.hasNext()) {

	            Map.Entry pairs = (Map.Entry)it.next();
	            
	            if(pairs.getKey().toString().equals("medium[upload]"))
	            {
	            	// magic file upload system
	            	// modify at your own risk
	            	
	            	File file = new File(pairs.getValue().toString());
	            	
	            	System.out.println("loading file "+pairs.getValue().toString()+" ("+file.getName()+")");

	            	
	            	String type = "";
	            	
	            	String extension = file.getName().substring(file.getName().length()-3, file.getName().length());
	            	
	            	System.out.println("extension : "+extension);
	            	
	            	if(extension.equals("jpg"))
	            	{
	            		type = "jpeg";
	            	}
	            	else
	            	{
	            		type = extension;
	            	}

	            	entity.addPart("medium[upload]", new FileBody(file,"image/"+type));
	            }
	            else
	            {
	            	try 
		            {
	            		if(pairs != null && pairs.getKey() != null && pairs.getValue() != null)
	            			entity.addPart(pairs.getKey().toString(), new StringBody(pairs.getValue().toString()));
	            		else
	            			System.err.println(pairs);
					} 
		            catch (UnsupportedEncodingException e) 
		            {
						e.printStackTrace();
					}
	            }
 
	            it.remove(); // avoids a ConcurrentModificationException
	        }
	        
	        postRequest.setEntity(entity);
		}
		else if(method.equals("GET"))
		{
			getRequest = new HttpGet(url);
			
			if(cookie.length() > 0)
			{
				getRequest.setHeader("Cookie", "_web_session="+cookie);
			}
		}
        
	    try {
	    	
	    	HttpResponse response = null;
	    	
	    	if(method.equals("GET"))
	    	{
	    		response = client.execute(getRequest);
	    	}
	    	else if(method.equals("POST"))
	    	{
	    		response = client.execute(postRequest);
	    	}
	         

	        final int statusCode = response.getStatusLine().getStatusCode();
	        
	        //if (statusCode != HttpStatus.SC_OK) { 
	            System.out.println("HTTP Code " + statusCode + "(" + url +")");
	       // }
	        
	        final HttpEntity entity = response.getEntity();
	        
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent(); 
	                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
	                StringBuilder json = new StringBuilder();
	                String line;
	                while ((line = r.readLine()) != null) {
	                    json.append(line);
	                }
	                
	                return json.toString();
	            } 
	            finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } 
	    catch (IOException e) {
	        // Could provide a more explicit error message for IOException or IllegalStateException
	    	System.out.println(e.getMessage());	        
	    } 
	    catch (IllegalStateException e) {
	    	System.out.println(e.getMessage());	        
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());	 
	    	getRequest.abort();
	    }
	    finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    
	    return null;
	}

}
