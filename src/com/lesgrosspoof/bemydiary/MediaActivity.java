package com.lesgrosspoof.bemydiary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.Media;
import com.lesgrosspoof.bemydiary.models.Items;
import com.lesgrosspoof.bemydiary.models.Medias;

public class MediaActivity extends AbstractActivity {
	
	private Button illustrer;
	private Button commenter;
	
	private Button photosBtn;
	private Button videosBtn;
	
	private TextView nbPhotos;
	private TextView nbVideos;
	
	private TextView title;
	
	private Button next;
	private Button prev;
	
	private List<Media> photos;
	private List<Media> videos;
	private List<Media> comments;
	
	private HashMap<Integer, HashMap<Integer, List<Media>>> mediasCurrentBoard;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_media);
        
        title = (TextView)findViewById(R.id.nameLieu);
        //image = (ImageView)findViewById(R.id.image);
        
        photosBtn = (Button)findViewById(R.id.boutonPhotos);
        photosBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// bouton photos
			}
		});
        
        videosBtn = (Button)findViewById(R.id.boutonVideos);
        videosBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// bouton videos
			}
		});

        nbPhotos = (TextView)findViewById(R.id.nbPhotos);
        nbVideos = (TextView)findViewById(R.id.nbVideos);
        
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				next();
			}
		});
        
        prev = (Button)findViewById(R.id.prev);
        prev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				prev();
			}
		});
        
        illustrer = (Button)findViewById(R.id.illustrer);
        illustrer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent target = new Intent(Intent.ACTION_GET_CONTENT);
				target.setType("image/*");
		        Intent intent = Intent.createChooser(target, "Ajouter un media");
		        try {
		            startActivityForResult(intent, 1234);
		        } catch (ActivityNotFoundException e) {}
			}
		});
        
        commenter = (Button)findViewById(R.id.commenter);
        commenter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// bouton commenter
			}
		});
        
        updateMedia();
        
        display();
    }
    
    private void updateMedia() {
    	try {
        	mediasCurrentBoard = Medias.getInstance().getMedias(Board.getCurrentBoard().getId());
        }
        catch(Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
	private void display() {
    	try {
    		System.out.println("display");
    		photos = mediasCurrentBoard.get(Board.getCurrentItemId()).get(Medias.PHOTO); // CRASH
    		videos = mediasCurrentBoard.get(Board.getCurrentItemId()).get(Medias.VIDEO);
    		comments = mediasCurrentBoard.get(Board.getCurrentItemId()).get(Medias.COMMENT);
    		
    		title.setText(Items.getInstance().get(Board.getCurrentItemId()).getNom().toUpperCase());
    		photosBtn.setBackgroundColor(Color.GRAY);

	    	if (photos != null && photos.size() > 0) {
	    		Bitmap bitmap = Medias.getInstance().getPhotoThumbnail(photos.get(photos.size()-1));
		    	if(bitmap != null) {
		    		Drawable thumbnail = new BitmapDrawable(getResources(),bitmap);
			    	photosBtn.setBackgroundDrawable(thumbnail);
		    	}
    		}
	    	
	    	videosBtn.setBackgroundColor(Color.GRAY);
	    	nbPhotos.setText(photos.size() + " photo(s)");
	    	nbVideos.setText(((videos == null) ? "0" : videos.size()) + " vidéo(s)");
    	}
    	catch(Exception e) {
    		System.out.println("ERROR display : " + e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    private void next() {
    	int nextItemId = 0;
    	List<ItemSelection> selections = Board.getCurrentBoard().getSelection();
    	
    	for(int i = 0; i < selections.size(); i++) {
    		if ((Board.getCurrentItemId() == selections.get(i).getId()) && ((i+1) < selections.size())) {
    			nextItemId = i+1;
    		}
    	}
    	
    	Board.setCurrentItemId(selections.get(nextItemId).getId());    	
    	
    	display();
    }
    
    private void prev() {
    	
    	List<ItemSelection> selections = Board.getCurrentBoard().getSelection();
    	int prevItemId = selections.size() - 1;
    	
    	for(int i = 0; i < selections.size(); i++) {
    		if ((Board.getCurrentItemId() == selections.get(i).getId()) && ((i-1) >= 0)) {
    			prevItemId = i-1;
    		}
    	}
    	
    	Board.setCurrentItemId(selections.get(prevItemId).getId());    	
    	
    	display();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popmenu, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case 1234:  
            if (resultCode == RESULT_OK) {  
                final Uri uri = data.getData();
                String realPathFile = getRealPathFromURI(uri);
                File file = new File(realPathFile);
                
                if (file.exists()) {
                	try {
                		int prefixName = (int) (Math.random() * 12345);
                		String fileName = prefixName +"_"+ file.getName();
                		
						copy(file, new File(this.getApplicationInfo().dataDir + "/"+ fileName));
						
						Board.getCurrentBoard().addMedia(Board.getCurrentItemId(), this.getApplicationInfo().dataDir + "/"+ fileName);
						
						updateMedia();
						
				        display();
				        
					} 
                	catch (NullPointerException e) {
						System.out.println("FAIL : " + e.getMessage() + " CAUSED BY : "+e.getCause());
						e.printStackTrace();
					}
                	catch (Exception e) {
						System.out.println("FAIL : " + e.getMessage() + " CAUSED BY : "+e.getCause());
						e.printStackTrace();
					}
                }
            }
        }
    }
    
    private void copy(File src, File dst) {
    	try {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    	}
    	catch(Exception e) {
    		System.out.println("FAIL: - " + e.getMessage());
    	}
    }
    
	private String getRealPathFromURI(Uri uri) {
		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
