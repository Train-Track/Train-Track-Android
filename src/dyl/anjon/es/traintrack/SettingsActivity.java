package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.os.Bundle;
import dyl.anjon.es.traintrack.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}

}
