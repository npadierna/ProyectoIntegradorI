package co.edu.udea.android.omrgrader.process.exam;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
abstract class AbstractExamHelper {

	private BaseStorageDirectory baseStorageDirectory;

	private Context context;

	public AbstractExamHelper(Context context) {
		super();

		this.setContext(context);
		this.setBaseStorageDirectory(BaseStorageDirectory
				.getInstance(this.context));
	}

	public BaseStorageDirectory getBaseStorageDirectory() {

		return (this.baseStorageDirectory);
	}

	public void setBaseStorageDirectory(
			BaseStorageDirectory baseStorageDirectory) {
		this.baseStorageDirectory = baseStorageDirectory;
	}

	public Context getContext() {

		return (this.context);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	protected File createIntentForTakingPicture(String examPictureName,
			File destinationDirectoryFile) throws OMRGraderProcessException {
		if (TextUtils.isEmpty(examPictureName)
				|| (destinationDirectoryFile == null)
				|| (TextUtils.isEmpty(examPictureName.trim()))
				|| (!destinationDirectoryFile.canWrite())) {
			throw new OMRGraderProcessException(
					"A grave error has been thrown caused by the bad parameterization");
		}

		File takenReferencePictureFile = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			takenReferencePictureFile = File.createTempFile(
					examPictureName,
					this.getContext().getResources()
							.getString(R.string.pictures_prefix),
					destinationDirectoryFile);

			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(takenReferencePictureFile));
			takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			this.getContext().startActivity(takePictureIntent);

			return (takenReferencePictureFile);
		} catch (Exception e) {
			takenReferencePictureFile = null;

			throw new OMRGraderProcessException(
					"A exception has ocurred while the Camera Application was being requested for taking the photo",
					e);
		}
	}

	public abstract File obtainDirectoryFileForExams();
}