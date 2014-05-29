package com.srichakradhar.bagel;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class game extends Activity {
	Shuffler s = new Shuffler();
	int[] a;
	int no[];
	int ans[];
	int tries = 0;
	int guesses[];
	int instrIndex = 0;
	TextView instrTV, historyTV;
	Button prevButton, nextButton, guessButton;
	EditText et1, et2, et3;
	String[] Instructions = { "Guess numbers randomly between 1 and 9 (inclusive)",
			"Pico : A digit is matched, but is in wrong place",
			"Fermi: A digit is matched and is in correct place",
			"Bagels : No digit is matched, dump those digits!" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		instrTV = (TextView) findViewById(R.id.Instruction);
		historyTV = (TextView) findViewById(R.id.historyTextView);

		prevButton = (Button) findViewById(R.id.PrevInstr);
		nextButton = (Button) findViewById(R.id.NextInstr);
		guessButton = (Button) findViewById(R.id.guessBtn);

		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);

		instrTV.setSelected(true);
		instrTV.setSingleLine(true);
		
		a = s.random(1);
		no = Arrays.copyOf(a, 3);
		ans = new int[3];
		tries = 0;
		guesses = new int[10];
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bagel, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Editor editor = preferences.edit();
		switch (item.getItemId()) {
		case R.id.help:
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle(R.string.help);
			ad.setMessage(Html.fromHtml(getString(R.string.basic_help)));
			ad.show();
			return true;
		case R.id.new_game:
			Toast.makeText(this.getApplicationContext(), "Secret was: "+a[0]+a[1]+a[2],Toast.LENGTH_SHORT).show();
			newGame();
			// editor.putBoolean("zeroAllowed", zeroAllowed);
			// editor.commit();
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
				.setMessage(
						"Answer : " + a[0] + "" + a[1] + "" + a[2]
								+ "\nWould you like to play another game?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								newGame();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void newGame(){
		a = s.random(1);
		no = Arrays.copyOf(a, 3);
		tries = 0;
		historyTV.setText("");
		et1.setText("1"); et2.setText("2");et3.setText("3");
		guesses = new int[10];
	}

	public void check(View v) {
		int t, t1 = Integer.parseInt(et1.getText().toString()), t2 = Integer
				.parseInt(et2.getText().toString()), t3 = Integer.parseInt(et3
				.getText().toString());
		switch (v.getId()) {
		case R.id.guessBtn:
			break;
		case R.id.button3:
			t = Integer.parseInt(et1.getText().toString()) % 9 + 1;
			if (t == t2)
				t++;
			if (t == t3)
				t++;
			if (t == t2)
				t++;
			et1.setText("" + t);
			return;
		case R.id.button2:
			t = Integer.parseInt(et2.getText().toString()) % 9 + 1;
			if (t == t1)
				t++;
			if (t == t3)
				t++;
			if (t == t1)
				t++;
			et2.setText("" + t);
			return;
		case R.id.button1:
			t = Integer.parseInt(et3.getText().toString()) % 9 + 1;
			if (t == t1)
				t++;
			if (t == t2)
				t++;
			if (t == t1)
				t++;
			et3.setText("" + t);
			return;
		case R.id.button4:
			t = Integer.parseInt(et1.getText().toString()) - 1;
			if (t <= 0)
				t = 9;
			if (t == t2)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t3)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t2)
				t--;
			if (t <= 0)
				t = 9;
			et1.setText("" + t);
			return;
		case R.id.button5:
			t = Integer.parseInt(et2.getText().toString()) - 1;
			if (t <= 0)
				t = 9;
			if (t == t1)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t3)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t1)
				t--;
			if (t <= 0)
				t = 9;
			et2.setText("" + t);
			return;
		case R.id.button6:
			t = Integer.parseInt(et3.getText().toString()) - 1;
			if (t <= 0)
				t = 9;
			if (t == t1)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t2)
				t--;
			if (t <= 0)
				t = 9;
			if (t == t1)
				t--;
			if (t <= 0)
				t = 9;
			et3.setText("" + t);
			return;
		case R.id.NextInstr:
			instrIndex = (instrIndex+1) % Instructions.length;
			instrTV.setText(Instructions[instrIndex]);
			Log.i("instrIndex", Instructions.length+""+instrIndex);
			return;
		case R.id.PrevInstr:
			if(--instrIndex < 0) instrIndex = Instructions.length - 1;
			Log.i("instrIndex", ""+instrIndex);
			instrTV.setText(Instructions[instrIndex]);
			return;
		}
		// Toast.makeText(this.getApplicationContext(), ""+a[0]+""+a[1]+""+a[2],
		// Toast.LENGTH_SHORT).show();
		if (tries > 9) {
			showDialog();
		} else {
			int answer = -10;
			try {
				ans[0] = Integer.parseInt(et1.getText().toString());
				ans[1] = Integer.parseInt(et2.getText().toString());
				ans[2] = Integer.parseInt(et3.getText().toString());
				answer = ans[0] * 100 + ans[1] * 10 + ans[2];
				//Log.i("index",tries+""+Arrays.asList(guesses).indexOf(answer));
				Arrays.sort(guesses);
				if(Arrays.binarySearch(guesses, answer) >= 0){
					throw(new Exception("guessed"));
					}
				guesses[tries] = answer;
				tries++;
			} catch (Exception e) {
				String s = "Please Enter Numbers";
				if(e.getMessage().toString().equals("guessed"))
					s = "You Already Guessed That!";
				Toast.makeText(this.getApplicationContext(),
						s, Toast.LENGTH_SHORT).show();
				return;
			}
			StringBuffer clues = new StringBuffer("");
			
			if (Arrays.equals(ans, no)) {
				instrTV.setText("Well Guessed!");
				showDialog();
			} else {
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++) {
						if (ans[i] == no[j]) {
							if (i == j)
								clues.append("Fermi ");
							else
								clues.append("Pico ");
						}
					}
				if (clues.length() == 0) {
					clues.append("Bagels");
				}
				// instrTV.setText(clues);
				Toast.makeText(this.getApplicationContext(), clues,
						Toast.LENGTH_SHORT).show();
				historyTV.setText(historyTV.getText().toString() + "\n" + tries
						+ ". " + ans[0] + "" + ans[1] + "" + ans[2] + " "
						+ clues.toString());
			}
		}
	}
}