package np.info.suraj.usingdialogclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            showDialog();
        }
    }

    private void showDialog() {
        // Create the new Dialog.
        Dialog dialog = new Dialog(this);

        // Set the title.
        dialog.setTitle("Dialog Title");

        // Inflate the layout.
        dialog.setContentView(R.layout.dialog_text_view);

        // Update the Dialog’s contents.
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        String text = "This is the text in my dialog";
        textView.setText(text);

        // Display the Dialog.
        dialog.show();
    }
}