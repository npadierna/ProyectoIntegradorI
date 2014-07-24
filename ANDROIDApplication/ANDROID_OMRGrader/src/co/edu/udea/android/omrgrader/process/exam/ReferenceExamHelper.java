package co.edu.udea.android.omrgrader.process.exam;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.process.exam.model.ReferenceExamItem;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class ReferenceExamHelper extends AbstractExamHelper {

	public ReferenceExamHelper(Context context) {
		super(context);
	}

	@Override()
	public File obtainDirectoryFileForExams() {

		return (super.getBaseStorageDirectory().getStorageDirectoriesFilesMap()
				.get(super.getContext().getString(R.string.album_exams_name)));
	}

	public File createIntentForTakingReferenceExamPicture(
			String referenceExamPictureName) throws OMRGraderProcessException {
		File destinationDirectoryFile = super
				.getBaseStorageDirectory()
				.getStorageDirectoriesFilesMap()
				.get(super.getContext().getResources()
						.getString(R.string.album_exams_name));

		return (super.createIntentForTakingPicture(referenceExamPictureName,
				destinationDirectoryFile));
	}

	public List<ReferenceExamItem> findAllReferenceExamsItems()
			throws OMRGraderProcessException {
		try {
			File referenceExamsDirectoryFile = super
					.getBaseStorageDirectory()
					.getStorageDirectoriesFilesMap()
					.get(super.getContext().getResources()
							.getString(R.string.album_exams_name));
			List<ReferenceExamItem> fileListItemList = new ArrayList<ReferenceExamItem>();

			for (File file : referenceExamsDirectoryFile.listFiles()) {
				fileListItemList.add(new ReferenceExamItem(new Date(file
						.lastModified()), file.getName()));
			}

			return (fileListItemList);
		} catch (Exception e) {
			throw new OMRGraderProcessException(
					"A exception has ocurred while the application was recovering the References Exams",
					e);
		}
	}

	public String obtainAbsolutePathForReferenceExam(
			String referenceExamFullName) throws OMRGraderProcessException {
		if ((TextUtils.isEmpty(referenceExamFullName))
				|| (TextUtils.isEmpty(referenceExamFullName.trim()))) {
			throw new OMRGraderProcessException(
					"A exception has ocurred while the application was recovering the References Exams");
		}

		return (super
				.getBaseStorageDirectory()
				.getStorageDirectoriesFilesMap()
				.get(super.getContext().getResources()
						.getString(R.string.album_exams_name))
				.getAbsolutePath()
				+ BaseStorageDirectory.SLASH + referenceExamFullName);
	}
}