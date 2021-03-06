package com.example.csh.forlang;

import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Locale;

public class WordListAdapter extends SimpleCursorAdapter implements View.OnClickListener, TextToSpeech.OnInitListener, Serializable
{
	private TextToSpeech tts;

	public WordListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
	{
		super(context, layout, c, from, to, flags);

		tts = new TextToSpeech(context, this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View itemView =  super.getView(position, convertView, parent);

		// speaker button
		ImageButton buttonSpeaker = itemView.findViewById(R.id.button_speaker);
		buttonSpeaker.setOnClickListener(this);

		return itemView;
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.button_speaker:
			{
				View itemView = (View)v.getParent();
				TextView textView = itemView.findViewById(R.id.tvWord);
				String word = textView.getText().toString();

				tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
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
	
	public TextToSpeech getTTS()
	{
		return tts;
	}
}