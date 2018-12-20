package com.example.csh.forlang;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class WordFile extends ArrayList<String[]> implements Serializable
{
	private ArrayList<String> wordList;
	private ArrayList<String[]> meaningList;

	public WordFile(Context context, String filename)
	{
		wordList = new ArrayList<>();
		meaningList = new ArrayList<>();

		try
		{
			InputStream is = context.getAssets().open(filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			for(String line = reader.readLine(); line != null; line = reader.readLine())
			{
				String word = line.split("\t", 2)[0];
				String[] meanings = line.split("\t", 2)[1].split(", ");

				wordList.add(word);
				meaningList.add(meanings);
			}

			reader.close();
			is.close();
		}
		catch(IOException e)
		{
			Log.e(e.getClass().toString(), e.getMessage().toString());
			e.printStackTrace();
			return;
		}
	}

	public ArrayList<String> getWordList()
	{
		return wordList;
	}

	public ArrayList<String[]> getMeaningList()
	{
		return meaningList;
	}

	public int getLength()
	{
		return wordList.size();
	}
}
