package com.example.google_tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
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
	 * @param s
	 *            要轉換的字串
	 * @param tl
	 *            語系（請參考 google 翻譯，zh-TW 台灣）
	 * @author will
	 */
	public void toSpeech(String s, String tl) {

		try {
			mediaPlayer.reset();
			URI uri = new URI("http://translate.google.com.tw/translate_tts?ie=UTF-8&q=" + s + "&tl=" + tl);
			mediaPlayer.setDataSource(uri.toASCIIString());
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void TTS2File(String filePath, final String fileName, final String s, final String tl) {
		try {
			URI uri = new URI("http://translate.google.com.tw/translate_tts?ie=UTF-8&q=" + s + "&tl=" + tl);
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
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
