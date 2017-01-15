package codepathtodoapp.myandroid.com.todoapp;

/**
 * Created by jsaluja on 1/11/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class EditDialogue extends DialogFragment {
    private EditText EtSubmitText, EtSubmitPriority;
    private Button BtnSave;
    private String ClickedText = "clicked_text_string";
    private String ClickedPriority = "clicked_priority_string";
    private DatePicker datePicker;

    public interface EditDialogueListener {
        void onFinishEditDialog(String inputText, String inputDate, String inputPriority);
    }



    EditDialogueListener EditDialogueListener;

    public EditDialogue() {
    }

    public static EditDialogue newInstance(String title, String Priority) {
        EditDialogue frag = new EditDialogue();
        Bundle args = new Bundle();
        args.putString("clicked_text_string", title );
        args.putString("clicked_priority_string", Priority );
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            EditDialogueListener = (EditDialogueListener)activity;
        }
        catch(Exception ex){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_item, container);
        Calendar c = Calendar.getInstance();

        EtSubmitText = (EditText)view.findViewById(R.id.ScndActEditTextId);
        EtSubmitPriority = (EditText)view.findViewById(R.id.ScndActPriorityEditTextId);
        BtnSave = (Button)view.findViewById(R.id.ScndActButtonId);
        this.datePicker = (DatePicker) view.findViewById(R.id.date_value);
        this.datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

        Bundle bundle = getArguments();
        EtSubmitText.setText(bundle.getString(ClickedText));
        EtSubmitPriority.setText(bundle.getString(ClickedPriority));

        //Place cursor at the end of the displayed value
        EtSubmitText.setSelection(EtSubmitText.getText().length());
        //Make sure the main text is focused
        EtSubmitText.requestFocus();

        BtnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int datePickerMonth = 1 + EditDialogue.this.datePicker.getMonth();
                String date_string = EditDialogue.this.datePicker.getYear() + "/" + datePickerMonth + "/" + EditDialogue.this.datePicker.getDayOfMonth();

                String PriorityInput = EtSubmitPriority.getText().toString();
                if((Objects.equals(PriorityInput, "High")) || (Objects.equals(PriorityInput, "Medium")) || (Objects.equals(PriorityInput, "Low"))) {
                    EditDialogueListener listener = (EditDialogueListener) getActivity();
                    listener.onFinishEditDialog(EtSubmitText.getText().toString(), date_string, EtSubmitPriority.getText().toString());
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Priority should be Low, Medium or High", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

}
