package com.example.google_tts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText edit = null;
	private TextView languages = null, result = null;
	private TTS mTTS = null;
	private static final int RQS_VOICE_RECOGNITION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();

	}

	class test implements Runnable {
		String str;

		test(String str) {
			this.str = str.replaceAll(" ", "+");
			System.out.println(this.str);
		}

		public void run() {
			URI uri;
			try {
				uri = new URI("http://translate.google.com/translate_a/t?client=t&hl=zh-TW&sl=auto&tl=zh-TW&q=" + str);
				URL u = new URL(uri.toASCIIString());
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.addRequestProperty("User-Agent", "Mozilla/5.0");
				c.setRequestMethod("GET");
				c.connect();

				BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
				String result, line = reader.readLine();
				result = line;
				while ((line = reader.readLine()) != null) {
					result += line;
				}

				mHandler.sendMessage(mHandler.obtainMessage(1, new JSONArray(result).getJSONArray(0).getJSONArray(0).getString(0)));
				reader.close();
			} catch (Exception e) {
			}

		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				result.setText((String) msg.obj);
				break;

			}

			super.handleMessage(msg);
		}

	};

	private void findView() {
		edit = (EditText) findViewById(R.id.edit);
		languages = (TextView) findViewById(R.id.languages);
		result = (TextView) findViewById(R.id.result);
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

	public void go2Translate(View v) {
		new Thread(new test(edit.getText().toString())).start();
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
