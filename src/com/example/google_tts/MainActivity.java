package com.example.google_tts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText edit = null;
	private TextView languages = null;
	private TTS mTTS = null;
	private static final int RQS_VOICE_RECOGNITION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
	}

	private void findView() {
		edit = (EditText) findViewById(R.id.edit);
		languages = (TextView) findViewById(R.id.languages);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.English:
			languages.setText("en");
			break;

		case R.id.zh_TW:
			languages.setText("zh_TW");
			break;
		}
		return false;
	}

	public void go2TTS(View v) {
		if (mTTS == null) {
			mTTS = new TTS();
		}
		new Thread(new Runnable() {

			public void run() {
				mTTS.toSpeech(edit.getText().toString(), languages.getText().toString());
			}
		}).start();

	}

	public void go2STT(View v) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "開始進行語音辨識");
		startActivityForResult(intent, RQS_VOICE_RECOGNITION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RQS_VOICE_RECOGNITION) {
			if (resultCode == RESULT_OK) {

				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String firstMatch = (String) result.get(0);
				edit.setText(firstMatch);
			}
		}
	}
}
