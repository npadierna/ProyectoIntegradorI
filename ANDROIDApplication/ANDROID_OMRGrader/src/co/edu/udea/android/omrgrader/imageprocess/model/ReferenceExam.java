package co.edu.udea.android.omrgrader.imageprocess.model;

import java.util.List;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ReferenceExam extends AbstractExam {

	private static final long serialVersionUID = 1820239615586789746L;

	public ReferenceExam(String picturePath,
			List<QuestionItem> questionsItemsList) {
		super(picturePath, questionsItemsList);
	}
}