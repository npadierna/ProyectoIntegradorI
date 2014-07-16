package co.edu.udea.android.omrgrader.activity.config.preference;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GraderSettingsPreferenceActivity extends Activity {

	private static final String GRADER_SETTINGS_PREFERENCE_FRAGMENT = "Preference Fragment for Grader Settings";

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			PreferenceFragment graderSettingsPreferenceFragment = new GraderSettingsPreferenceFragment();

			super.getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content,
							graderSettingsPreferenceFragment,
							GRADER_SETTINGS_PREFERENCE_FRAGMENT).commit();
		}
	}
}