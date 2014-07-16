package co.edu.udea.android.omrgrader.activity.config.preference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import co.edu.udea.android.omrgrader.R;

public class GraderSettingsPreferenceFragment extends PreferenceFragment
		implements OnSharedPreferenceChangeListener {

	private SharedPreferences sharedPreferences;

	@Override()
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.addPreferencesFromResource(R.xml.grader_settings_preference);
	}

	@Override()
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	@Override()
	public void onStart() {
		super.onStart();

		this.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getActivity()
						.getApplicationContext());
		this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}
}