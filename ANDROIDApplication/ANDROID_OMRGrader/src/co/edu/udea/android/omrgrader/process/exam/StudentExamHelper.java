package co.edu.udea.android.omrgrader.process.exam;

import java.io.File;

import android.content.Context;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StudentExamHelper extends AbstractExamHelper {

	public StudentExamHelper(Context context) {
		super(context);
	}

	@Override()
	public File obtainDirectoryFileForExams() {

		return (super.getBaseStorageDirectory().getStorageDirectoriesFilesMap()
				.get(super.getContext().getString(R.string.album_tests_name)));
	}
}