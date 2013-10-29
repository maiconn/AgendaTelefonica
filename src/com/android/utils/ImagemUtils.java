package com.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImagemUtils {
	
	public static void downloadFromUrl(String fileURL, String fileName){
		try{
			File arquivo = new File(fileName);
			if (!arquivo.exists()){
				arquivo.createNewFile();
			}

			URL url = new URL(fileURL);
			File file = new File(fileName);

			URLConnection cURL = url.openConnection();

			InputStream is = cURL.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayBuffer	baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1){
				baf.append((byte)current);
			}

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		}catch (IOException e){
			Log.i("IOException", e.toString());
		}
	}
	
	public static Drawable loadImagemFromWebOperations(String url){
		try{		
			InputStream is = (InputStream) new URL(url).getContent();	

			Drawable d = Drawable.createFromStream(is, "src name");

			return d;
		}catch(Exception e){
			Log.e("IMAGEM", "Exception: n�o foi poss�vel carregar imagem... "+e.toString());
			return null;
		}
	}

}
