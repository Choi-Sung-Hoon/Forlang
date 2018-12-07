package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWord extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener
{
	private String word;
	private String meanings[];
	private TextToSpeech tts;

	public FragmentWord()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// random word fragment design
		View rootView = inflater.inflate(R.layout.fragment_word, container, false);

		// get bundle data
		word = getArguments().getString("word");
		meanings = getArguments().getStringArray("meanings");

		// set TextView
		TextView tv = rootView.findViewById(R.id.tvWord);
		tv.setText(word);

		// set speaker button
		tts = new TextToSpeech(getActivity(), this);
		ImageButton buttonSpeaker = rootView.findViewById(R.id.button_speaker);
		buttonSpeaker.setOnClickListener(this);

		TextView tvTranslated1 = rootView.findViewById(R.id.tvTranslated1);
		tvTranslated1.setText(meanings[0]);
		if(meanings.length >= 2)
		{
			TextView tvTranslated2 = rootView.findViewById(R.id.tvTranslated2);
			tvTranslated2.setText(meanings[1]);
		}
		if(meanings.length >= 3)
		{
			TextView tvTranslated3 = rootView.findViewById(R.id.tvTranslated3);
			tvTranslated3.setText(meanings[2]);
		}

		// set add button listener
		Button buttonAdd = rootView.findViewById(R.id.button_add);
		buttonAdd.setOnClickListener(this);

		// set search button listener
		Button buttonSearch = rootView.findViewById(R.id.button_search);
		buttonSearch.setOnClickListener(this);

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onDestroy()
	{
		if(tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	// OnClickListener only for tts
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.button_speaker:
				tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
				break;
			case R.id.button_add:
			{
				ContentResolver cr = v.getContext().getContentResolver();
				Uri uri = Uri.parse("content://forlang.provider/MyWords");

				ContentValues values = new ContentValues();
				values.put("word", word);
				values.put("meaning1", meanings[0]);
				if(meanings.length >= 2)
					values.put("meaning2", meanings[1]);
				if(meanings.length >= 3)
					values.put("meaning3", meanings[2]);
				values.put("examNo", 0);

				cr.insert(uri, values);
				break;
			}
			case R.id.button_search:
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://endic.naver.com/search.nhn?sLn=kr&isOnlyViewEE=N&query=" + word));
				startActivity(intent);
				break;
		}
	}

	@Override
	public void onInit(int status)
	{
		if(status == TextToSpeech.SUCCESS)
		{
			int result = tts.setLanguage(Locale.US);
			tts.setSpeechRate(0.95f);

			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
				Log.i("TTS", "Language is not supported");
		}
		else
			Log.e("TTS", "Initialization failed");
	}
}