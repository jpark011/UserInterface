package ca.uwaterloo.cs.hj8park.a4.model;

import java.util.Observable;

/**
 * Created by JayP on 2018-03-26.
 */

public class Model extends Observable {
    private static Model singleton = new Model();
    private String userName;
    private int numQuestions1;

    private Model() {
        userName = null;
        numQuestions1 = 0;
    }

    public static Model getInstance() {
        return singleton;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void reset() {
        Model.singleton = null;
        Model.singleton = new Model();
    }

    public int getNumQuestions() {
        return numQuestions1;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions1 = numQuestions;
    }
}
