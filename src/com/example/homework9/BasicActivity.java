package com.example.homework9;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;


public class BasicActivity extends FragmentActivity {
	
	private BasicFragment basicFragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        if (savedInstanceState == null) {
        	// Add the fragment on initial activity setup
        	basicFragment = new BasicFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, basicFragment)
            .commit();
        } else {
        	// Or set the fragment from restored state info
        	basicFragment = (BasicFragment) getSupportFragmentManager()
        	.findFragmentById(android.R.id.content);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic, menu);
        return true;
    }
}
