package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotebook extends Fragment
{
	WordListAdapter adapter;

	public FragmentNotebook()
	{
		// Required empty public constructor
	}

	@Override
	public void onDestroy()
	{
		TextToSpeech tts = adapter.getTTS();
		if(tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_notebook, container, false);

		// simple list view
		Context context = container.getContext();
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.parse("content://forlang.provider/MyWords");

		Cursor cursor = cr.query(uri, new String[]{"_id", "word", "meaning1", "meaning2", "meaning3"}, "examNo=?", new String[]{"0"}, null);
		adapter = new WordListAdapter(
				context,
				R.layout.list_item,
				cursor,
				new String[]{"word", "meaning1", "meaning2", "meaning3"},
				new int[]{R.id.tvWord, R.id.tvMeaning1, R.id.tvMeaning2, R.id.tvMeaning3}, 0);

		ListView listView = rootView.findViewById(R.id.list_view);
		listView.setAdapter(adapter);

		return rootView;
	}
}
