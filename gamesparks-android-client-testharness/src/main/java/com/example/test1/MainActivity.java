package com.example.test1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test1.models.GSConfig;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSMessageHandler.MatchFoundMessage;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder.EndSessionResponse;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
{
	private TextView 	mTextView1;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        setupButtons();

		initGS(this, new GSConfig() {

        });
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
				context,
				config.ApiKey,
                config.ApiSecret,
				config.ApiCredential,
				config.LiveMode,
                config.AutoUpdate);

		GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
			@Override
			public void onEvent(Boolean available) {
				if (!available) addLog("GameSparks not available");
                else {
                    String name = getRegistrationName();
                    sendRegistrationRequest(name, name, new GSEventConsumer<GSResponseBuilder.RegistrationResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.RegistrationResponse registrationResponse) {
                            sendMatchMakingRequest(config.Skill, config.MatchShortCode);
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

	private void sendRegistrationRequest(String username, String password, GSEventConsumer<GSResponseBuilder.RegistrationResponse> r) {
	    GSAndroidPlatform.gs().getRequestBuilder().createRegistrationRequest()
	            .setUserName(username)
                .setPassword(password)
                .setDisplayName(username)
                .send(r);
    }

    private void sendMatchMakingRequest(long skill, String shortCode) {
        GSAndroidPlatform.gs().getRequestBuilder().createMatchmakingRequest()
                .setSkill(skill)
                .setMatchShortCode(shortCode)
                .send(new GSEventConsumer<GSResponseBuilder.MatchmakingResponse>() {
                    @Override
                    public void onEvent(GSResponseBuilder.MatchmakingResponse matchmakingResponse) {
                        if (matchmakingResponse.hasErrors()) addLog("Matchmaking Error");
                        else addLog("Matchmaking Success");
                    }
                });
    }

	private void sendEndSessionRequest() {
        mTextView1.setText("Sending EndSessionRequest...");
        addLog("Sending EndSessionRequest...");
        GSAndroidPlatform.gs().getRequestBuilder().createEndSessionRequest()
            .send(new GSEventConsumer<EndSessionResponse>() {
            @Override
            public void onEvent(EndSessionResponse endSessionResponse) {
                if (endSessionResponse.hasErrors()) addLog("EndSession Error");
                else addLog("EndSession Success");
            }
        });
    }

    private void setupButtons() {
        mTextView1 = (TextView) findViewById(R.id.textView1);
        TextView mTextView2 = (TextView) findViewById(R.id.textView2);
        Button button6 = (Button)findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEndSessionRequest();
            }
        });
    }

    private String getRegistrationName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").toString();
    }

    private void addLog(String message) {
	    EditText e = (EditText) findViewById(R.id.editText1);
        e.setText(message + "\n" + e.getText().toString());
    }
}
