package co.edu.udea.android.omrgrader.process.exception;

import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderProcessException extends Exception {

	private static final long serialVersionUID = 3833841801550952820L;
	private static final String TAG = OMRGraderProcessException.class
			.getSimpleName();

	public OMRGraderProcessException() {
		super();

		Log.e(TAG, this.getMessage(), this.getCause());
	}

	public OMRGraderProcessException(String detailMessage) {
		super(detailMessage);

		Log.e(TAG, detailMessage, this.getCause());
	}

	public OMRGraderProcessException(Throwable throwable) {
		super(throwable);

		Log.e(TAG, throwable.getMessage(), throwable);
	}

	public OMRGraderProcessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);

		Log.e(TAG, detailMessage, throwable);
	}
}