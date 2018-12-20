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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTest extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener
{
	private TextToSpeech tts;
	private WordFile wordFile;
	private String word, meanings[];

	public FragmentTest()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// random word fragment design
		View rootView = inflater.inflate(R.layout.fragment_test, container, false);

		// read word file
		Bundle args = getArguments();
		wordFile = (WordFile) args.getSerializable("wordFile");

		int random = (new Random()).nextInt(wordFile.getLength());
		word = wordFile.getWordList().get(random);
		meanings = wordFile.getMeaningList().get(random);

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

		// set search button listener
		Button buttonSubmit = rootView.findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(this);

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
	}

	// OnClickListener only for tts
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.button_speaker:
			{
				tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
				break;
			}
			case R.id.button_submit:
			{
				break;
			}
		}
	}

	@Override
	public void onInit(int status)
	{
		if(status == TextToSpeech.SUCCESS)
		{
			int result = tts.setLanguage(Locale.US);
			tts.setSpeechRate(1.0f);

			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
				Log.i("TTS", "Language is not supported");
		}
		else
			Log.e("TTS", "Initialization failed");
	}
}