package ca.uwaterloo.cs.hj8park.a4.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Set;

import ca.uwaterloo.cs.hj8park.a4.R;
import ca.uwaterloo.cs.hj8park.a4.model.Quiz;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    private static final String ARG_QUIZ = "quiz";

    private Quiz mQuiz;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quiz the main param
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(Quiz quiz) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUIZ, quiz);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        View fragView = null;

        // Inflate the layout for this fragment
        if (mQuiz.isMulti()) {
            fragView = inflater.inflate(R.layout.fragment_question_multiple, container,false);
            TextView text = fragView.findViewById(R.id.multipleQuestion);
            ImageView image = fragView.findViewById(R.id.multipleImage);
            ViewGroup group = fragView.findViewById(R.id.multipleCheckboxGroup);

            text.setText(mQuiz.question);
            image.setImageResource(
                    activity.getResources().getIdentifier(mQuiz.image, "drawable", activity.getPackageName()));
            for (int i = 0; i < group.getChildCount(); i++) {
                CheckBox view = (CheckBox) group.getChildAt(i);
                view.setText(mQuiz.candidates[i]);
                view.setOnClickListener(new OnCheckBoxClicked());
            }
        } else {
            fragView = inflater.inflate(R.layout.fragment_question_single, container, false);
            TextView text = fragView.findViewById(R.id.singleQuestion);
            ImageView image = fragView.findViewById(R.id.singleImage);
            ViewGroup group = fragView.findViewById(R.id.singleRadioGroup);

            text.setText(mQuiz.question);
            image.setImageResource(
                    activity.getResources().getIdentifier(mQuiz.image, "drawable", activity.getPackageName()));
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton view = (RadioButton) group.getChildAt(i);
                view.setText(mQuiz.candidates[i]);
                view.setOnClickListener(new OnRadioButtonClicked());
            }
        }

        return fragView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentRadioCheck(int answer);
        void onFragmentCheckBoxCheck(int answer);
        void onFragmentCheckBoxUnCheck(int answer);
    }

    private class OnRadioButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton)v).isChecked();
            if (!checked) return;

            int answer = -1;
            switch(v.getId()) {
                case R.id.option1:
                    answer = 0;
                    break;
                case R.id.option2:
                    answer = 1;
                    break;
                case R.id.option3:
                    answer = 2;
                    break;
                case R.id.option4:
                    answer = 3;
                    break;
            }
            mListener.onFragmentRadioCheck(answer);
        }
    }

    private class OnCheckBoxClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox)v).isChecked();

            int answer = -1;
            switch(v.getId()) {
                case R.id.checkbox1:
                    answer = 0;
                    break;
                case R.id.checkbox2:
                    answer = 1;
                    break;
                case R.id.checkbox3:
                    answer = 2;
                    break;
                case R.id.checkbox4:
                    answer = 3;
                    break;
            }

            if (checked) {
                mListener.onFragmentCheckBoxCheck(answer);
            } else {
                mListener.onFragmentCheckBoxUnCheck(answer);
            }
        }
    }
}
