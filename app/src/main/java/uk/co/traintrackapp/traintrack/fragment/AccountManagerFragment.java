package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class AccountManagerFragment extends Fragment {

    private ProgressBar progress;
    private Button update;
    private Button logout;
    private Button login;
    private Button signup;

    public static AccountManagerFragment newInstance() {
        return new AccountManagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_account_manager, container, false);
        final TrainTrack app = (TrainTrack) getActivity().getApplication();
        final User user = app.getUser();

        final EditText username = (EditText) v.findViewById(R.id.username);
        final EditText email = (EditText) v.findViewById(R.id.email);
        final EditText password = (EditText) v.findViewById(R.id.password);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        signup = (Button) v.findViewById(R.id.signup);
        login = (Button) v.findViewById(R.id.login);
        update = (Button) v.findViewById(R.id.update);
        logout = (Button) v.findViewById(R.id.logout);
        updateButtonVisibility(user.isLoggedIn());
        username.setText(user.getUsername());
        email.setText(user.getEmail());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Send to server
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.save(getActivity());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                new LoginRequest().execute(email.getText().toString(),
                        password.getText().toString());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.logout(getActivity());
                app.setUser(user);
                Toast.makeText(getActivity(), "Goodbye " + user.getUsername(), Toast.LENGTH_LONG).show();
                getActivity().recreate();
            }
        });

        return v;
    }

    class LoginRequest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject postData = new JSONObject();
            JSONObject user = new JSONObject();
            try {
                user.put("email", params[0]);
                user.put("password", params[1]);
                postData.put("user", user);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
            return Utils.httpPost(Utils.API_BASE_URL + "/login", postData.toString());
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progress.setVisibility(View.GONE);
            JSONObject userJson = new JSONObject();
            try {
                userJson = new JSONObject(response);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
            User user = new User(userJson);
            if (user.isLoggedIn()) {
                user.save(getActivity());
                TrainTrack app = (TrainTrack) getActivity().getApplication();
                app.setUser(user);
                updateButtonVisibility(true);
                Toast.makeText(getActivity(), "Hello " + user.getUsername(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Could not login sorry!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateButtonVisibility(boolean loggedIn) {
        if (loggedIn) {
            signup.setVisibility(View.GONE);
            login.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        } else {
            signup.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
    }

}
