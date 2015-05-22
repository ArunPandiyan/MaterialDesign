package androidhive.info.materialdesign.data;

public class ResultData {
	
	private int total_question;
	private int attempt_questions;
	private int correct_answers;
	private int marked_review;
	private int percentage;
	private String time_spent;
	private String result;
	private String quiz_name;
	private int test_ID;

	public int getTotalQuestion() {
		return total_question;
	}
	public int getAttemptQuestions() {
		return attempt_questions;
	}
	
	public int getCorrectAnswers() {
		return correct_answers;
	}
	
	public int getMarkedReview() {
		return marked_review;
	}
	
	public int getPercentage() {
		return percentage;
	}
	
	public String getTimeSpent() {
		return time_spent;
	}
	
	public String getResult() {
		return result;
	}
	
	public String getQuizName() {
		return quiz_name;
	}
	public int getTestID() {
		return test_ID;
	}
	
	public void setTotalQuestion(int total_question) {
		this.total_question = total_question;
	}
	
	public void setAttemptQuestions(int attempt_questions) {
		this.attempt_questions = attempt_questions;
	}
	
	public void setCorrectAnswers(int correct_answers) {
		this.correct_answers = correct_answers;
	}
	
	public void setMarkedReview(int marked_review) {
		this.marked_review = marked_review;
	}
	
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	public void setTimeSpent(String time_spent) {
		this.time_spent = time_spent;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setQuizName(String quiz_name) {
		this.quiz_name = quiz_name;
	}
	
	public void setTestID(int test_ID) {
		this.test_ID = test_ID;
	}

	
	
	
}
