package com.srichakradhar.bagel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Main extends Activity implements OnClickListener{
	private MediaPlayer music;
	EditText levelET;
	Button Play, Play_ass;
	ToggleButton zeroTB;
	private TextView TvHowTo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
 		
		Play_ass = (Button) findViewById(R.id.Play_ass);
		Play = (Button) findViewById(R.id.Play);
		levelET = (EditText) findViewById(R.id.levelET);
		zeroTB = (ToggleButton) findViewById(R.id.zeroToggleButton);
		TvHowTo = (TextView) findViewById(R.id.TvHowTo);
		//View relLayout = findViewById(R.id.relativeLayout);
		
		Play.setOnClickListener(this);
		Play_ass.setOnClickListener(this);
		TvHowTo.setOnClickListener(this);
		//AnimationDrawable progressAnimation = (AnimationDrawable) relLayout.getBackground();
		//progressAnimation.start();
		music = MediaPlayer.create(this, R.raw.bgm);
		music.start();
		music.setLooping(true);
	}
	
	@Override
	protected void onDestroy() {
		music.stop();
		music.release();
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		if(v == Play){
			Intent intent = new Intent(this, game.class);
			startActivity(intent);
		}
		else if(v == Play_ass){
			Intent intent = new Intent(this, Bagel.class);
			intent.putExtra(getString(R.string.zero_allowed), zeroTB.isChecked());
			intent.putExtra(getString(R.string.level), Integer.parseInt(levelET.getText().toString()));
			startActivity(intent);
		}
		else if(v==TvHowTo){
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle(R.string.help);
			ad.setMessage(Html.fromHtml(getString(R.string.main_help)));
			ad.show();
		}
	}
}
