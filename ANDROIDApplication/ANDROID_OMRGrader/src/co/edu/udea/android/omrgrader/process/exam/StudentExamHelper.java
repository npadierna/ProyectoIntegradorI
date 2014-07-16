package co.edu.udea.android.omrgrader.process.exam;

import java.io.File;

import android.content.Context;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

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

	public void createIntentForTakingStudentExamPicture(
			String studentExamPictureName) throws OMRGraderProcessException {
		File destinationDirectoryFile = super
				.getBaseStorageDirectory()
				.getStorageDirectoriesFilesMap()
				.get(super.getContext().getResources()
						.getString(R.string.album_tests_name));

		super.createIntentForTakingPicture(studentExamPictureName,
				destinationDirectoryFile);
	}
}