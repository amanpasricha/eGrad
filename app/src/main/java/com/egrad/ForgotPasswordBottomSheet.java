package com.egrad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordBottomSheet extends BottomSheetDialogFragment {
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private ForgotPasswordListener listener;

    public interface ForgotPasswordListener {
        void sendPasswordResetLink(String emailAddress);
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior.from( (View) requireView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ForgotPasswordListener) context;
        } catch(ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement Forgot Password Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_forgot_password, container, false);

        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        emailEditText = view.findViewById(R.id.emailEditText);

        final MaterialButton sendPasswordResetLink = view.findViewById(R.id.sendPasswordResetLink);
        sendPasswordResetLink.setOnClickListener(this::sendPasswordResetLink);

        return view;
    }

    private void sendPasswordResetLink(View view) {
        String emailAddress="";

        if(emailEditText.getText() != null)
            emailAddress = emailEditText.getText().toString().trim();

//        TODO: REGEX VALIDATION
        if(emailAddress.isEmpty()) {
            emailInputLayout.setError("Please enter correct email address");
        } else {
            listener.sendPasswordResetLink(emailAddress);
            dismiss();
        }
    }

}
