package com.example.homework9;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.AppEventsLogger;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    
    public final static String EXTRA_MESSAGE = "com.example.homework9.MESSAGE";
	Spinner spinner;
	Button button;
	Button imagebutton;
	Intent intent;
	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setTitle("CSCI571 Homework 9");
	    getSupportActionBar().setIcon(R.drawable.ic_launcher);	    

		spinner = (Spinner) findViewById(R.id.spinner1);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.states_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	    
	 }
	
	 @Override
     public void onItemSelected(AdapterView<?> adapterView, View view,
                            int i, long l) {
      
     }
	 
     @Override
     public void onNothingSelected(AdapterView<?> arg0) {
              
     }
     
     public int emptyCheck(String str, TextView error)
     {   
    	 int index = 0;
    	 
         if(TextUtils.isEmpty(str)){
                 error.setVisibility(View.VISIBLE);
                 index = 1;
         }
		return index;

     }
     
     public int spinnerCheck(String str, TextView error)
     {   
    	 int index = 0;
    	 
         if(str.trim().equals("Choose State")){
                 error.setVisibility(View.VISIBLE);
                 index = 1;
         }
		return index;

     }
     
     public void gotoresult (View view) 
	 {   TextView error1 = (TextView) findViewById(R.id.streeterror); 
	     TextView error2 = (TextView) findViewById(R.id.cityerror);  
    	 TextView error3 = (TextView) findViewById(R.id.staterror); 
    	 intent = new Intent(this, Slidingtabs.class);
    	 aq = new AQuery(this);
    	 EditText street = (EditText) findViewById(R.id.street);
    	 EditText city = (EditText) findViewById(R.id.city);
    	 Spinner spinner = (Spinner) findViewById(R.id.spinner1);
    	 String message1 = street.getText().toString();
    	 String message2 = city.getText().toString();
    	 String message3 = spinner.getSelectedItem().toString();
    	 int k1=emptyCheck(message1,error1);
    	 if(k1==0)
    	 { error1.setVisibility(View.GONE); }
    	 int k2=emptyCheck(message2,error2);
    	 if(k2==0)
    	 { error2.setVisibility(View.GONE); }
    	 int k3=spinnerCheck(message3,error3);
    	 if(k3==0)
    	 { error3.setVisibility(View.GONE); }
      
    	 if((k1|k2|k3)==0)
	     {
         String url = "http://qiangzheng-env.elasticbeanstalk.com//?streetInput="+ message1 +"&cityInput=" + message2 +"&stateInput=" + message3;
         
         aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                 @Override
                 public void callback(String url, JSONObject json, AjaxStatus status) {
             	    TextView error4 = (TextView) findViewById(R.id.error);  
                         
                         if(json != null){
                     	         error4.setVisibility(View.GONE);
                                 //successful ajax call, show status code and json content 
                     	         intent.putExtra(EXTRA_MESSAGE, json.toString());
                    	         startActivity(intent);
                           
                         }else{
                                //ajax error, show error code
                        	    error4.setVisibility(View.VISIBLE);
                         }
                 }
         });
	     }
	 }
     
     public void gotourl (View view) 
	 {
    	 Uri uri = Uri.parse("http://www.zillow.com");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
	 }
						

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	    

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
	  super.onResume();

	  // Logs 'install' and 'app activate' App Events.
	  AppEventsLogger.activateApp(this);
	}
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	  AppEventsLogger.deactivateApp(this);
	}
	
	
}
