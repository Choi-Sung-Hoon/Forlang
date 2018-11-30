package com.example.csh.forlang;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DBProvider extends ContentProvider
{
	static final Uri CONTENT_URI = Uri.parse("content://forlang.provider/MyWords");
	static final int GETALL = 1;
	static final int GETONE = 2;

	static final UriMatcher matcher;
	static
	{
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI("forlang.provider", "MyWords", GETALL);
		matcher.addURI("forlang.provider", "MyWords/*", GETONE);
	}

	SQLiteDatabase mDB;
	class DBHelper extends SQLiteOpenHelper
	{
		public DBHelper(Context context)
		{
			super(context, "MyWords.db", null, 1);
		}

		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("create table if not exists MyWords(_id int primary key, word text unique, meaning1 text, meaning2 text, meaning3 text, examNo int);");
		}

		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
		{
			db.execSQL("drop table if exists MyWords;");
			onCreate(db);
		}
	}

	public DBProvider()
	{
	}

	@Override
	public boolean onCreate()
	{
		DBHelper helper = new DBHelper(getContext());
		mDB = helper.getWritableDatabase();
		return (mDB != null);
	}

	@Override
	public String getType(Uri uri)
	{
		switch(matcher.match(uri))
		{
			case GETALL:
				return "vnd.android.cursor.dir/vnd.com.example.csh.forlang.MyWords";
			case GETONE:
				return "vnd.android.cursor.item/vnd.com.example.csh.forlang.MyWords";
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		long row = mDB.insertWithOnConflict("MyWords", null, values, SQLiteDatabase.CONFLICT_REPLACE);

		if(row > 0)
		{
			Uri notiuri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(notiuri ,null);
			return notiuri;
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		/*
		String sql = "select * from MyWords";
		if(matcher.match(uri) == GETONE)
		{
			sql += " WHERE examNo ='";
			sql += uri.getPathSegments().get(1);
			sql += "'";
		}
		sql += ";";

		Cursor cursor = mDB.rawQuery(sql, null);
		return cursor;
		*/
		//Cursor cursor = mDB.query("MyWords", projection, selection, selectionArgs, null, null, sortOrder);
		//cursor.moveToFirst();
		Cursor cursor = mDB.rawQuery("select * from MyWords", null);
		return cursor;
	}
}