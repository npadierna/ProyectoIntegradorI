package co.edu.udea.android.omrgrader.activity.session;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.about.AboutUsActivity;
import co.edu.udea.android.omrgrader.activity.config.preference.GraderSettingsPreferenceActivity;
import co.edu.udea.android.omrgrader.activity.exam.reference.ReferenceExamListActivity;
import co.edu.udea.android.omrgrader.process.exam.ReferenceExamHelper;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionActivity extends FragmentActivity {

	private static final String TAG = MainSessionActivity.class.getSimpleName();

	private ReferenceExamHelper referenceExamHelper;

	private AlertDialog.Builder errorAlertDialogBuilder;
	private DialogFragment inputReferenceExamNameDialog;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.fragment_activity_main_session);

		this.createComponents();
		this.createWidgetsComponents();
	}

	@Override()
	public boolean onCreateOptionsMenu(Menu menu) {
		super.getMenuInflater().inflate(R.menu.menu_main_session, menu);

		return (true);
	}

	@Override()
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_us_main_session_menu_item:
			super.startActivity(new Intent(super.getApplicationContext(),
					AboutUsActivity.class));

			return (true);

		case R.id.grader_settings_main_session_menu_item:
			super.startActivity(new Intent(super.getApplicationContext(),
					GraderSettingsPreferenceActivity.class));

			return (true);
		}

		return (false);
	}

	private void createComponents() {
		Log.v(TAG, "createComponents():void");

		this.referenceExamHelper = new ReferenceExamHelper(
				super.getApplicationContext());
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		Bundle bundle = new Bundle();
		bundle.putInt(
				InputReferenceExamNameDialogFragment.DIALOG_FRAGMENT_TITLE,
				R.string.main_title_dialog_fragment);

		this.inputReferenceExamNameDialog = new InputReferenceExamNameDialogFragment();
		this.inputReferenceExamNameDialog.setArguments(bundle);

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder
				.setMessage(R.string.no_reference_exam_taken_message_alert_dialog);
		this.errorAlertDialogBuilder
				.setTitle(R.string.no_reference_exam_taken_title_alert_dialog);
		this.errorAlertDialogBuilder.setPositiveButton(R.string.accept_button, null);
	}

	public void putReferenceExamPictureName(String referencePictureName) {
		Log.v(TAG, "putReferencePictureName(String):void");

		this.inputReferenceExamNameDialog.dismiss();

		if ((TextUtils.isEmpty(referencePictureName))
				|| (TextUtils.isEmpty(referencePictureName.trim()))) {
			this.inputReferenceExamNameDialog.show(
					super.getSupportFragmentManager(), null);
		} else {
			try {
				this.referenceExamHelper
						.createIntentForTakingReferenceExamPicture(referencePictureName
								.trim());
			} catch (OMRGraderProcessException e) {
				(this.errorAlertDialogBuilder.create()).show();

				return;
			}
		}
	}

	public void onStartGraderSession(View view) {
		Log.v(TAG, "onStartGraderSession(View):void");

		super.startActivity(new Intent(super.getApplicationContext(),
				ReferenceExamListActivity.class));
	}

	public void onTakeReferenceExamTemplate(View view) {
		Log.v(TAG, "onTakeReferenceExamTemplate(View):void");

		this.inputReferenceExamNameDialog.show(
				super.getSupportFragmentManager(), null);
	}
}