package co.edu.udea.android.omrgrader.activity.session;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class InputReferenceExamNameDialogFragment extends DialogFragment {

	public static final String DIALOG_FRAGMENT_TITLE = "Title for Dialog Fragment";

	private EditText inputReferenceNameEditText;

	@Override()
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				super.getActivity());
		LayoutInflater layoutInflater = super.getActivity().getLayoutInflater();
		View view = layoutInflater.inflate(
				R.layout.dialog_fragment_input_reference_exam_name, null);

		this.inputReferenceNameEditText = (EditText) view
				.findViewById(R.id.input_reference_exam_name_edit_text);

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setCancelable(false);

		Bundle bundle = super.getArguments();
		if ((bundle != null) && (bundle.containsKey(DIALOG_FRAGMENT_TITLE))) {
			alertDialogBuilder.setTitle(bundle.getInt(DIALOG_FRAGMENT_TITLE));
		}

		alertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						((MainSessionActivity) getActivity())
								.putReferenceExamPictureName(inputReferenceNameEditText
										.getText().toString());
					}
				});

		return (alertDialogBuilder.create());
	}
}