package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import androidx.fragment.app.Fragment;

import uk.co.traintrackapp.traintrack.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
