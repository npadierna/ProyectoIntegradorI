package co.edu.udea.android.omrgrader.activity.config.preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

	private AlertDialog.Builder errorAlertDialogBuilder;
	private SharedPreferences sharedPreferences;

	@Override()
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.addPreferencesFromResource(R.xml.grader_settings_preference);

		this.createWidgetsComponents();
	}

	@Override()
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		String value = sharedPreferences.getString(key, "").trim();

		Log.i(TAG, String.format("Shared Preference Changed: [%s, %s]", key,
				value));

		if (key.equals(super.getActivity().getString(
				R.string.grader_settings_preference_maximum_grade_key))) {
		} else if (key.equals(super.getActivity().getString(
				R.string.grader_settings_preference_radius_lenght_key))) {
			try {
				int radiusLength = Integer.valueOf(value);
				int maximumRadiusLenght = super.getActivity().getResources()
						.getInteger(R.integer.maximum_radius_lenght);

				if ((radiusLength < 1) || (radiusLength > maximumRadiusLenght)) {
					this.rollbackSharedPreferenceRadiusLenght(
							sharedPreferences, key);
				}
			} catch (Exception ex) {
				this.rollbackSharedPreferenceRadiusLenght(sharedPreferences,
						key);
			}
		} else if (key.equals(super.getActivity().getString(
				R.string.grader_settings_preference_minimum_thresh_key))) {
			try {
				int minimumThresh = Integer.valueOf(value);
				int maximumThresh = super.getActivity().getResources()
						.getInteger(R.integer.maximum_thresh);

				if ((minimumThresh < 1) || (minimumThresh > maximumThresh)) {
					this.rollbackSharedPreferenceMinimumThresh(
							sharedPreferences, key);
				}
			} catch (Exception ex) {
				this.rollbackSharedPreferenceMinimumThresh(sharedPreferences,
						key);
			}
		}
	}

	@Override()
	public void onStart() {
		super.onStart();

		this.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getActivity()
						.getApplicationContext());
		this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.errorAlertDialogBuilder = new AlertDialog.Builder(
				super.getActivity());
		this.errorAlertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						GraderSettingsPreferenceFragment.this.getActivity()
								.finish();
					}
				});
	}

	private void rollbackSharedPreferenceMinimumThresh(
			SharedPreferences sharedPreferences, String key) {
		Log.v(TAG, "rollbackSharedPreferenceMinimumThresh(String):void");

		SharedPreferences.Editor editor = this.sharedPreferences.edit();
		editor.putString(
				key,
				String.valueOf(super.getActivity().getResources()
						.getInteger(R.integer.question_item_bubble_thresh)));
		editor.commit();

		this.errorAlertDialogBuilder.setMessage(super.getActivity().getString(
				R.string.bad_thresh_value_message_alert_dialog));
		this.errorAlertDialogBuilder.setTitle(super.getActivity().getString(
				R.string.bad_thresh_value_title_alert_dialog));
		(this.errorAlertDialogBuilder.create()).show();
	}

	private void rollbackSharedPreferenceRadiusLenght(
			SharedPreferences sharedPreferences, String key) {
		Log.v(TAG, "rollbackSharedPreferenceRadiusLenght(String):void");

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(
				key,
				String.valueOf(super
						.getActivity()
						.getResources()
						.getInteger(
								R.integer.question_item_bubble_radius_length)));
		editor.commit();

		this.errorAlertDialogBuilder.setMessage(super.getActivity().getString(
				R.string.bad_radius_lenght_value_message_alert_dialog));
		this.errorAlertDialogBuilder.setTitle(super.getActivity().getString(
				R.string.bad_radius_lenght_value_title_alert_dialog));
		(this.errorAlertDialogBuilder.create()).show();
	}
}