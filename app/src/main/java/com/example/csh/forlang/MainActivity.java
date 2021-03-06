package com.example.csh.forlang;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity	implements NavigationView.OnNavigationItemSelectedListener
{
	private WordFile wordFile;
	private ViewPager wordViewPager;
	private FragmentStatePagerAdapter fragmentPagerAdapter;
	private ViewPagerTransformer viewPagerTransformer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set toolbar
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		// set toolbar
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// add text asset
		wordFile = new WordFile(this, "words.txt");

		// set the main fragment to FragmentWord
		fragmentPagerAdapter = new ViewPagerWord(getSupportFragmentManager(), wordFile.getWordList(), wordFile.getMeaningList());
		viewPagerTransformer = new ViewPagerTransformer();
		wordViewPager = findViewById(R.id.wordViewPager);
		wordViewPager.setPageTransformer(true, viewPagerTransformer);
		wordViewPager.setAdapter(fragmentPagerAdapter);
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		FragmentManager fragmentManager = getSupportFragmentManager();

		// remove all fragments in the container
		if(id != R.id.nav_developer)
			for(Fragment f : fragmentManager.getFragments())
				fragmentManager.beginTransaction().remove(f).commit();

		if (id == R.id.nav_word)
		{
			if(fragmentPagerAdapter != null)
			{
				wordViewPager.removeAllViews();
				wordViewPager.setAdapter(null);
			}

			setTitle("단어암기");
			wordViewPager.setPageTransformer(true, viewPagerTransformer);
			fragmentPagerAdapter = new ViewPagerWord(getSupportFragmentManager(), wordFile.getWordList(), wordFile.getMeaningList());
			wordViewPager.setAdapter(fragmentPagerAdapter);
		}
		else if (id == R.id.nav_exam)
		{
			if(fragmentPagerAdapter != null)
			{
				wordViewPager.removeAllViews();
				wordViewPager.setAdapter(null);
			}

			setTitle("시험");
			FragmentExam fragmentExam = new FragmentExam();
			Bundle args = new Bundle();
			args.putSerializable("wordFile", wordFile);
			fragmentExam.setArguments(args);
			fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentExam).commit();
		}
		else if (id == R.id.nav_notebook)
		{
			if(fragmentPagerAdapter != null)
			{
				wordViewPager.removeAllViews();
				wordViewPager.setAdapter(null);
			}

			setTitle("단어장");
			FragmentNotebook fragmentNotebook = new FragmentNotebook();
			fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentNotebook).commit();
		}
		else if (id == R.id.nav_developer)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(R.drawable.dialog_icon_knu);
			dialog.setTitle("경북대학교");
			dialog.setMessage("컴퓨터학부 2014105091 최성훈");
			dialog.setPositiveButton("확인", null);
			dialog.show();
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}