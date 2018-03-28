package ca.uwaterloo.cs.hj8park.a4.model;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import ca.uwaterloo.cs.hj8park.a4.R;

/**
 * Created by JayP on 2018-03-26.
 */

public class Model extends Observable {
    private static Model singleton = new Model();
    private  static ArrayList<Quiz> masterQuizzes = new ArrayList<>();
    private String userName;
    private int numQuestions1;
    private int curNumQuestion;     // directly related to UI shift
    private int score;
    private HashMap<Quiz, Set<Integer>> userAnswers;

    private Model() {
        userName = null;
        numQuestions1 = 0;
        curNumQuestion = 0;
        score = 0;
        userAnswers = new HashMap<>();
    }

    public static Model getInstance() {
        return singleton;
    }

    public Quiz getQuiz(int i) {
        return masterQuizzes.get(i);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Model reset() {
        Model.singleton = null;
        Model.singleton = new Model();
        return getInstance();
    }

    public int getNumQuestions() {
        return numQuestions1;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions1 = numQuestions;
    }

    public void addAnswer(int answer) {
        Quiz quiz = masterQuizzes.get(curNumQuestion);
        userAnswers.get(quiz).add(answer);
    }

    public void removeAnswer(int answer) {
        Quiz quiz = masterQuizzes.get(curNumQuestion);
        userAnswers.get(quiz).remove(answer);
    }

    public void updateAnswer(int answer) {
        Quiz quiz = masterQuizzes.get(curNumQuestion);
        userAnswers.get(quiz).clear();
        userAnswers.get(quiz).add(answer);
    }

    public void loadQuiz() {
        for (int i=0; i < numQuestions1; i++) {
            Quiz quiz = masterQuizzes.get(i);
            userAnswers.put(quiz, new HashSet<Integer>());
        }
    }

    public void setUpQuizzes(Resources res, InputStream is) {
        if (masterQuizzes.isEmpty()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split(",");

                    // Question
                    String question = words[2];

                    // Candidate answers
                    String[] candidates = new String[4];
                    for (int i = 3; i < 7; i++) {
                        candidates[i-3] = words[i];
                    }

                    // Answers
                    Set<Integer> answers = new HashSet<>();
                    char[] alphaAns = words[7].toCharArray();
                    for (char c : alphaAns) {
                        answers.add(c - 65);
                    }

                    // Answer type
                    String type = words[1];

                    // Image file
                    String image = res.getString(R.string.image_file) + words[0];

                    // add quiz
                    masterQuizzes.add(new Quiz(question, candidates, type, answers, image));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void calcScore() {
        // to prevent cheating!!
        this.score = 0;
        for (Quiz quiz : userAnswers.keySet()) {
            Set<Integer> userAnswer = userAnswers.get(quiz);
            if (quiz.isCorrect(userAnswer)) {
                this.score++;
            }
        }
    }

    public int getCurNumQuestion() {
        return curNumQuestion;
    }

    public void nextPage() {
        setCurNumQuestion(curNumQuestion+1);
    }

    public void prevPage() {
        setCurNumQuestion(curNumQuestion-1);
    }

    public void setCurNumQuestion(int curNumQuestion) {
        this.curNumQuestion = curNumQuestion;
        setChanged();
        notifyObservers();
    }

    public int getScore() {
        return score;
    }
}
