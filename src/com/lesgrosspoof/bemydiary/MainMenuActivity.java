package com.lesgrosspoof.bemydiary;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.MyBase;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Users;

public class MainMenuActivity extends AbstractActivity implements OnClickListener{
	private Button currentBoard;
	private Button oldBoards;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
        
        currentBoard = (Button)findViewById(R.id.currentBoard);
        oldBoards = (Button)findViewById(R.id.oldBoards);
        
        currentBoard.setOnClickListener(this);
        oldBoards.setOnClickListener(this);
        
        /**
         * Association d'un compte google
         */
        if (Users.getInstance().get() == null) {
        	AccountManager am = AccountManager.get(this);
            final Account[] accounts = am.getAccountsByType("com.google");
        	
            String[] mails = new String[accounts.length];
            
            for (int i = 0; i < mails.length; i ++) {
            	mails[i] = accounts[i].name;
            }
            
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
			builder.setTitle("Compte à associer");
			builder.setCancelable(false);
			builder.setItems(mails, new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Users.getInstance().add(accounts[which].name, "", "google");
				}
			});
			
			AlertDialog alert = builder.create();
			alert.show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
	public void onClick(View v) {
		if (v == currentBoard) {
			if (Boards.getInstance().existOpenBoard() == false) {
				new AlertDialog.Builder(this)
				.setTitle("Aucun board ouvert")
				.setMessage("Aucun board en cours, voulez vous en créer un nouveau ?")
				.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Boards.getInstance().create();
						Board.currentIndexBoard = Boards.getInstance().getCurrentBoardId();
						Board.getCurrentBoard().getSelection();
						
						Intent myIntent = new Intent(getBaseContext(), CurrentListActivity.class);
						startActivityForResult(myIntent, 0);
					}
				})
				.setNegativeButton("Non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				})
				.show();
			}
			else {
				Board.currentIndexBoard = Boards.getInstance().getCurrentBoardId();
				Intent myIntent = new Intent(getBaseContext(), CurrentListActivity.class);
				startActivityForResult(myIntent, 0);
			}
		}
		else if (v == oldBoards) {
			if (Boards.getInstance().existOldBoard() == true) {
				Intent myIntent = new Intent(getBaseContext(), OldListActivity.class);
				startActivityForResult(myIntent, 0);
			}
			else {
				Toast.makeText(this, "Vous n'avez pas d'anciens carnets", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
