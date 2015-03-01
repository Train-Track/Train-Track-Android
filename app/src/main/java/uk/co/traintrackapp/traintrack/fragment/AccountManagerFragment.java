package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.User;


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
        final EditText password = (EditText) v.findViewById(R.id.password);
        Button update = (Button) v.findViewById(R.id.update);
        Button logout = (Button) v.findViewById(R.id.logout);
        Button login = (Button) v.findViewById(R.id.login);
        Button signup = (Button) v.findViewById(R.id.signup);

        if (user.getId() == 0) {
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
                //TODO: Send to server
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
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

}
