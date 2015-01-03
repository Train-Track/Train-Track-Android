package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Badge;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.UserBadge;
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
        final ParseUser user = ParseUser.getCurrentUser();

        final EditText username = (EditText) v.findViewById(R.id.username);
        final EditText email = (EditText) v.findViewById(R.id.email);
        final EditText password = (EditText) v.findViewById(R.id.password);
        Button update = (Button) v.findViewById(R.id.update);
        Button logout = (Button) v.findViewById(R.id.logout);
        Button login = (Button) v.findViewById(R.id.login);
        Button signup = (Button) v.findViewById(R.id.signup);

        if (ParseAnonymousUtils.isLinked(user)) {
            signup.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
        } else {
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            logout.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Some error checking is required
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "Hello!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Utils.log("User Sign Up: " + e.getMessage());
                        }
                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseObject.unpinAllInBackground("Journeys");
                ParseObject.unpinAllInBackground("JourneyLegs");
                app.setJourneys(new ArrayList<Journey>());
                //TODO: Close and reopen the app?
            }
        });

        ParseQuery<UserBadge> query = ParseQuery.getQuery(UserBadge.class);
        query.orderByDescending("createdAt");
        query.include("badge");
        query.include("badge.image");
        query.findInBackground(new FindCallback<UserBadge>() {
            public void done(List<UserBadge> badgeList, ParseException e) {
                for (UserBadge userBadge : badgeList) {
                    Badge badge = userBadge.getBadge();
                    Utils.log("I have found the badge: " + badge.toString());
                }
            }
        });

        return v;
    }

}
