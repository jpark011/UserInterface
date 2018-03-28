package ca.uwaterloo.cs.hj8park.a4.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by JayP on 2018-03-26.
 */

enum QuizType {
    SINGLE,
    MULTIPLE
}

// behaves like struct
public class Quiz implements Serializable {
    public String question;
    public String[] candidates;
    public Set<Integer> answers;
    public QuizType type;
    public String image;

    public Quiz(String question, String[] candidates, String type, Set<Integer> answers, String image) {
        this.question = question;
        this.candidates = candidates;
        switch (type) {
        case "Single":
            this.type = QuizType.SINGLE;
            break;
        case "Multiple":
            this.type = QuizType.MULTIPLE;
            break;
        }
        this.answers = answers;
        this.image = image;
    }

    public boolean isMulti() {
        return this.type == QuizType.MULTIPLE;
    }

    public boolean isAnswer(int answer) {
        return this.answers.contains(answer);
    }

    public boolean isCorrect(Set<Integer> userAnswers) {
        // user input not match with answers
        for (int ans : userAnswers) {
            if (!this.isAnswer(ans)) {
                return false;
            }
        }

        // all answers should be in user answers
        for (int ans : this.answers) {
            if (!userAnswers.contains(ans)) {
                return false;
            }
        }

        return true;
    }
}
