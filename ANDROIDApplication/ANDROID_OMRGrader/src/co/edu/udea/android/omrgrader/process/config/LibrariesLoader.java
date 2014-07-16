package co.edu.udea.android.omrgrader.process.config;

import android.util.Log;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class LibrariesLoader {

	private static final String TAG = LibrariesLoader.class.getSimpleName();

	public static final String[] LIBRARIES_NAMES = new String[] {
			"opencv_java", "nonfree" };

	private LibrariesLoader() {
		super();
	}

	public static final boolean loadLibraries()
			throws OMRGraderProcessException {
		Log.v(TAG, "loadLibraries():void");

		try {
			for (String libraryName : LIBRARIES_NAMES) {
				Log.i(TAG, String.format("Loading library: %s", libraryName));

				System.loadLibrary(libraryName);
			}
		} catch (Exception e) {
			throw new OMRGraderProcessException(
					"Fatal error while the loader was trying to load all required libraries.",
					e);
		}

		return (true);
	}
}