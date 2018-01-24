package com.myclass.myclass;

import java.io.Serializable;

/**
 * Created by priya on 20/1/18.
 */

public class AttemptedQuizStructure implements Serializable{

    private String quiz_id,scored,total,sDate;

    public AttemptedQuizStructure(String quiz_id, String scored, String total, String sDate) {
        this.quiz_id = quiz_id;
        this.scored = scored;
        this.total = total;
        this.sDate = sDate;
    }


    public String getQuiz_id() {
        return quiz_id;
    }

    public String getTotal() {
        return total;
    }

    public String getsDate() {
        return sDate;
    }

    public String getScored() {
        return scored;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public void setScored(String scored) {
        this.scored = scored;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
}
