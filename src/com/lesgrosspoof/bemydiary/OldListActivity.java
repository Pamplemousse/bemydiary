package com.lesgrosspoof.bemydiary;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.models.Boards;

public class OldListActivity extends AbstractActivity implements OnItemClickListener {
	
	private ListView list;
	private List<Board> oldsBoard;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_old_list);
        list = (ListView)findViewById(R.id.listViewOld);
        
        oldsBoard = Boards.getInstance().getOldsBoard();
        
        List<String> values = new ArrayList<String>();
        
        for (Board board : oldsBoard) {
        	values.add(board.getName());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int idBoard = oldsBoard.get(arg2).getId();
			}
    	});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popmenu, menu);
        return true;
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
