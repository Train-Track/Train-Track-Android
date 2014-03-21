package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.UserRowAdapter;
import dyl.anjon.es.traintrack.models.User;

public class UsersFragment extends Fragment {

	public UsersFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_users, container,
				false);

		ArrayList<User> users = User.getAllFriends(getActivity());

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final UserRowAdapter adapter = new UserRowAdapter(inflater, users);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				User user = (User) adapter.getItem(index);
				Log.i("User HIT", user.getName());
				return;
			}

		});

		EditText search = (EditText) rootView.findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence search, int arg1, int arg2,
					int arg3) {
				adapter.getFilter().filter(search);
			}
		});

		return rootView;
	}
}
