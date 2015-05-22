package androidhive.info.materialdesign.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidhive.info.materialdesign.data.Quizdata;
import androidhive.info.materialdesign.data.ResultData;

public class QuizJSONParser {
	public String QUESTIONID = "qid";
    public String QUESTION = "question";
    public String ANSWER = "Answer";
    public String OPTIONA = "optionA";
    public String OPTIONB = "optionB";
    public String OPTIONC = "optionC";
    public String OPTIOND = "optionD";
    public String DESCRIPTION = "description";
    
    public String DIFFCULTY = "difficultylevel";
    public String KNOWLEDGEAREA = "knowledgearea";
    public String PROCESSGROUP = "processgroup";
    public String PROCESSNAME = "processname";
    public String TESTID = "processname";
    
    public String jsonArrayName;
    
	public ArrayList<Quizdata> testJsonParsing(JSONObject json) throws JSONException{
		System.out.println("" + json);
		
		JSONArray quizArray = json.getJSONArray(jsonArrayName);
//		 Quizdata dataSource = new Quizdata();
		 ArrayList<Quizdata> list = new ArrayList<Quizdata>();
        // looping through All Contacts
        for (int i = 0; i < quizArray.length(); i++) {
        	
        	Quizdata dataSource = new Quizdata();
            JSONObject c = quizArray.getJSONObject(i);
            
            dataSource.setQuestionID(c.getString(QUESTIONID));
            dataSource.setQuestion(c.getString(QUESTION));
            dataSource.setAnswer(c.getInt(ANSWER));
            dataSource.setOptionA(c.getString(OPTIONA));
            dataSource.setOptionB(c.getString(OPTIONB));
            dataSource.setOptionC(c.getString(OPTIONC));
            dataSource.setOptionD(c.getString(OPTIOND));
            dataSource.setDescription(c.getString(DESCRIPTION));
            
            dataSource.setDifficultylevel(c.getString(DIFFCULTY));
            dataSource.setKnowledgeArea(c.getString(KNOWLEDGEAREA));
            dataSource.setProcessName(c.getString(PROCESSNAME));
            dataSource.setProcessGroup(c.getString(PROCESSGROUP));
       
            list.add(dataSource);
	}
		return list;  
	}
	
	public ArrayList<Quizdata> reviewJsonParsing(JSONObject json) throws JSONException{
		System.out.println("" + json);
		
		JSONArray quizArray = json.getJSONArray("markforviewlist");
//		 Quizdata dataSource = new Quizdata();
		 ArrayList<Quizdata> list = new ArrayList<Quizdata>();
        // looping through All Contacts
        for (int i = 0; i < quizArray.length(); i++) {
        	
        	Quizdata dataSource = new Quizdata();
            JSONObject c = quizArray.getJSONObject(i);
            
            dataSource.setQuestionID(c.getString(QUESTIONID));
            dataSource.setQuestion(c.getString(QUESTION));
            dataSource.setAnswer(c.getInt(ANSWER));
            dataSource.setOptionA(c.getString(OPTIONA));
            dataSource.setOptionB(c.getString(OPTIONB));
            dataSource.setOptionC(c.getString(OPTIONC));
            dataSource.setOptionD(c.getString(OPTIOND));
            dataSource.setDescription(c.getString(DESCRIPTION));
            
//            dataSource.setDifficultylevel(c.getString(DIFFCULTY));
//            dataSource.setKnowledgeArea(c.getString(KNOWLEDGEAREA));
//            dataSource.setProcessName(c.getString(PROCESSNAME));
//            dataSource.setProcessGroup(c.getString(PROCESSGROUP));
       
            list.add(dataSource);
	}
		return list;  
	}
	
	public ArrayList<ResultData> performJsonParsing(JSONObject json) throws JSONException{
		System.out.println("" + json);
		
		JSONArray quizArray = json.getJSONArray("Performance");
//		 Quizdata dataSource = new Quizdata();
		 ArrayList<ResultData> list = new ArrayList<ResultData>();
        // looping through All Contacts
        for (int i = 0; i < quizArray.length(); i++) {
        	
        	ResultData dataSource = new ResultData();
            JSONObject c = quizArray.getJSONObject(i);
            
            dataSource.setTestID(Integer.valueOf(c.getString("sid")));
            dataSource.setQuizName(c.getString("Quizname"));
            dataSource.setTotalQuestion(c.getInt("Total-questions"));
            dataSource.setAttemptQuestions(c.getInt("Attempt-questions"));
            dataSource.setCorrectAnswers(c.getInt("Correct-answers"));
            dataSource.setMarkedReview(c.getInt("Marked-For-review"));
            dataSource.setPercentage(Integer.valueOf(c.getString("Percentage")));
            dataSource.setResult(c.getString("Result"));
            dataSource.setTimeSpent(c.getString("Timespent"));


            list.add(dataSource);
	}
		return list;  
	}
	
	
	public ArrayList<Quizdata> performReviewJsonParsing(JSONObject json) throws JSONException{
		System.out.println("" + json);

		
		JSONArray quizArray = json.getJSONArray("Review");
		 ArrayList<Quizdata> list = new ArrayList<Quizdata>();
        // looping through All Contacts
        for (int i = 0; i < quizArray.length(); i++) {
        	
        	Quizdata dataSource = new Quizdata();
            JSONObject c = quizArray.getJSONObject(i);
            
            dataSource.setQuestionID(c.getString(QUESTIONID));
            dataSource.setQuestion(c.getString(QUESTION));
            dataSource.setAnswer(c.getInt(ANSWER));
            dataSource.setOptionA(c.getString(OPTIONA));
            dataSource.setOptionB(c.getString(OPTIONB));
            dataSource.setOptionC(c.getString(OPTIONC));
            dataSource.setOptionD(c.getString(OPTIOND));
            dataSource.setDescription(c.getString(DESCRIPTION));
            int answer = Integer.valueOf(c.getString("options-selected"));
            if (answer != 0) {
            	dataSource.setIsAnswer(1);
            	dataSource.setWrongAnswer(answer);
			}else{
				dataSource.setIsAnswer(0);
				
			}
            
            
//            dataSource.setDifficultylevel(c.getString(DIFFCULTY));
//            dataSource.setKnowledgeArea(c.getString(KNOWLEDGEAREA));
//            dataSource.setProcessName(c.getString(PROCESSNAME));
//            dataSource.setProcessGroup(c.getString(PROCESSGROUP));
       
            list.add(dataSource);
	}
		return list;  
	}

}
