package co.edu.udea.android.omrgrader.directory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

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
	private static final String SOURCE_ONLY_LOGOS_TEMPLATE_ROUTE = "images/template";

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

	public File copyBaseTemplateToDirectory() throws OMRGraderProcessException {
		AssetManager assetManager = this.context.getAssets();
		File outputFile = null;
		InputStream inputStream = null;
		String[] assetsFilesNames = null;

		try {
			assetsFilesNames = assetManager
					.list(SOURCE_ONLY_LOGOS_TEMPLATE_ROUTE);
			inputStream = assetManager.open(SOURCE_ONLY_LOGOS_TEMPLATE_ROUTE
					+ SLASH + assetsFilesNames[0]);

			File directoryFile = this.getStorageDirectoriesFilesMap().get(
					this.context.getResources().getString(
							R.string.album_templates_name));
			outputFile = new File(directoryFile, assetsFilesNames[0]);

			if (!outputFile.exists()) {
				OutputStream outputStream = new FileOutputStream(outputFile);
				int read = 0;
				byte[] bytes = new byte[2048];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				outputStream.flush();
				outputStream.close();
				inputStream.close();
			}
		} catch (IOException e) {
			throw new OMRGraderProcessException(
					"A grave error has occurred while the Only Logos Template was being copied to Storage Directory.",
					e);
		}

		return (outputFile);
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

				this.createAllBaseDirectories();
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

	private void createAllBaseDirectories() {
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