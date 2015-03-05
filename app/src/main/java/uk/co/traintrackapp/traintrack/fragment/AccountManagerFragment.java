package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;


public class AccountManagerFragment extends Fragment {

    public static AccountManagerFragment newInstance() {
        return new AccountManagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_manager, container, false);
        final TrainTrack app = (TrainTrack) getActivity().getApplication();
        final User user = app.getUser();

        final EditText username = (EditText) v.findViewById(R.id.username);
        final EditText email = (EditText) v.findViewById(R.id.email);
        //final EditText password = (EditText) v.findViewById(R.id.password);
        Button update = (Button) v.findViewById(R.id.update);
        Button logout = (Button) v.findViewById(R.id.logout);
        Button login = (Button) v.findViewById(R.id.login);
        Button signup = (Button) v.findViewById(R.id.signup);

        if (user.getId() == 0) {
            signup.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
        } else {
            logout.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
        }
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
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                new LoginRequest().execute(user.toJson().toString());
                //TODO start a progress spinner
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Set user to nobody and clear the app cache
            }
        });

        return v;
    }

    class LoginRequest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return Utils.httpPost(Utils.API_BASE_URL + "/login", params[0]);
        }

        @Override
        protected void onPostExecute(String returnCode) {
            super.onPostExecute(returnCode);
            Utils.log("The return code is: " + returnCode);
        }
    }

}
