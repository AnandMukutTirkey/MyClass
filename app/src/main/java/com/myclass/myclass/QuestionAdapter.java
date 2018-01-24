package com.myclass.myclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by priya on 15/1/18.
 */

public class QuestionAdapter extends ArrayAdapter<QuizQuestionData> {
    public static ArrayList<String> selectedQuizAnswer;

    ArrayList<QuizQuestionData> questionList = new ArrayList<>();
    public QuestionAdapter(Context context, int textViewResourceId, ArrayList<QuizQuestionData> objects) {
        super(context,textViewResourceId, objects);
        questionList = objects;
        selectedQuizAnswer =new ArrayList<>();

        for(int i=0;i<questionList.size();i++)
        {
            selectedQuizAnswer.add("Not selected");
        }
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    /*@Override
    public Object getItem(int i){
        return null;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RadioButton b1,b2,b3,b4;
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.question, null);
        TextView textView = (TextView) v.findViewById(R.id.question);
        b1 = (RadioButton)v.findViewById(R.id.option1);
        b2 = (RadioButton)v.findViewById(R.id.option2);
        b3 = (RadioButton)v.findViewById(R.id.option3);
        b4 = (RadioButton)v.findViewById(R.id.option4);
        //RadioGroup bg=(RadioGroup)v.findViewById(R.id.selectedOption);
        //Button submitButton = (Button) v.findViewById(R.id.submitQuiz);

        textView.setText(questionList.get(position).getQuestion());
        b1.setText(questionList.get(position).getOption1());
        b2.setText(questionList.get(position).getOption2());
        b3.setText(questionList.get(position).getOption3());
        b4.setText(questionList.get(position).getOption4());

        b1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedQuizAnswer.set(position,questionList.get(position).getOption1());
            }
        });

        b2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedQuizAnswer.set(position, questionList.get(position).getOption2());
            }
        });

        b3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedQuizAnswer.set(position,questionList.get(position).getOption3());
            }
        });

        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedQuizAnswer.set(position, questionList.get(position).getOption4());
            }
        });
        return v;
    }
}
