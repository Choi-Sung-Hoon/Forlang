package com.example.csh.forlang;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
	Deprecated class
 */
public class Translation extends AsyncTask<String, Void, String>
{
	@Override
	protected String doInBackground(String... args)
	{
		String clientId = "7ZbXQOnzrTEaoYf7RyTO"; // client id
		String clientSecret = "UOv_RQPsIQ"; // client secret
		HttpURLConnection con;
		BufferedReader br;

		try
		{
			String text = URLEncoder.encode(args[0], "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
			URL url = new URL(apiURL);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

			// post request
			String postParams = "source=en&target=ko&text=" + text;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			// 정상 호출
			if(responseCode==200)
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null)
				response.append(inputLine);
			br.close();

			JSONObject jsonObject = new JSONObject(response.toString());
			String translated = jsonObject.getJSONObject("message").getJSONObject("result").getString("translatedText");
			return translated;
		}
		catch (Exception e)
		{
			Log.e(e.getClass().toString(), e.getMessage().toString());
			e.printStackTrace();
			return null;
		}
	}
}