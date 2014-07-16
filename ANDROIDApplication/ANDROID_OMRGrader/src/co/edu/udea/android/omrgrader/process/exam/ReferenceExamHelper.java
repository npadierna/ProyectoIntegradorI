package co.edu.udea.android.omrgrader.process.exam;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class ReferenceExamHelper {

	private Context context;

	private BaseStorageDirectory baseStorageDirectory;

	public ReferenceExamHelper(Context context) {
		super();

		this.context = context;
		this.baseStorageDirectory = BaseStorageDirectory
				.getInstance(this.context);
	}

	public void createIntentForTakingPicture(String referencePictureName)
			throws OMRGraderProcessException {
		File takenReferencePictureFile = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File directoryFile = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap().get(
						this.context.getResources().getString(
								R.string.album_exams_name));
		try {
			takenReferencePictureFile = File
					.createTempFile(
							referencePictureName,
							this.context.getResources().getString(
									R.string.pictures_prefix), directoryFile);

			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(takenReferencePictureFile));
			takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			this.context.startActivity(takePictureIntent);
		} catch (Exception e) {
			throw new OMRGraderProcessException(
					"A exception has ocurred while the Camera Applications was being requested",
					e);
		}
	}

	public List<ReferenceExamItem> findAllReferenceExamsItems()
			throws OMRGraderProcessException {
		try {
			File referenceExamsDirectoryFile = this.baseStorageDirectory
					.getStorageDirectoriesFilesMap().get(
							this.context.getResources().getString(
									R.string.album_exams_name));
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
}