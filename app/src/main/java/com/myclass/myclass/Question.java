package com.myclass.myclass;

/**
 * Created by anand on 18/01/18.
 */

public class Question {
    String question,option1,option2,option3,option4,correctanswer,type;
    int marks;

    public Question(String question, String option1, String option2, String option3, String option4, String correctanswer, int marks) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctanswer = correctanswer;
        this.marks = marks;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public int getMarks() {
        return marks;
    }
}
