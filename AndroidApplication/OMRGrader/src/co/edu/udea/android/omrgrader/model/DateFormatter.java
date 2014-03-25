package co.edu.udea.android.omrgrader.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	public static final String DATE_FORMAT = "ddMMyyyy_HHmmss";

	private static DateFormatter instance;
	private SimpleDateFormat simpleDateFormat;

	@SuppressLint("SimpleDateFormat")
	private DateFormatter() {
		super();

		this.setSimpleDateFormat(new SimpleDateFormat(DateFormatter.DATE_FORMAT));
	}

	public SimpleDateFormat getSimpleDateFormat() {

		return (this.simpleDateFormat);
	}

	private void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
		this.simpleDateFormat = simpleDateFormat;
	}

	public static synchronized DateFormatter getInstance() {
		if (instance == null) {
			instance = new DateFormatter();
		}

		return (instance);
	}

	public String formatDate(Date date) {

		return (this.getSimpleDateFormat().format(date));
	}
}