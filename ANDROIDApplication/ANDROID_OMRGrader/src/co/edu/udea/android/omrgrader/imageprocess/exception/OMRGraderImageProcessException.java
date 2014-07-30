package co.edu.udea.android.omrgrader.imageprocess.exception;

import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderImageProcessException extends Exception {

	private static final String TAG = OMRGraderImageProcessException.class
			.getSimpleName();

	private static final long serialVersionUID = 8830405776860663856L;

	public OMRGraderImageProcessException() {
		super();

		Log.e(TAG, this.getMessage(), this.getCause());
	}

	public OMRGraderImageProcessException(String detailMessage) {
		super(detailMessage);

		Log.e(TAG, detailMessage, this.getCause());
	}

	public OMRGraderImageProcessException(Throwable throwable) {
		super(throwable);

		Log.e(TAG, throwable.getMessage(), throwable);
	}

	public OMRGraderImageProcessException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);

		Log.e(TAG, detailMessage, throwable);
	}
}