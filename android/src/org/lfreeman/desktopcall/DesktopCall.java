package org.lfreeman.desktopcall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DesktopCall extends Activity {

    private static final String LOG_TAG                        = "DesktopCall";
    private DesktopCallApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (DesktopCallApplication) getApplication();
        if (mApplication.isLocked()) {
            launchLoginScreen();
        }
        
        
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mApplication.isLocked()) {
            launchLoginScreen();
        }else{
            setPushNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.desktop_call, menu);
        return true;
    }

    protected void launchLoginScreen() {
        Intent launchLogin = new Intent(this, LoginActivity.class);
        startActivity(launchLogin);
    }

    public void handleLogout(View v) {
        mApplication.lockApplication();
        launchLoginScreen();
    }
    
    private void setPushNotification(){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("owner", ParseUser.getCurrentUser());
        installation.saveInBackground(new SaveCallback() {
            
            @Override
            public void done(ParseException e) {
                if (e != null) {
                  Log.d(LOG_TAG, e.getLocalizedMessage());
             }
                
            }
        });
        
    }
}
