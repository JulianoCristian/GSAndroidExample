package com.example.test1;

import android.util.Log;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.test1.models.GSConfig;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSMessageHandler.MatchFoundMessage;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;

/**
 * Stephen Callaghan <stephen.callaghan@gamesparks.com>
 * Edited: 2018/01/17
 * Created: 2018/01/16
 */

public class MainActivity extends AppCompatActivity
{
    private EditText _username;
    private EditText _password;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_username = (EditText) findViewById(R.id.editUsernameText);
		_password = (EditText) findViewById(R.id.editPasswordText);

		// Initialize GS
		initGS(this, new GSConfig() {{ // Initialize GameSparks
            Skill = 0;
            LiveMode = false;
            AutoUpdate = true;
            MatchShortCode = "Alpha";
            ApiKey = BuildConfig.GAMESPARKS_API_KEY;
            ApiSecret = BuildConfig.GAMESPARKS_API_SECRET;
            ApiCredential = BuildConfig.GAMESPARKS_API_CREDENTIAL;
        }});
	}

	@Override
	public void onStart() {
		super.onStart();
        GSAndroidPlatform.gs().start();
	}

	@Override
	public void onStop() {
		super.onStop();
        GSAndroidPlatform.gs().stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void OnAuthButtonClick(View v) {
        Log.d("authenticate", "fired");
        authenticate(_username.getText().toString(), _password.getText().toString());
    }

	public void authenticate(final String username, final String password) {
        GSService.sendAuthenticationRequest(username, password, new GSEventConsumer<GSResponseBuilder.AuthenticationResponse>() {
            @Override
            public void onEvent(GSResponseBuilder.AuthenticationResponse authenticationResponse) {

                if (!authenticationResponse.hasErrors()) {
                    // successful authentication
                    Log.d("Authentication", "Successful Auth");

                } else {
                    GSService.sendRegistrationRequest(username, password,  new GSEventConsumer<GSResponseBuilder.RegistrationResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.RegistrationResponse registrationResponse) {

                            if (registrationResponse.hasErrors()) return;
                            Log.d("Registration", "Successful Auth");
                        }
                    });
                }
            }
        });
    }

	private void initGS(Context context, final GSConfig config) {
        // Initialize the SDK
		GSAndroidPlatform.initialise(
				context, config.ApiKey,
                config.ApiSecret, config.ApiCredential,
				config.LiveMode, config.AutoUpdate);

        // Setup GS Listeners
        initGSListeners();
	}

    private void initGSListeners() {
        // On Gamesparks Available Listener
        GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
            @Override
            public void onEvent(Boolean available) {

                Log.d("fired", "fired");
                //if (!available) addLog("GameSparks not available");
                //else {
            }
        });
        // On Match Found Listener
        GSAndroidPlatform.gs().getMessageHandler().setMatchFoundMessageListener(new GSEventConsumer<MatchFoundMessage>() {
            @Override
            public void onEvent(MatchFoundMessage matchFoundMessage) {
                //matchFoundMessage.getAccessToken()
            }
        });
    }
}
