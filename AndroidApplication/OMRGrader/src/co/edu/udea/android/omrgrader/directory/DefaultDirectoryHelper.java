package co.edu.udea.android.omrgrader.directory;

import java.io.File;

import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class DefaultDirectoryHelper {

	private static final String TAG = DefaultDirectoryHelper.class
			.getSimpleName();

	private static final String SLASH = "/";
	public static final String CAMERA_DIRECTORY = "/DCIM";

	public String pictureAlbumName;
	public String sessionName;

	public DefaultDirectoryHelper(String pictureAlbumName, String sessionName) {
		this.setPictureAlbumName(pictureAlbumName);
		this.setSessionName(sessionName);
	}

	public String getPictureAlbumName() {

		return (this.pictureAlbumName);
	}

	private void setPictureAlbumName(String pictureAlbumName) {
		this.pictureAlbumName = pictureAlbumName;
	}

	public String getSessionName() {

		return (this.sessionName);
	}

	private void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public File getDefaultAlbumDirectory() {
		Log.i(DefaultDirectoryHelper.TAG, "getDefaultAlbumDirectory() method.");

		File picturesStorageDirectory = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			picturesStorageDirectory = this.buildAlbumDirectoryFile();

			Log.d(DefaultDirectoryHelper.TAG, "Album Storage Directory: "
					+ picturesStorageDirectory.toString());

			if ((picturesStorageDirectory != null)
					&& (!picturesStorageDirectory.mkdirs())
					&& (!picturesStorageDirectory.exists())) {

				return (null);
			} else {
				Log.d(TAG, "The Pictures' Album Creation was successfull.");
			}
		}

		return (picturesStorageDirectory);
	}

	private File buildAlbumDirectoryFile() {
		StringBuilder albumDirectoryPath = new StringBuilder(Environment
				.getExternalStorageDirectory().toString());

		albumDirectoryPath.append(DefaultDirectoryHelper.CAMERA_DIRECTORY)
				.append(DefaultDirectoryHelper.SLASH)
				.append(this.getPictureAlbumName())
				.append(DefaultDirectoryHelper.SLASH)
				.append(this.getSessionName());

		return (new File(albumDirectoryPath.toString()));
	}
}