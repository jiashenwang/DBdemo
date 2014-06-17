package com.example.databasedemo;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button save, load;
	EditText name, email;
	DataHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        save = (Button)findViewById(R.id.button1);
        load = (Button)findViewById(R.id.button2);
        
        name = (EditText)findViewById(R.id.text1);
        email = (EditText)findViewById(R.id.text2);
        
        save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

			}
        	
        });
        
        load.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String getName, getEmail;
				getName = "";
				getEmail = "";
				handler = new DataHandler(getBaseContext());
				handler.open();
				Cursor C = handler.returnData();
				
				if(C.moveToFirst())
				{
					do
					{
						if(getName == C.getString(1));
						{
							getEmail = C.getString(0);
							Toast.makeText(getBaseContext(), "SMILES: "+getEmail , Toast.LENGTH_LONG).show();
						}
					}while(C.moveToNext());
				}
				
				handler.close();
			}
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
