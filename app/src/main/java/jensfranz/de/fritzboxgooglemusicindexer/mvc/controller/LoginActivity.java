package jensfranz.de.fritzboxgooglemusicindexer.mvc.controller;

import android.text.TextUtils;

import com.google.inject.Inject;

import jensfranz.de.fritzboxgooglemusicindexer.R;
import jensfranz.de.fritzboxgooglemusicindexer.business.FritzBoxBusiness;
import jensfranz.de.fritzboxgooglemusicindexer.event.LoggedInEvent;
import jensfranz.de.fritzboxgooglemusicindexer.event.SignInPressedEvent;
import jensfranz.de.fritzboxgooglemusicindexer.mvc.model.FritzboxInformation;
import jensfranz.de.fritzboxgooglemusicindexer.validator.FritzBoxInformationValidator;
import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContentView;


/**
 * A login screen that offers login via email/password.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {

    @Inject
    private EventManager eventManager;

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
                eventManager.fire(new LoggedInEvent(true));
            }

            @Override
            public void failed(String reason) {
                eventManager.fire(new LoggedInEvent(reason));
            }
        });
    }

    private void onSignPressed(@Observes final SignInPressedEvent signInPressedEvent) {
        attemptLogin(signInPressedEvent.getFritzboxInformation());
    }
}



