package com.example.google_tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.media.MediaPlayer;
import android.os.Environment;

public class TTS {
	MediaPlayer mediaPlayer = null;

	public TTS() {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
	}

	/**
	 * 
	 * @param str
	 *            要轉換的字串
	 * @param lang
	 *            語系（請參考 google 翻譯，zh-TW 台灣）
	 * @author will
	 */
	public void toSpeech(String str, String lang) {

		try {
			mediaPlayer.reset();
			str = str.replaceAll(" ", "+");
			URI uri = new URI("http://translate.google.com.tw/translate_tts?ie=UTF-8&q=" + str + "&tl=" + lang);
			mediaPlayer.setDataSource(uri.toASCIIString());
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean TTS2File(String filePath, final String fileName, final String str, final String lang) {
		try {
			URI uri;
			uri = new URI("http://translate.google.com.tw/translate_tts?ie=UTF-8&q=" + str + "&tl=" + lang);
			URL u = new URL(uri.toASCIIString());
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.addRequestProperty("User-Agent", "Mozilla/5.0");
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			FileOutputStream f = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".mp3"));
			InputStream in = c.getInputStream();
			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) > 0) {
				f.write(buffer, 0, len1);
			}
			f.close();
			c.disconnect();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
