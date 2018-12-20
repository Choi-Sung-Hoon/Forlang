package com.example.csh.forlang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

public class ViewPagerWord extends FragmentStatePagerAdapter
{
	private final int MAX_WORDS = 10;
	private int wordsCount;
	private ArrayList<String> wordList;
	private ArrayList<String[]> meaningList;
	private ArrayList<FragmentWord> fragmentList;

	public ViewPagerWord(FragmentManager fm, ArrayList<String> wordList, ArrayList<String[]> meaningList)
	{
		super(fm);
		this.wordList = wordList;
		this.meaningList = meaningList;
		this.fragmentList = new ArrayList<>();

		for(wordsCount = 0; wordsCount < MAX_WORDS; wordsCount++)
		{
			FragmentWord fragmentWord = new FragmentWord();
			Bundle args = new Bundle();
			int index = new Random().nextInt(wordList.size());
			args.putString("word", wordList.get(index).toString());
			args.putStringArray("meanings", meaningList.get(index));
			fragmentWord.setArguments(args);

			fragmentList.add(fragmentWord);
		}
	}

	@Override
	public int getCount()
	{
		return wordsCount;
	}

	@Override
	public Fragment getItem(int position)
	{
		return fragmentList.get(position);
	}
}