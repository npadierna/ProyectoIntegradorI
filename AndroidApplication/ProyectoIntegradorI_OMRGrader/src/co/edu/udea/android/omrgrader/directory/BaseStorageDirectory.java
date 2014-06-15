package co.edu.udea.android.omrgrader.directory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class BaseStorageDirectory {

	private static final String TAG = BaseStorageDirectory.class
			.getSimpleName();

	public static final String ONLY_LOGOS_PATH = "Only Logos Template Path";
	private static final String SLASH = "/";

	private Context context;
	private File baseStorageDirectoryFile;
	private Map<String, File> storageDirectoriesFilesMap;

	private static BaseStorageDirectory instance;

	private BaseStorageDirectory(Context context) {
		super();

		this.context = context;
		this.storageDirectoriesFilesMap = new HashMap<String, File>();

		this.createBaseStorageDirectory();
	}

	public static synchronized BaseStorageDirectory getInstance(Context context) {
		if (instance == null) {
			instance = new BaseStorageDirectory(context);
		}

		return (instance);
	}

	public Map<String, File> getStorageDirectoriesFilesMap() {

		return (this.storageDirectoriesFilesMap);
	}

	private void createBaseStorageDirectory() {
		File storageDirectoryFile = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			storageDirectoryFile = this.buildStorageDirectory();

			if (storageDirectoryFile == null) {
				Log.e(TAG, "The storage directory could not be created.");
			} else {
				this.storageDirectoriesFilesMap.put(this.context.getResources()
						.getString(R.string.album_root_name),
						storageDirectoryFile);
				this.baseStorageDirectoryFile = storageDirectoryFile;

				this.createAllNeededDirectories();
			}
		}
	}

	private File buildStorageDirectory() {
		StringBuilder storageDirectoryPath = new StringBuilder(Environment
				.getExternalStorageDirectory().toString());

		storageDirectoryPath.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_root_name));

		return (new File(storageDirectoryPath.toString()));
	}

	private void createAllNeededDirectories() {
		StringBuilder pathStringBuilder = new StringBuilder();

		pathStringBuilder
				.append(this.baseStorageDirectoryFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_references_name));
		File auxiliarPathFile = new File(pathStringBuilder.toString());
		pathStringBuilder.delete(0, pathStringBuilder.length());

		/*
		 * Creating the directory: "/OMRGrader/references/templates"
		 */
		pathStringBuilder
				.append(auxiliarPathFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_templates_name));
		File auxiliarInnerPathFile = new File(pathStringBuilder.toString());
		auxiliarInnerPathFile.mkdirs();
		this.storageDirectoriesFilesMap.put(this.context.getResources()
				.getString(R.string.album_templates_name),
				auxiliarInnerPathFile);
		pathStringBuilder.delete(0, pathStringBuilder.length());

		/*
		 * Creating the directory: "/OMRGrader/references/exams"
		 */
		pathStringBuilder
				.append(auxiliarPathFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_exams_name));
		auxiliarInnerPathFile = new File(pathStringBuilder.toString());
		auxiliarInnerPathFile.mkdirs();
		this.storageDirectoriesFilesMap.put(this.context.getResources()
				.getString(R.string.album_exams_name), auxiliarInnerPathFile);
		pathStringBuilder.delete(0, pathStringBuilder.length());

		/*
		 * Creating the directory: "/OMRGrader/tests"
		 */
		pathStringBuilder
				.append(this.baseStorageDirectoryFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_tests_name));
		auxiliarPathFile = new File(pathStringBuilder.toString());
		auxiliarPathFile.mkdirs();
		this.storageDirectoriesFilesMap.put(this.context.getResources()
				.getString(R.string.album_tests_name), auxiliarPathFile);
		pathStringBuilder.delete(0, pathStringBuilder.length());

		pathStringBuilder
				.append(this.baseStorageDirectoryFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_temp_name));
		auxiliarPathFile = new File(pathStringBuilder.toString());
		pathStringBuilder.delete(0, pathStringBuilder.length());

		/*
		 * Creating the directory: "/OMRGrader/temp/black_white"
		 */
		pathStringBuilder
				.append(auxiliarPathFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_blackwhite_exams_name));
		auxiliarInnerPathFile = new File(pathStringBuilder.toString());
		auxiliarInnerPathFile.mkdirs();
		this.storageDirectoriesFilesMap.put(this.context.getResources()
				.getString(R.string.album_blackwhite_exams_name),
				auxiliarInnerPathFile);
		pathStringBuilder.delete(0, pathStringBuilder.length());

		/*
		 * Creating the directory: "/OMRGrader/temp/processed"
		 */
		pathStringBuilder
				.append(auxiliarPathFile.toString())
				.append(SLASH)
				.append(this.context.getResources().getString(
						R.string.album_processed_exams_name));
		auxiliarInnerPathFile = new File(pathStringBuilder.toString());
		auxiliarInnerPathFile.mkdirs();
		this.storageDirectoriesFilesMap.put(this.context.getResources()
				.getString(R.string.album_processed_exams_name),
				auxiliarInnerPathFile);
		pathStringBuilder.delete(0, pathStringBuilder.length());
	}
}