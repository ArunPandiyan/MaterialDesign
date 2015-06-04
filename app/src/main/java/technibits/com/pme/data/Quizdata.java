package technibits.com.pme.data;

import java.io.Serializable;

public class Quizdata implements Serializable {

    private String question;
    private int answer;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String description;
    private String difficulty_level;
    private String process_name;
    private String process_group;
    private String knowledge_area;
    private String questionID;
    private String status;
    private String testid;
    private String userid;

    private int wrongAnswer;
    private int isAnswer;
    private int examanswer;
    private int isCecked;


    private int total_question;
    private int attempt_questions;
    private int correct_answers;
    private int marked_review;
    private int percentage;
    private String time_spent;
    private String result;


    public String getUserid() {
        return userid;
    }

    public String getTestid() {
        return testid;
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
    }

    public int getAnswer() {
        return answer;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficultylevel() {
        return difficulty_level;
    }

    public String getProcessname() {
        return process_name;
    }

    public String getProcessgroup() {
        return process_group;
    }

    public String getKnowledgeArea() {
        return knowledge_area;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public int getIsAnswer() {
        return isAnswer;
    }

    public int getExamAnswer() {
        return examanswer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public int getISchecked() {
        return isCecked;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficultylevel(String difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public void setKnowledgeArea(String knowledgeArea) {
        this.knowledge_area = knowledgeArea;
    }

    public void setProcessGroup(String processGroup) {
        this.process_group = processGroup;
    }

    public void setProcessName(String processName) {
        this.process_name = processName;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public void setExamAnswer(int examanswer) {
        this.examanswer = examanswer;
    }

    public void setIsAnswer(int isAnswer) {
        this.isAnswer = isAnswer;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public void setISchecked(int ischecked) {
        this.isCecked = ischecked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
