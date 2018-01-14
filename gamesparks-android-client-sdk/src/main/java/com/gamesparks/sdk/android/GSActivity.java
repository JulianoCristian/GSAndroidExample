/**
 * 
 */
package com.gamesparks.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author Giuseppe Perniola
 *
 */
public class GSActivity extends Activity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_gs);
        
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	}
}
