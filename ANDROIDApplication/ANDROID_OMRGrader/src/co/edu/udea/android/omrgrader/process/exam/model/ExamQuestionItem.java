package co.edu.udea.android.omrgrader.process.exam.model;

public final class ExamQuestionItem {

	private boolean correctAnswer;
	private int[] questionsItemsBackgroundColors;
	private short id;

	public ExamQuestionItem(short id, boolean correctAnswer,
			int[] questionsItemsBackgroundColors) {
		this.setId(id);
		this.setCorrectAnswer(correctAnswer);
		this.setQuestionsItemsBackgroundColors(questionsItemsBackgroundColors);
	}

	public boolean isCorrectAnswer() {

		return (this.correctAnswer);
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public int[] getQuestionsItemsBackgroundColors() {

		return (this.questionsItemsBackgroundColors);
	}

	public void setQuestionsItemsBackgroundColors(
			int[] questionsItemsBackgroundColors) {
		this.questionsItemsBackgroundColors = questionsItemsBackgroundColors;
	}

	public short getId() {

		return (this.id);
	}

	public void setId(short id) {
		this.id = id;
	}
}