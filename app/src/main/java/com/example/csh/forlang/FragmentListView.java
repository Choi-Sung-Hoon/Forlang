package com.example.csh.forlang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentListView extends DialogFragment
{
	private WordListAdapter wordListAdapter;
	private int examNo;

	public FragmentListView()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_listview, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		ListView listView = view.findViewById(R.id.list_word);

		// get arguments
		Bundle args = getArguments();
		examNo = args.getInt("examNo");
		wordListAdapter = (WordListAdapter)args.getSerializable("wordListAdapter");
		// set adapter
		listView.setAdapter(wordListAdapter);

		// set TextView
		TextView tvTitle = view.findViewById(R.id.tvTitle);
		tvTitle.setText(examNo + "회차");

		// set TextView
		int score = args.getInt("score");
		System.out.println(score);
		TextView tvExamScore = view.findViewById(R.id.tvExamScore);
		tvExamScore.setText((score * 10) + " / 100");
	}
}
