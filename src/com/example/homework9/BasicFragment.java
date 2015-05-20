package com.example.homework9;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class BasicFragment extends Fragment {

	public static BasicFragment newInstance(String json) {
		BasicFragment f = new BasicFragment();
		Bundle b = new Bundle();
        b.putString("msg", json);
        f.setArguments(b);
		return f;
	}
	
	String homedetailurl;
	String chart1url;
	String addressdetail;
	String solddetail;
	
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
	private Button shareButton;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	
	private boolean pendingPublishReauthorization = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_zillow_basicinfo, container, false);
		
		String message = getArguments().getString("msg");
		TextView address = (TextView) view.findViewById(R.id.address);
		TextView propertytype = (TextView) view.findViewById(R.id.propertytype);
		TextView yearbuilt = (TextView) view.findViewById(R.id.yearbuilt);
		TextView lotsize = (TextView) view.findViewById(R.id.lotsize);
		TextView finiarea = (TextView) view.findViewById(R.id.finiarea);
		TextView bathrooms = (TextView) view.findViewById(R.id.bathrooms);
		TextView bedrooms = (TextView) view.findViewById(R.id.bedrooms);
		TextView taxyear = (TextView) view.findViewById(R.id.taxyear);
		TextView tax = (TextView) view.findViewById(R.id.tax);
		TextView price = (TextView) view.findViewById(R.id.price);
		TextView date = (TextView) view.findViewById(R.id.date);
		TextView zestimate = (TextView) view.findViewById(R.id.zestimate);
		TextView overallchange = (TextView) view.findViewById(R.id.overallchange);
		TextView range1 = (TextView) view.findViewById(R.id.range1);
		TextView rzestimate = (TextView) view.findViewById(R.id.rzestimate);
		TextView rentchange = (TextView) view.findViewById(R.id.rentchange);
		TextView range2 = (TextView) view.findViewById(R.id.range2);
		TextView zestimatedate = (TextView) view.findViewById(R.id.zestimatedate);
		String string1 = zestimatedate.getText().toString();
		TextView rzestimatedate = (TextView) view.findViewById(R.id.rzestimatedate);
		String string2 = rzestimatedate.getText().toString();
		
		TextView url1 = (TextView) view.findViewById(R.id.textView2);
		TextView url2 = (TextView) view.findViewById(R.id.textView3);
		
		try {
			JSONObject json = new JSONObject(message);
			JSONObject result = json.getJSONObject("result");
			JSONObject chart = json.getJSONObject("chart");
			homedetailurl = result.getString("homedetails");
			addressdetail = result.getString("street")+", "+result.getString("city")+", "+result.getString("state")+"-"+result.getString("zipcode");
			chart1url = chart.getString("url1year");
			solddetail = "Last Sold Price: "+result.getString("lastSoldPrice")+", 30 Days Overall Change: " +result.getString("estimateValueChangeSign")+result.getString("estimateValueChange");
			address.setMovementMethod(LinkMovementMethod.getInstance());
			address.setText(Html.fromHtml("<a href='"+result.getString("homedetails")+"'>"+result.getString("street")+", "+result.getString("city")+", "+result.getString("state")+"-"+result.getString("zipcode")+"</a>"));
			propertytype.setText(result.getString("useCode"));
			yearbuilt.setText(result.getString("yearBuilt"));
			lotsize.setText(result.getString("lotSizeSqFt"));
			finiarea.setText(result.getString("finishedSqFt"));
			bathrooms.setText(result.getString("bathrooms"));
			bedrooms.setText(result.getString("bedrooms"));
			taxyear.setText(result.getString("taxAssessmentYear"));
			tax.setText(result.getString("taxAssessment"));
			price.setText(result.getString("lastSoldPrice"));
			date.setText(result.getString("lastSoldDate"));
			zestimate.setText(result.getString("estimateAmount"));
			if(result.getString("estimateValueChangeSign").equals("+"))
			{
				String html1 = "<img src=\'up_g.gif\'>" + result.getString("estimateValueChange");
				overallchange.setText(Html.fromHtml(html1, new ImageGetter(), null));	
			}
			else 
			{
				String html2 = "<img src=\'down_r.gif\'>" + result.getString("estimateValueChange");
				overallchange.setText(Html.fromHtml(html2, new ImageGetter(), null));
			}
			range1.setText(result.getString("estimateValuationRangeLow")+"-"+result.getString("estimateValuationRangeHigh"));
			rzestimate.setText(result.getString("restimateAmount"));
			if(result.getString("restimateValueChangeSign").equals("+"))
			{
				String html1 = "<img src=\'up_g.gif\'>" + result.getString("restimateValueChange");
				rentchange.setText(Html.fromHtml(html1, new ImageGetter(), null));
			}
			else 
			{
				String html2 = "<img src=\'down_r.gif\'>" + result.getString("restimateValueChange");
				rentchange.setText(Html.fromHtml(html2, new ImageGetter(), null));
			}
			range2.setText(result.getString("restimateValuationRangeLow")+"-"+result.getString("restimateValuationRangeHigh"));
			zestimatedate.setText(string1+result.getString("estimateLastUpdate"));
			rzestimatedate.setText(string2+result.getString("restimateLastUpdate"));
			
			url1.setMovementMethod(LinkMovementMethod.getInstance());
			url1.setText(Html.fromHtml("Use is subject to <a href='http://www.zillow.com/corp/Terms.htm'>Terms of Use</a>"));
			url2.setMovementMethod(LinkMovementMethod.getInstance());
			url2.setText(Html.fromHtml("<a href='http://www.zillow.com/zestimate/'>What's a Zestimate?</a>"));
		}
		catch (JSONException e) {
	         
            e.printStackTrace();
        }
        
		shareButton = (Button) view.findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	new AlertDialog.Builder(getActivity())
		        .setMessage("Post to Facebook")
		        .setPositiveButton("Post Property Details", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) { 
		            	
		            	onClickLogin();
		            	    Session session = Session.getActiveSession();
		            	    if(session.isOpened())
		            	    {
		            	    	publishFeedDialog();}
	            
		                }
		         })
		        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) { 
		            	Toast.makeText(getActivity(),"Post Cancelled", 
		                        Toast.LENGTH_SHORT).show();		            }
		         })
		        .setIcon(android.R.drawable.ic_dialog_alert)
		         .show();
		    	       
		    }
		});
		
		if (savedInstanceState != null) {
			pendingPublishReauthorization = 
				savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
		}
		return view;
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
        uiHelper.onSaveInstanceState(outState);
    }
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            if (pendingPublishReauthorization && 
            		state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
            	pendingPublishReauthorization = false;
            	publishFeedDialog();
            }
        } else if (state.isClosed()) {
        }
    }

	public void publishFeedDialog() {
	    Session session = Session.getActiveSession();
	    if (session != null) {

		    // Check for publish permissions    
		    List<String> permissions = session.getPermissions();
		        if (!isSubsetOf(PERMISSIONS, permissions)) {
		        	pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(this, PERMISSIONS);
		            session.requestNewPublishPermissions(newPermissionsRequest);
		            return;
		       }

		        Bundle params = new Bundle();
			    params.putString("name", addressdetail);
			    params.putString("caption", "Property Information from Zillow.com");
			    params.putString("description", solddetail);
			    params.putString("link",homedetailurl );
			    params.putString("picture", chart1url);

			    WebDialog feedDialog = (
				        new WebDialog.FeedDialogBuilder(getActivity(),
				            session,
				            params))
				        .setOnCompleteListener(new OnCompleteListener() {

				            @Override
				            public void onComplete(Bundle values,
				                FacebookException error) {
			                if (error == null) {
			                    // When the story is posted, echo the success
			                    // and the post Id.
			                    final String postId = values.getString("post_id");
			                    if (postId != null) {
			                        Toast.makeText(getActivity(),
			                            "Posted story, ID: "+postId,
			                            Toast.LENGTH_SHORT).show();
			                    } else {
			                        // User clicked the Cancel button
			                        Toast.makeText(getActivity().getApplicationContext(), 
			                            "Post cancelled", 
			                            Toast.LENGTH_SHORT).show();
			                    }
			                } else if (error instanceof FacebookOperationCanceledException) {
			                    // User clicked the "x" button
			                    Toast.makeText(getActivity().getApplicationContext(), 
			                        "Post cancelled", 
			                        Toast.LENGTH_SHORT).show();
			                } else {
			                    // Generic, ex: network error
			                    Toast.makeText(getActivity().getApplicationContext(), 
			                        "Error posting story", 
			                        Toast.LENGTH_SHORT).show();
			                }
			            }

			        })
			        .build();
			    feedDialog.show();
		}
	}
	
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(callback));
        } else {
            Session.openActiveSession(getActivity(), this, true, callback);
        }
    }
    
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	private class ImageGetter implements Html.ImageGetter {
		
	public Drawable getDrawable(String source) {
        int id;
        if (source.equals("up_g.gif")) {
               id = R.drawable.up_g;
        }
        else if (source.equals("down_r.gif")) {
            id = R.drawable.down_r;
        }
        else{
            return null;
        }

       Drawable d = getResources().getDrawable(id);
       d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
       return d;
     }
	};


}
