package com.example.csh.forlang;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Locale;

public class ExamListAdapter extends SimpleCursorAdapter
{
	public ExamListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
	{
		super(context, layout, c, from, to, flags);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View itemView =  super.getView(position, convertView, parent);

		// tvExamNo
		TextView tvExamNo = itemView.findViewById(R.id.tvExamNo);
		String examNo = tvExamNo.getText().toString();
		tvExamNo.setText(examNo + "회차");

		return itemView;
	}
}