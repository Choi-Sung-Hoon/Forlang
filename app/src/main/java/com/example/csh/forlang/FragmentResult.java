package com.example.csh.forlang;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentResult extends Fragment implements View.OnClickListener
{
	private Context context;
	private ContentResolver cr;
	private Uri uri;
	private WordFile wordFile;
	private ArrayList<Integer> wordList;
	private ArrayList<Integer> results;
	private int result, examNo, score;
	private String word;
	private String[] meanings;

	public FragmentResult()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// random word fragment design
		View rootView = inflater.inflate(R.layout.fragment_result, container, false);

		context = container.getContext();
		cr = context.getContentResolver();
		uri = Uri.parse("content://forlang.provider/MyWords");

		// get arguments
		Bundle args = getArguments();
		wordFile = (WordFile)args.getSerializable("wordFile");
		wordList = args.getIntegerArrayList("wordList");
		results = args.getIntegerArrayList("results");
		examNo = args.getInt("examNo");

		for(int i = 0; i < wordList.size(); i++)
		{
			word = wordFile.getWordList().get(wordList.get(i));
			meanings = wordFile.getMeaningList().get(wordList.get(i));
			result = results.get(i);

			ContentValues values = new ContentValues();
			values.put("word", word);
			values.put("meaning1", meanings[0]);
			if(meanings.length >= 2)
				values.put("meaning2", meanings[1]);
			if(meanings.length >= 3)
				values.put("meaning3", meanings[2]);
			values.put("examNo", examNo);
			values.put("correct", result);
			cr.insert(uri, values);

			if(result == 1)
				score++;
		}

		TextView examScore = rootView.findViewById(R.id.tvExamScore);
		examScore.setText(String.valueOf(score * 10));

		// set search button listener
		Button buttonSubmit = rootView.findViewById(R.id.button_return);
		buttonSubmit.setOnClickListener(this);

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.button_return:
			{
				FragmentExam fragmentExam = new FragmentExam();
				Bundle args = new Bundle();
				args.putSerializable("wordFile", wordFile);
				fragmentExam.setArguments(args);
				getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentExam).commit();

				break;
			}
		}
	}
}