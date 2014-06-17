package com.example.databasedemo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.example.databasedemo.StackOverflowXmlParser.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler {

	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String TABLE_NAME = "mytable";
	public static final String DATA_BASE_NAME = "mydatabase3";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_CREATE = "create table mytable (name test not null, email text not null);";

	DataBaseHelper dbhelper;
	Context ctx;
	static SQLiteDatabase db;
	
	public DataHandler(Context ctx)
	{
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}
	
	private static class DataBaseHelper extends SQLiteOpenHelper
	{
		Context context;
		
		public DataBaseHelper(Context ctx)
		{
			super(ctx, DATA_BASE_NAME,null, DATABASE_VERSION);
			context = ctx;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL(TABLE_CREATE);
			
			List<Entry> entries = null;
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
			InputStream in = context.getResources().openRawResource(R.xml.testingdb);
			
			try {
				entries = stackOverflowXmlParser.parse(in);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Iterator it = entries.iterator();
			while(it.hasNext())
			{
				Entry temp = (Entry)it.next();
				insertData(temp.Ligand_SMILES, temp.BindingDB_MonomerID, temp.BindingDB_Ligand_Name,
							temp.Ki, temp.IC50, temp.Link, temp.UniProt_Recommended_Name_of_Target_Chain,
							temp.UniProt_Primary_ID_of_Target_Chain);
			}

			
		}


		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS mytable");

			onCreate(db);
		}
		
	}
	
	public DataHandler open()
	{
		db = dbhelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbhelper.close();
	}
	public static long insertData(String a, String b, String c, String d, String e, String f, String g, String h)
	{
		ContentValues content = new ContentValues();
		content.put("Ligand_SMILES", a);
		content.put("BindingDB_MonomerID", b);
		content.put("BindingDB_Ligand_Name", c);
		content.put("Ki", d);
		content.put("IC50", e);
		content.put("Link", f);
		content.put("UniProt_Recommended_Name_of_Target_Chain", g);
		content.put("UniProt_Primary_ID_of_Target_Chain", h);
		return db.insertOrThrow(TABLE_NAME, null, content);
	}
	public Cursor returnData()
	{
		return db.query(TABLE_NAME, new String[] {NAME, EMAIL}, null, null, null, null, null);
	}
}
