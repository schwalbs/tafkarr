package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_file), MODE_PRIVATE);
        String auth = mSharedPrefs.getString(getString(R.string.prefs_key_auth_shared), null);
        //check if they're already logged in
        if (auth != null) {
            if (!auth.isEmpty()) {
                goToMainActivity();
            }
        }

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.required_field));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.required_field));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void goToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(i);
        finish();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpPost req = new HttpPost(getString(R.string.schedule_lookup_base));
            // authentication
            String authorization = new String(Base64.encodeBase64((mUsername + ":" + mPassword).getBytes()));
            req.addHeader(getString(R.string.request_authorization), "Basic " + authorization);
            return Util.sendHttpRequest(LoginActivity.this, req) != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // save password if remember me is checked and continue to main activity
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString(getString(R.string.prefs_key_auth_shared),  new String(Base64.encodeBase64((mUsername + ":" + mPassword).getBytes())));
                editor.putString(getString(R.string.prefs_key_username_shared), mUsername);
                editor.apply();
                goToMainActivity();
            } else {
                Toast.makeText(LoginActivity.this,getString(R.string.verification_error), Toast.LENGTH_SHORT).show();
                mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
