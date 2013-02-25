package org.lfreeman.desktopcall;

import android.app.Application;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

public class DesktopCallApplication extends Application {
    private boolean locked;

    @Override
    public void onCreate() {
        super.onCreate();
        locked = true;

        initCloud();
    }

    public boolean isLocked() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            locked = true;
        } else {
            locked = false;
        }

        return locked;
    }

    public void lockApplication() {
        parseLogout();
        locked = true;

    }

    public void unlockApplication(String user, String password, LoginActivity context) {
        parseLogin(user, password, context);

    }
    
    public void registerUser(String user, String password, LoginActivity context) {
        parseRegister(user, password, context);

    }

    private void parseLogout() {
        ParseUser.logOut();
    }

    private void parseLogin(String user, String password, LoginActivity context) {
        final LoginActivity localContext = context;
        ParseUser.logInInBackground(user, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                boolean status = (user != null) ? true : false;
                locked = !status;
                localContext.loginCompleted(status);

            }
        });
    }
    
    private void parseRegister(String user, String password, LoginActivity context){
        final LoginActivity localContext = context;
        ParseUser u = new ParseUser();
        u.setUsername(user);
        u.setPassword(password);
        //u.setEmail("email@example.com");
        
        u.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                boolean status = (e == null) ? true : false;
                localContext.registerCompleted(status);              
            }
          });
    }

    private void initCloud() {
        String YOUR_APPLICATION_ID = getString(R.string.parse_application_id);
        String YOUR_CLIENT_KEY = getString(R.string.parse_client_key);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        PushService.subscribe(this, "", DesktopCall.class);
        PushService.setDefaultPushCallback(this,DesktopCall.class);

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(false);
        defaultACL.setPublicWriteAccess(false);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}