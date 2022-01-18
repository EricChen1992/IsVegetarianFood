package thisis.vegetarian.question.mark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.MessageFormat;

import thisis.vegetarian.question.mark.databinding.ActivityIvfFeedbackBinding;

public class IVFFeedBackActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "thisis.vegetarian.question.mark.feedback.EXTRA_NAME";
    public static final String EXTRA_ID = "thisis.vegetarian.question.mark.feedback.EXTRA_ID";
    private final String feedBakEmail = "azxt1468@gmail.com";
    private final String feedBackTittle = "IsVegetarian Feedback - {0}";
    private final String feedBackBody = "Create name: {0}\nCreate id: {1}\nContent:\n{2}";
    private ActivityIvfFeedbackBinding activityIvfFeedbackBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfFeedbackBinding = DataBindingUtil.setContentView(IVFFeedBackActivity.this, R.layout.activity_ivf_feedback);
        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_NAME);
        String userId = intent.getStringExtra(EXTRA_ID);
        Button button = activityIvfFeedbackBinding.ivfFeedbackButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(getFeedBackTittle(activityIvfFeedbackBinding.ivfFeedbackTittle.getText().toString()),
                        getFeedBackBody(userName, userId));
            }
        });
        //set actionbar close button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ivf_close);
    }

    private String getFeedBackTittle(String tittle){
        if ("".equals(tittle)) return "";
        return MessageFormat.format(feedBackTittle, tittle);
    }

    private String getFeedBackBody(String name, String id){
        String content = activityIvfFeedbackBinding.ivfFeedbackContent.getText().toString();
        if ("".equals(content)) return "";
        return MessageFormat.format(feedBackBody, "".equals(name) ? "unKnow" : name, "".equals(id) ? "unKnow" : id, content);
    }

    private void sendMail(String tittle, String body){
        if ("".equals(tittle)){
            showMessage("Tittle is empty.");
            return;
        }

        if ("".equals(body)){
            showMessage("Body is empty.");
            return;
        }

        Intent returnIt = new Intent(Intent.ACTION_SENDTO);

        String[] to = { feedBakEmail };

        returnIt.putExtra(Intent.EXTRA_EMAIL, to);

        returnIt.putExtra(Intent.EXTRA_SUBJECT, tittle);

        returnIt.putExtra(Intent.EXTRA_TEXT, body);

        returnIt.setData(Uri.parse("mailto:"));


        try {
            startActivity(Intent.createChooser(returnIt, "Send mail..."));
            setResult(RESULT_OK, new Intent());
            finish();
            Log.i("Mail","Finished sending email...");

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(String message){
        if (message.equals("")) return;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }
}