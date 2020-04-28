package com.example.reminderapp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CustomRepModeFragment extends DialogFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_custom_rep_mode_fragment, container, false);
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            final EditText editText = view.findViewById(R.id.num_days_fc);

            if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("num_days")))
                editText.setText(getArguments().getString("num_days"));

            Button btn = view.findViewById(R.id.submit_button_fc);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Reminder will repeat after " + editText.getText() + " days!", Toast.LENGTH_LONG).show();
                    DialogListener dialogListener = (DialogListener) getActivity();
                    if(dialogListener != null)
                        dialogListener.onFinishEditDialog(editText.getText().toString());
                    dismiss();
                }
            });

        }

        @Override
        public void onResume() {
            super.onResume();

        }



        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        public interface DialogListener {
            void onFinishEditDialog(String inputText);
        }
}
