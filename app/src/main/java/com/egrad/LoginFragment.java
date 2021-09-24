package com.egrad;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginFragment extends Fragment {

    private static final String LOGIN_AUTHORITY = "LOGIN_AUTHORITY";
    public static final int STUDENT_LOGIN = 0;
    public static final int FACULTY_LOGIN = 1;

    private final ForgotPasswordBottomSheet forgotPasswordBottomSheet;
    private TextInputLayout emailInputLayout, passInputLayout;
    private TextInputEditText emailEditText, passEditText;
    private int LOGIN_MODE;

    public LoginFragment() {
        // Required empty public constructor
        forgotPasswordBottomSheet = new ForgotPasswordBottomSheet();
    }

    public static LoginFragment newInstance(int LOGIN_MODE) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(LOGIN_AUTHORITY, LOGIN_MODE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            LOGIN_MODE = getArguments().getInt(LOGIN_AUTHORITY, STUDENT_LOGIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passInputLayout = view.findViewById(R.id.passInputLayout);
        emailEditText = view.findViewById(R.id.emailID);
        passEditText = view.findViewById(R.id.password);

        final MaterialButton loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::login);

        final MaterialTextView forgotPassword = view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this::forgotPassword);
    }

    public void forgotPassword(View view) {
        // Open a dialog and ask for email id for a reset password link
        if(!forgotPasswordBottomSheet.isAdded())
            forgotPasswordBottomSheet.show(getParentFragmentManager(), null);
    }

    public void login(View view) {
        String emailID="", password="";
        boolean valid = true;

        if(emailEditText.getText() != null)
            emailID = emailEditText.getText().toString().trim();

        if(passEditText.getText() != null)
            password = passEditText.getText().toString().trim();

//        TODO: REGEX VALIDATION
        // Validate email id and password
        if(emailID.isEmpty())
            emailInputLayout.setError("Please enter correct email address");

        if(password.isEmpty())
            passInputLayout.setError("Please enter correct password");

        if(LOGIN_MODE == STUDENT_LOGIN)
            startActivity(new Intent(getActivity(), StudentActivity.class));
        else
            startActivity(new Intent(getActivity(), FacultyActivity.class));

//        TODO:
        requireActivity().finish();
    }
}