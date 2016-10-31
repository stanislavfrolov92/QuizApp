package com.example.stanislavfrolov.quizapp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SurveyActivity extends Activity implements View.OnClickListener {

    UserDatabaseHelper userDatabaseHelper;
    QuestionDatabaseHelper questionDatabaseHelper;
    List<Question> allQuestions;
    Question question;
    int questionID = 0;
    TextView questionText;
    RadioButton radioButtonA, radioButtonB, radioButtonC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        userDatabaseHelper = new UserDatabaseHelper(this);
        questionDatabaseHelper = new QuestionDatabaseHelper(this);

        allQuestions = questionDatabaseHelper.getAllQuestions();
        question = allQuestions.get(questionID);

        questionText = (TextView) findViewById(R.id.textQuestion);
        radioButtonA = (RadioButton) findViewById(R.id.optionA);
        radioButtonB = (RadioButton) findViewById(R.id.optionB);
        radioButtonC = (RadioButton) findViewById(R.id.optionC);

        Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        setQuestionView();
    }

    @Override
    public void onClick(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.optionsGroup);
        RadioButton answer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        Calendar calendar = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
        userDatabaseHelper.addAnswer(question.getQuestion(), answer.getText().toString(), timestamp.toString());

        questionID++;
        if (questionID < allQuestions.size()) {
            question = allQuestions.get(questionID);
            setQuestionView();
        } else {
            Intent intent = new Intent(this, ThankYouActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private void setQuestionView() {
        questionText.setText(question.getQuestion());
        radioButtonA.setText(question.getOptionA());
        radioButtonB.setText(question.getOptionB());
        radioButtonC.setText(question.getOptionC());
    }
}
