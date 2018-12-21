package com.example.csh.forlang;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentExam extends Fragment implements SwipeDismissListViewTouchListener.DismissCallbacks, View.OnClickListener, ListView.OnItemClickListener
{
	private ExamListAdapter examListAdapter;
	private WordListAdapter wordListAdapter;
	private Context context;
	private ContentResolver cr;
	private Uri uri;
	private WordFile wordFile;
	private ArrayList<Integer> wordList;
	private ArrayList<Integer> results;
	private int examNo;

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
		examListAdapter = new ExamListAdapter(
				context,
				R.layout.list_item_exam,
				cursor,
				new String[]{"examNo"},
				new int[]{R.id.tvExamNo}, 0);

		ListView listView = rootView.findViewById(R.id.list_exam);
		listView.setEmptyView(rootView.findViewById(R.id.list_empty));
		listView.setAdapter(examListAdapter);
		listView.setOnItemClickListener(this);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, this);
		listView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during ListView scrolling,
		// we don't look for swipes.
		listView.setOnScrollListener(touchListener.makeScrollListener());

		// set add button listener
		Button buttonAdd = rootView.findViewById(R.id.button_exam);
		buttonAdd.setOnClickListener(this);

		// read word file
		Bundle args = getArguments();
		wordFile = (WordFile) args.getSerializable("wordFile");
		wordList = new ArrayList<>();
		results = new ArrayList<>();

		return rootView;
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
			c = (Cursor) examListAdapter.getItem(position);
			int examNo = c.getInt(1);
			cr.delete(uri, "examNo=?", new String[]{String.valueOf(examNo)});
		}
		c = cr.query(uri, new String[]{"distinct _id", "examNo"}, "examNo>?", new String[]{"0"}, null);
		examListAdapter.changeCursor(c);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.button_exam:
			{
				FragmentTest fragmentTest = new FragmentTest();
				Cursor cursor = cr.query(uri, new String[]{"_id", "max(examNo)"}, null, null, null);
				examNo = 1;
				if (cursor.moveToNext())
					examNo = cursor.getInt(1) + 1;

				Bundle args = new Bundle();
				args.putSerializable("wordFile", wordFile);
				args.putIntegerArrayList("wordList", wordList);
				args.putIntegerArrayList("results", results);
				args.putInt("examNo", examNo);
				args.putInt("progress", 1);
				fragmentTest.setArguments(args);

				getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentTest).commit();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		int examNo = position + 1;

		Cursor cursor = cr.query(uri, new String[]{"_id", "word", "meaning1", "meaning2", "meaning3"}, "examNo=?", new String[]{String.valueOf(examNo)}, null);
		wordListAdapter = new WordListAdapter(
				context,
				R.layout.list_item_word,
				cursor,
				new String[]{"word", "meaning1", "meaning2", "meaning3"},
				new int[]{R.id.tvWord, R.id.tvMeaning1, R.id.tvMeaning2, R.id.tvMeaning3}, 0);
		cursor = cr.query(uri, new String[]{"_id", "count(*)"}, "examNo=? and correct=?", new String[]{String.valueOf(examNo), "1"}, null);

		FragmentListView fragmentListView = new FragmentListView();
		Bundle args = new Bundle();
		args.putInt("examNo", examNo);
		if(cursor.moveToNext())
			args.putInt("score", cursor.getInt(1));
		args.putSerializable("wordListAdapter", wordListAdapter);
		fragmentListView.setArguments(args);
		fragmentListView.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		fragmentListView.show(getFragmentManager(), "Tag");
	}
}