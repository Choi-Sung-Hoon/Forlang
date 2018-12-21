package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
	private String answer, word, meanings[];
	private ArrayList<Integer> wordList;
	private ArrayList<Integer> results;
	private int examNo;

	public FragmentTest()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// random word fragment design
		View rootView = inflater.inflate(R.layout.fragment_test, container, false);

		// get arguments
		Bundle args = getArguments();
		wordFile = (WordFile) args.getSerializable("wordFile");
		wordList = args.getIntegerArrayList("wordList");
		results = args.getIntegerArrayList("results");
		examNo = args.getInt("examNo");

		// pick a random word
		int random_index = (new Random()).nextInt(wordFile.getLength());
		answer = wordFile.getWordList().get(random_index);
		word = answer;
		meanings = wordFile.getMeaningList().get(random_index);
		int length = answer.length();

		// put the word into the array list
		wordList.add(random_index);

		// change word form
		if(length < 4)
		{
			word = "";
			for(int i = 0; i < length; i++);
				word = word.concat("_");
		}
		else
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (i > 1 && (new Random()).nextInt(2) == 1)
					word = word.substring(0, i) + '_' + word.substring(i + 1);
			}
		}

		// set TextView
		TextView tv = rootView.findViewById(R.id.tvWord);
		tv.setText(word);

		// set speaker button
		tts = new TextToSpeech(getActivity(), this);
		ImageButton buttonSpeaker = rootView.findViewById(R.id.button_speaker);
		buttonSpeaker.setOnClickListener(this);

		// set search button listener
		Button buttonSubmit = rootView.findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(this);

		// set meanings
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
				View rootView = v.getRootView();
				TextView editText = rootView.findViewById(R.id.edit_text);
				String userInput = editText.getText().toString();

				if(answer.equals(userInput))
					results.add(1);
				else
					results.add(0);

				if(wordList.size() < 10)
				{
					FragmentTest fragmentTest = new FragmentTest();
					Bundle args = new Bundle();
					args.putSerializable("wordFile", wordFile);
					args.putIntegerArrayList("wordList", wordList);
					args.putIntegerArrayList("results", results);
					args.putInt("examNo", examNo);
					fragmentTest.setArguments(args);

					getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTest).commit();
				}
				else
				{
					FragmentResult fragmentResult = new FragmentResult();
					Bundle args = new Bundle();
					args.putSerializable("wordFile", wordFile);
					args.putIntegerArrayList("wordList", wordList);
					args.putIntegerArrayList("results", results);
					args.putInt("examNo", examNo);
					fragmentResult.setArguments(args);

					getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentResult).commit();
				}

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