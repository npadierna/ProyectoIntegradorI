package co.edu.udea.android.omrgrader.process.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class GraderSession implements Serializable {

	private static final long serialVersionUID = -5418906090820115625L;

	private List<StudentExam> studentsExamsList;
	private ReferenceExam referenceExam;

	public GraderSession(String referenceExamAsolutePath) {
		this(new ReferenceExam(referenceExamAsolutePath, null), null);
	}

	public GraderSession(ReferenceExam referenceExam,
			List<StudentExam> studentsExamsList) {
		super();

		this.setReferenceExam(referenceExam);
		this.setStudentsExamsList(studentsExamsList);
	}

	public List<StudentExam> getStudentsExamsList() {

		return (this.studentsExamsList);
	}

	public void setStudentsExamsList(List<StudentExam> studentsExamsList) {
		this.studentsExamsList = studentsExamsList;
	}

	public ReferenceExam getReferenceExam() {

		return (this.referenceExam);
	}

	public void setReferenceExam(ReferenceExam referenceExam) {
		this.referenceExam = referenceExam;
	}
}