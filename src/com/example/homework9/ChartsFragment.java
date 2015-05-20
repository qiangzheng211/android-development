package com.example.homework9;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class ChartsFragment extends Fragment {
	
	private String message;
	private Button buttonpre;
	private Button buttonnext;
	private ImageSwitcher imageSwitcher;
	private TextSwitcher mSwitcher;
	private TextView text;
	JSONObject json;
	JSONObject result;
	JSONObject chart;
	String[] urlArray = new String[3];
	int index = 0; 
	String address;
	String textToShow[]={"Historical Zestimate for the past 1 year","Historical Zestimate for the past 5 years","Historical Zestimate for the past 10 years"};
	Animation out;
	Animation in;
			
	public static ChartsFragment newInstance(String json) {
		ChartsFragment f = new ChartsFragment();
		Bundle b = new Bundle();
        b.putString("msg", json);
        f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, null);
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		message = getArguments().getString("msg");
		View v = inflater.inflate(R.layout.activity_zillow_charts, container, false);
		
	    buttonpre = (Button) v.findViewById(R.id.button1);
	    buttonnext = (Button) v.findViewById(R.id.button2);
	    imageSwitcher = (ImageSwitcher) v.findViewById(R.id.imageSwitcher);
	    mSwitcher = (TextSwitcher) v.findViewById(R.id.textSwitcher);
	    text = (TextView) v.findViewById(R.id.textView);
	    TextView url1 = (TextView) v.findViewById(R.id.textView2);
		TextView url2 = (TextView) v.findViewById(R.id.textView3);
		url1.setMovementMethod(LinkMovementMethod.getInstance());
		url1.setText(Html.fromHtml("Use is subject to <a href='http://www.zillow.com/corp/Terms.htm'>Terms of Use</a>"));
		url2.setMovementMethod(LinkMovementMethod.getInstance());
		url2.setText(Html.fromHtml("<a href='http://www.zillow.com/zestimate/'>What's a Zestimate?</a>"));
	    
	    imageSwitcher.setFactory(new ViewFactory() {

	    	   @Override
	    	   public View makeView() {
	    	      ImageView myView = new ImageView(getActivity());
	    	      myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	    	      myView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    	      return myView;
	    	       }

	    	   });
	    
	   // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
	    mSwitcher.setFactory(new ViewFactory() {
            
            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like color, size etc
                TextView myText = new TextView(getActivity());
                myText.setTextSize(20);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                return myText;
            }
        });
	    
	 // Declare the in and out animations
	    out = AnimationUtils.loadAnimation(this.getActivity(),
	    	      android.R.anim.slide_out_right);
	    in = AnimationUtils.loadAnimation(this.getActivity(),
	    	      android.R.anim.slide_in_left);
	    
	    imageSwitcher.setInAnimation(in);
	    imageSwitcher.setOutAnimation(out);
	    
	 // set the animation type of textSwitcher
	    mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
	    
		try {
			json = new JSONObject(message);
			result = json.getJSONObject("result");
			chart = json.getJSONObject("chart");
			urlArray[0] = chart.getString("url1year");
			urlArray[1] = chart.getString("url5years");
			urlArray[2] = chart.getString("url10years");
			address = result.getString("street")+", "+result.getString("city")+", "+result.getString("state")+"-"+result.getString("zipcode");
			text.setText(address);
			new LoadImage().execute(urlArray[0]);
			mSwitcher.setText(textToShow[0]);
		    }
		catch (JSONException e) {
	         
            e.printStackTrace();
        }
		
		
		buttonpre.setOnClickListener(new View.OnClickListener() {
	          
	          public void onClick(View v) {
	                 // If index reaches minimum reset it
	        	  index --;
	        	  if(index == -1)
	                   index = 2;
	        	  new LoadImage().execute(urlArray[index]);
	        	  mSwitcher.setText(textToShow[index]);
	        	  
	          }
	      });
	    
	    buttonnext.setOnClickListener(new View.OnClickListener() {
	          
	          public void onClick(View v) {
	        	  
	                 // If index reaches maximum reset it
	     	      index ++;
	     	      if(index==3)
                     index=0;
	        	  new LoadImage().execute(urlArray[index]);
	        	  mSwitcher.setText(textToShow[index]);
	          }
	      });
		
		return v;
	}
	
	private class LoadImage extends AsyncTask<String, String, Drawable> {
	
	       protected Drawable doInBackground(String... args) {
	   	    try {
		        InputStream is = (InputStream) new URL(args[0]).getContent();
		        Drawable d = Drawable.createFromStream(is, null);
		        return d;
		    } catch (Exception e) {
		        return null; }
	       }
	       protected void onPostExecute(Drawable image) {
	         if(image != null){
	        	 imageSwitcher.setImageDrawable(image);
	         }else{
	         }
	       }
	}
 	   
}
