package com.myclass.myclass;

/**
 * Created by priya on 16/1/18.
 */

import java.io.Serializable;

/**
 * Created by priya on 15/1/18.
 */

public class QuizQuestionData implements Serializable {

    private String question;
    private String correctanswer;
    //private String type;
    private String option1,option2,option3,option4;
    private Long marks;
    public QuizQuestionData(){
        //needed for firebase
    }
    public QuizQuestionData(String a,String b,String a1,String a2,String a3,String a4){
        question=a;
        correctanswer=b;
        option1=a1;
        option2=a2;
        option3=a3;
        option4=a4;
        // type=t;
    }
    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String que)
    {
        question = que;
    }
    public String getCorrectanswer()
    {
        return correctanswer;
    }

    public void setCorrectanswer(String que)
    {
        correctanswer = que;
    }

    public void setOption1(String que)
    {
        option1 = que;
    }
    public String getOption4()
    {
        return option4;
    }
    public String getOption1() {return option1;}
    public void setOption4(String que)
    {
        option4 = que;
    }public String getOption2()
    {
        return option2;
    }

    public void setOption2(String que)
    {
        option2 = que;
    }public String getOption3()
    {
        return option3;
    }

    public void setOption3(String que)
    {
        option3 = que;
    }

    public long getMarks()
    {
        return marks;
    }
    public void setMarks(long que)
    {
        marks=que;
    }
}
