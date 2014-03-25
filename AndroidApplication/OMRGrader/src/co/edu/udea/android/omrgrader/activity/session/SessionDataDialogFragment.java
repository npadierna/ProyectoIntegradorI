package co.edu.udea.android.omrgrader.activity.session;

import co.edu.udea.android.omrgrader.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SessionDataDialogFragment extends DialogFragment {

	private static final String TITLE_FOR_DIALOG = "Title for DialogFragment";

	private EditText studentsTestsEditText;
	private EditText sessionNameEditText;

	public static SessionDataDialogFragment newInstance(int title) {
		SessionDataDialogFragment sessionDataDialogFragment = new SessionDataDialogFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(SessionDataDialogFragment.TITLE_FOR_DIALOG, title);

		sessionDataDialogFragment.setArguments(bundle);

		return (sessionDataDialogFragment);
	}

	@Override()
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				super.getActivity());
		LayoutInflater layoutInflater = super.getActivity().getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.dialog_session_data, null);

		this.sessionNameEditText = (EditText) view
				.findViewById(R.id.session_name_edit_text);
		this.studentsTestsEditText = (EditText) view
				.findViewById(R.id.students_test_edit_text);

		alertDialog.setView(view);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(super.getArguments().getInt(
				SessionDataDialogFragment.TITLE_FOR_DIALOG));
		alertDialog.setPositiveButton(
				super.getActivity().getString(R.string.accept_text),
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						((GradeSessionActivity) SessionDataDialogFragment.super
								.getActivity())
								.onCaptureSessionData(
										SessionDataDialogFragment.this.sessionNameEditText
												.getText().toString(),
										SessionDataDialogFragment.this.studentsTestsEditText
												.getText().toString());
					}
				});

		return (alertDialog.create());
	}
}