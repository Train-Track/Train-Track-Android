package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import uk.co.traintrackapp.traintrack.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
