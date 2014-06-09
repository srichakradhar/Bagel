package com.srichakradhar.bagel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Main extends Activity implements OnClickListener, OnCheckedChangeListener{
	private MediaPlayer music;
	EditText levelET;
	Button Play, Play_ass;
	ToggleButton zeroTB, soundTB;
	private TextView TvHowTo;
	SharedPreferences preferences;
	private boolean sound;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
 		
		Play_ass = (Button) findViewById(R.id.Play_ass);
		Play = (Button) findViewById(R.id.Play);
		levelET = (EditText) findViewById(R.id.levelET);
		zeroTB = (ToggleButton) findViewById(R.id.zeroToggleButton);
		soundTB = (ToggleButton) findViewById(R.id.soundTB);
		TvHowTo = (TextView) findViewById(R.id.TvHowTo);
		//View relLayout = findViewById(R.id.relativeLayout);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		Play.setOnClickListener(this);
		Play_ass.setOnClickListener(this);
		TvHowTo.setOnClickListener(this);
		soundTB.setOnCheckedChangeListener(this);
		//AnimationDrawable progressAnimation = (AnimationDrawable) relLayout.getBackground();
		//progressAnimation.start();
		music = MediaPlayer.create(this, R.raw.bgm);
		sound = preferences.getBoolean("Sound", true);
		if(sound){
			music.start();
			music.setLooping(true);
		}
		soundTB.setChecked(sound);
	}
	
	@Override
	protected void onPause() {
		if(music.isPlaying())
			music.pause();
		Editor editor = preferences.edit();
		editor.putBoolean("Sound", soundTB.isChecked());
		editor.commit();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(soundTB.isChecked()){
			music.start();
			music.setLooping(true);
		}
		super.onResume();
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

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(arg0 == soundTB){
			if(arg0.isChecked() && !music.isPlaying()){
				music.start();
			}
			if(!arg0.isChecked() && music.isPlaying()){
				music.pause();
			}
//			(arg0.isChecked())?((music.isPlaying()) ? : music.start()):(music.isPlaying() ? music.pause() : );
		}
		
	}
}
