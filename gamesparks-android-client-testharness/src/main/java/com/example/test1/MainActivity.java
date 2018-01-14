package com.example.test1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.test1.models.GSConfig;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSMessageHandler.MatchFoundMessage;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
{
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initGS(this, new GSConfig() {{ // Initialize GameSparks
            Skill = 0;
            ApiKey = "";
            ApiSecret = "";
            ApiCredential = "";
            LiveMode = false;
            AutoUpdate = true;
            MatchShortCode = "Alpha";
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

	private void initGS(Context context, final GSConfig config) {

		GSAndroidPlatform.initialise(
				context, config.ApiKey,
                config.ApiSecret, config.ApiCredential,
				config.LiveMode, config.AutoUpdate);

		GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
			@Override
			public void onEvent(Boolean available) {

				if (!available) addLog("GameSparks not available");
                else {

                    String name = getRegistrationName();
                    GSService.sendRegistrationRequest(
                            name, name,
                            new GSEventConsumer<GSResponseBuilder.RegistrationResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.RegistrationResponse registrationResponse) {

                            GSService.sendMatchMakingRequest(config.Skill, config.MatchShortCode,
                                new GSEventConsumer<GSResponseBuilder.MatchmakingResponse>() {
                                    @Override
                                    public void onEvent(GSResponseBuilder.MatchmakingResponse matchmakingResponse) {

                                        if (matchmakingResponse.hasErrors()) addLog("Matchmaking Error");
                                        else addLog("Matchmaking Success");
                                    }
                                });
                            }
                        });
                    }
				}
			});


        GSAndroidPlatform.gs().getMessageHandler().setMatchFoundMessageListener(new GSEventConsumer<MatchFoundMessage>() {
            @Override
            public void onEvent(MatchFoundMessage matchFoundMessage) {
                //matchFoundMessage.getAccessToken()
            }
        });
	}

    private String getRegistrationName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").toString();
    }

    private void addLog(String message) {
	    //EditText e = (EditText) findViewById(R.id.editText1);
        //e.setText(message + "\n" + e.getText().toString());
    }

    private Button _authBtn = (Button) findViewById(R.id.authButton);
    private EditText _usernameTxt = (EditText) findViewById(R.id.editUsernameText);
    private EditText _passwordTxt = (EditText) findViewById(R.id.editPasswordText);
}
