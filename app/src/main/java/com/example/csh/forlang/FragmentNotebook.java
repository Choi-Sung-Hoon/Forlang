package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotebook extends Fragment implements SwipeDismissListViewTouchListener.DismissCallbacks
{
	private WordListAdapter adapter;
	private Context context;
	private ContentResolver cr;
	private Uri uri;

	public FragmentNotebook()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_notebook, container, false);

		context = container.getContext();
		cr = context.getContentResolver();
		uri = Uri.parse("content://forlang.provider/MyWords");

		Cursor cursor = cr.query(uri, new String[]{"_id", "word", "meaning1", "meaning2", "meaning3"}, "examNo=?", new String[]{"0"}, null);
		adapter = new WordListAdapter(
				context,
				R.layout.list_item_word,
				cursor,
				new String[]{"word", "meaning1", "meaning2", "meaning3"},
				new int[]{R.id.tvWord, R.id.tvMeaning1, R.id.tvMeaning2, R.id.tvMeaning3}, 0);

		ListView listView = rootView.findViewById(R.id.list_word);
		listView.setEmptyView(rootView.findViewById(R.id.list_empty));
		listView.setAdapter(adapter);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, this);
		listView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during ListView scrolling,
		// we don't look for swipes.
		listView.setOnScrollListener(touchListener.makeScrollListener());

		return rootView;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(adapter != null)
		{
			TextToSpeech tts = adapter.getTTS();
			if(tts != null)
			{
				tts.stop();
				tts.shutdown();
			}
		}
	}

	@Override
	public boolean canDismiss(int position)
	{
		return true;
	}

	@Override
	public void onDismiss(ListView listView, int[] reverseSortedPositions)
	{
		Cursor c;
		for (int position : reverseSortedPositions)
		{
			c = (Cursor) adapter.getItem(position);
			String word = c.getString(1);
			cr.delete(uri, "word=?", new String[]{word});
		}
		c = cr.query(uri, new String[]{"_id", "word", "meaning1", "meaning2", "meaning3"}, "examNo=?", new String[]{"0"}, null);
		adapter.changeCursor(c);
	}
}
