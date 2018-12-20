package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentExam extends Fragment implements SwipeDismissListViewTouchListener.DismissCallbacks, View.OnClickListener
{
	private ExamListAdapter adapter;
	private Context context;
	private ContentResolver cr;
	private Uri uri;

	public FragmentExam()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_exam, container, false);

		context = container.getContext();
		cr = context.getContentResolver();
		uri = Uri.parse("content://forlang.provider/MyWords");

		Cursor cursor = cr.query(uri, new String[]{"distinct _id", "examNo"}, "examNo>?", new String[]{"0"}, null);
		adapter = new ExamListAdapter(
				context,
				R.layout.list_item_exam,
				cursor,
				new String[]{"examNo"},
				new int[]{R.id.tvExamNo}, 0);

		ListView listView = rootView.findViewById(R.id.list_exam);
		listView.setEmptyView(rootView.findViewById(R.id.list_empty));
		listView.setAdapter(adapter);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, this);
		listView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during ListView scrolling,
		// we don't look for swipes.
		listView.setOnScrollListener(touchListener.makeScrollListener());

		// set add button listener
		Button buttonAdd = rootView.findViewById(R.id.button_exam);
		buttonAdd.setOnClickListener(this);

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
			int examNo = c.getInt(1);
			cr.delete(uri, "examNo=?", new String[]{String.valueOf(examNo)});
		}
		c = cr.query(uri, new String[]{"distinct _id", "examNo"}, "examNo>?", new String[]{"0"}, null);
		adapter.changeCursor(c);
	}

	@Override
	public void onClick(View v)
	{
	}
}
