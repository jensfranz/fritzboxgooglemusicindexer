package jensfranz.de.fritzboxgooglemusicindexer.mvc.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jensfranz.de.fritzboxgooglemusicindexer.R;
import jensfranz.de.fritzboxgooglemusicindexer.event.EventListener;
import jensfranz.de.fritzboxgooglemusicindexer.event.EventManager;
import jensfranz.de.fritzboxgooglemusicindexer.event.LoggedInEvent;
import jensfranz.de.fritzboxgooglemusicindexer.event.SignInPressedEvent;
import jensfranz.de.fritzboxgooglemusicindexer.mvc.model.FritzboxInformation;
import jensfranz.de.fritzboxgooglemusicindexer.validator.FritzBoxInformationValidator;
import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public class LoginFragment extends RoboFragment implements EventListener {

    @InjectView(R.id.email)
    private AutoCompleteTextView mEmailView;

    @InjectView(R.id.password)
    private EditText mPasswordView;

    @InjectView(R.id.text)
    private TextView mTextView;

    @InjectView(R.id.login_progress)
    private View mProgressView;

    @InjectView(R.id.login_form)
    private View mLoginFormView;

    @InjectView(R.id.email_sign_in_button)
    private Button mEmailSignInButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.registerListener(this);
    }

    private FritzboxInformation getFritzboxInformation() {
        final FritzboxInformation fritzboxInformation = new FritzboxInformation();
        fritzboxInformation.setPassword(mPasswordView.getText().toString());
        return fritzboxInformation;
    }

    private void attemptLogin() {
        boolean cancel = false;
        View focusView = null;

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        // Reset errors.
        mPasswordView.setError(null);
        mEmailView.setError(null);

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !FritzBoxInformationValidator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!FritzBoxInformationValidator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    EventManager.fireEvent(new SignInPressedEvent(getFritzboxInformation()));
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                EventManager.fireEvent(new SignInPressedEvent(getFritzboxInformation()));
            }
        });
    }

    @Override
    public void eventOccured(Object event) {
        if (event instanceof LoggedInEvent) {
            final LoggedInEvent loggedInEvent = (LoggedInEvent) event;
            if (loggedInEvent.isSuccess()) {
                mTextView.setText("Request sent.");
                showProgress(false);
            } else {
                mTextView.setText("Failed: " + loggedInEvent.getFailureReason());
                showProgress(false);
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
