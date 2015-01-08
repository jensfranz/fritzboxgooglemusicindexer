package jensfranz.de.fritzboxgooglemusicindexer.mvc.controller;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import jensfranz.de.fritzboxgooglemusicindexer.R;
import jensfranz.de.fritzboxgooglemusicindexer.business.FritzBoxBusiness;
import jensfranz.de.fritzboxgooglemusicindexer.event.EventListener;
import jensfranz.de.fritzboxgooglemusicindexer.event.EventManager;
import jensfranz.de.fritzboxgooglemusicindexer.event.LoggedInEvent;
import jensfranz.de.fritzboxgooglemusicindexer.event.SignInPressedEvent;
import jensfranz.de.fritzboxgooglemusicindexer.mvc.model.FritzboxInformation;
import jensfranz.de.fritzboxgooglemusicindexer.validator.FritzBoxInformationValidator;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements EventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventManager.registerListener(this);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(final FritzboxInformation fritzboxInformation) {
        // Store values at the time of the login attempt.
        String email = fritzboxInformation.getUser();
        String password = fritzboxInformation.getPassword();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !FritzBoxInformationValidator.isPasswordValid(password)) {
            cancel = true;
        }

        // Check for a valid email address.
        if (!FritzBoxInformationValidator.isEmailValid(email)) {
            cancel = true;
        }

        if (!cancel) {
            doRequest(fritzboxInformation);
        }
    }

    private void doRequest(final FritzboxInformation fritzboxInformation) {
        FritzBoxBusiness.login(this, "fritz.box", fritzboxInformation.getPassword(), new FritzBoxBusiness.LoginCallback() {
            @Override
            public void loggedIn(String sid) {
                EventManager.fireEvent(new LoggedInEvent(true));
            }

            @Override
            public void failed(String reason) {
                EventManager.fireEvent(new LoggedInEvent(reason));
            }
        });
    }

    @Override
    public void eventOccured(Object event) {
        if (event instanceof SignInPressedEvent) {
            final SignInPressedEvent signInPressedEvent = (SignInPressedEvent) event;
            attemptLogin(signInPressedEvent.getFritzboxInformation());
        }
    }
}



