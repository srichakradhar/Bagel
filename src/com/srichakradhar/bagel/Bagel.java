package com.srichakradhar.bagel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Bagel extends Activity {
	EditText guessEditText;
	TextView alertTextView, historyTextView, livesTextView;
	CheckBox autoSubmitCB;
	Button guessButton;
//	View scrollView;
//	SharedPreferences preferences;
	int level = 3;
	boolean zeroAllowed = false;
	Shuffler s = new Shuffler();
	int[] a = new int[10];
	int no[] = new int[level];
	int lives = 10, score = 0, nogames = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bagel);

		guessEditText = (EditText) findViewById(R.id.guessEditText);
		alertTextView = (TextView) findViewById(R.id.alertTextView);
		historyTextView = (TextView) findViewById(R.id.history);
		livesTextView = (TextView) findViewById(R.id.lives);
		autoSubmitCB = (CheckBox) findViewById(R.id.checkBox1);
		guessButton = (Button) findViewById(R.id.bagelBtn);

		// zeroAllowed = preferences.getBoolean("zeroAllowed", false);
		livesTextView.setText(getString(R.string.lives, lives));
		autoSubmitCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				guessButton.setEnabled(!isChecked);
				if(isChecked)
					check(guessButton);
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			zeroAllowed = extras.getBoolean(getString(R.string.zero_allowed));
			level = extras.getInt(getString(R.string.level));
			//Log.i("level",""+level+zeroAllowed);
		}
		
		a = s.random(zeroAllowed ? 0 : 1);
		no = Arrays.copyOf(a, level);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(level);
		guessEditText.setFilters(filterArray);	// to set the size of EditText field to "level"
		
		guessEditText.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		           check(guessButton);
		           return true;
		      }else if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK)){
		    	  
		    	  AlertDialog.Builder builder = new AlertDialog.Builder(Bagel.this)
					.setMessage("My Jaan! You've scored " + score + "!\nI won't suggest leaving now.\nYou can always press HOME button and come back to continue.\n\n\tReveal Secret and Quit?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Toast.makeText(getApplicationContext(), "Secret was: "+Arrays.toString(no),Toast.LENGTH_LONG).show();
									finish();
								}
							})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Toast.makeText(getApplicationContext(), "That's good..! ;)", Toast.LENGTH_SHORT).show();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		      }
		    return false;
		  }
	});
		
		guessEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(autoSubmitCB.isChecked() && guessEditText.length() == level){
		    		  check(guessButton);
				}
			}
		});
	}
		
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
			check(findViewById(R.id.guessBtn));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bagel, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle(R.string.help);
			ad.setMessage(Html.fromHtml(getString(R.string.reference)));
			ad.show();
			return true;
		case R.id.new_game:
			Toast.makeText(this.getApplicationContext(), "Secret was: "+Arrays.toString(no),Toast.LENGTH_SHORT).show();
			newGame();
			return true;
		case R.id.feedback:
			Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "srichakra3nsr3@gmail.com", null));
			i.putExtra(Intent.EXTRA_SUBJECT, "[Bagel Feedback]");
			i.putExtra(Intent.EXTRA_TEXT, "Hi Srichakradhar!\n\tI am ...");
			if(i.resolveActivity(getPackageManager()) == null)
				Toast.makeText(this.getApplicationContext(), "No e-mail client found! :(",Toast.LENGTH_SHORT).show();
			else
				startActivity(Intent.createChooser(i, "Choose an e-mail client"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("One more?")
				.setMessage(
						"Answer : " + Arrays.toString(no)
								+ "\nWould you like to play another game?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								score += lives - 1;
								newGame();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(lives > 1)
						score = (score + nogames + lives - 1 + (level >= 5 ? level : 0) + (zeroAllowed ? 1 : 0)) * nogames;
						Log.d("Score","Lives = "+lives+ " Lives Left = " + (score - nogames + lives) +"\nLevel Bonus = "+ (level >= 5 ? level : 0) +
								" Zero Allowed = "+(zeroAllowed ? level : 0)+"No. of games = "+nogames+" Score = "+ score);
						//score = (1(right guess) + level >=5 + zeroChecked * level) * no. of games
						Toast.makeText(getApplicationContext(), "Score : " + score, Toast.LENGTH_LONG).show();
					}
				});//.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				autoSubmitCB.setChecked(false);
				autoSubmitCB.setClickable(false);
				guessButton.setEnabled(false);
				Toast.makeText(getApplicationContext(), "Select \"New Game\" from menu to continue.", Toast.LENGTH_LONG).show();
			}
		});
		dialog.show();
	}

	protected void newGame() {
		historyTextView.setText("");
		guessEditText.setText("");
		guessEditText.requestFocus();
		a = s.random(zeroAllowed ? 0 : 1);
		no = Arrays.copyOf(a, level);
		lives = 10;
		nogames += 1;
		livesTextView.setText(getString(R.string.lives, lives));
		historyTextView.setText("");
		alertTextView.setText("clue");
		autoSubmitCB.setClickable(true);
		autoSubmitCB.setChecked(true);
	}

	public void check(View v) {
		Log.i("no", Arrays.toString(no));
		//Log.d("lives", ""+lives);
		if( lives < 2) {
			showDialog();
		} else {

			int gi = 0;
			int gn[] = new int[level];
			try {
				gi = Integer.parseInt(guessEditText.getText().toString());
				int g = gi;
				for (int i = level - 1; i >= 0; i--) {
					gn[i] = g % 10;
					if (!zeroAllowed && gn[i] == 0)
						throw (new Exception("noZero"));
					for (int j = level - 1; j > i; j--)
						if (gn[i] == gn[j])
							throw (new Exception("Equal"));
					g = g / 10;
				}
				lives--;
			} catch (Exception e) {
				if (e.getMessage().toString().equals("noZero"))
					Toast.makeText(this.getApplicationContext(),
							"Zero Not Allowed!", Toast.LENGTH_SHORT).show();
				/* */
				else
					Toast.makeText(this.getApplicationContext(),
							"No duplicates please! Enter " + level + " different digits",
							Toast.LENGTH_SHORT).show();
				/**/
				guessEditText.requestFocus();
				return;
			} finally {
				guessEditText.setText("");
			}
			livesTextView.setText(getString(R.string.lives, lives));
			ArrayList<String> clues = new ArrayList<String>();
			if (Arrays.equals(gn, no)) {
				alertTextView.setText("Well Guessed!");
				showDialog();
			} else {
				for (int i = 0; i < level; i++)
					for (int j = 0; j < level; j++) {
						if (gn[i] == a[j]) {
							if (i == j)
								clues.add("Fermi ");
							else
								clues.add("Pico ");
						}
					}
				if (clues.size() == 0) {
					clues.add("Bagels");
				}
				Collections.shuffle(clues);
				alertTextView.setText(clues.toString());
				historyTextView.setText(historyTextView.getText().toString()
						+ "\n" + lives + ". " + gi + " " + clues.toString());
				// Toast.makeText(this.getApplicationContext(), clues,
				// Toast.LENGTH_SHORT).show();
			}
		}
	}
}