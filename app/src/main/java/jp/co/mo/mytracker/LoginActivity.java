package jp.co.mo.mytracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.mo.mytracker.common.Parameter;

public class LoginActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.loginPhoneNumber)
    EditText mLoginPhoneNumber;
    @BindView(R.id.nextBtn)
    Button mNextBtn;
    @BindView(R.id.loginProgressBar)
    ProgressBar mLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.mContext = this;

    }

    @OnClick(R.id.nextBtn)
    public void onClicknNxtBtn() {
        String phoneNumber = mLoginPhoneNumber.getText().toString();

        if(!TextUtils.isEmpty(phoneNumber)) {
            AppDataManager.getInstance().saveStringData(this, Parameter.KEY_PHONE_NUMBER, phoneNumber);
            DatabaseManager.updateLocationInFirebase(phoneNumber, new CallBackAction() {
                @Override
                public void success() {
                    moveNextActivity();
                }

                @Override
                public void failed() {

                }
            });
        }
    }

    private void moveNextActivity() {
        Intent i = new Intent(this, MyTrackerActivity.class);
        startActivity(i);
    }
}
