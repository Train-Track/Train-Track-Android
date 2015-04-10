package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import uk.co.traintrackapp.traintrack.R;

public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
