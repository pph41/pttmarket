package com.pttmarket.potatomarket;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DialogReport extends Dialog {

    private CheckBox checkBox1, checkBox2; // Add more checkboxes if needed
    private Button submitButton;

    public DialogReport(Context context) {
        super(context);
        setContentView(R.layout.dialog_report);

        setTitle("신고하기");

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        // Initialize more checkboxes if needed

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle submit button click
                dismiss(); // Close the dialog after submission
            }
        });
    }
}
