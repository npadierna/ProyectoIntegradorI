package co.edu.udea.android.omrgrader.activity.config.preference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSettingsPreferenceFragment extends PreferenceFragment
		implements OnSharedPreferenceChangeListener {

	private static final String TAG = GraderSettingsPreferenceFragment.class
			.getSimpleName();

	private SharedPreferences sharedPreferences;

	@Override()
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.addPreferencesFromResource(R.xml.grader_settings_preference);
	}

	@Override()
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.i(TAG, String.format("Shared Preference Changed: [%s, %s]", key,
				sharedPreferences.getString(key, "")));
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