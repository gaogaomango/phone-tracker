package jp.co.mo.mytracker.presentation.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.mo.mytracker.CallBackAction;
import jp.co.mo.mytracker.R;
import jp.co.mo.mytracker.common.Parameter;
import jp.co.mo.mytracker.presentation.AbstractBaseActivity;
import jp.co.mo.mytracker.presentation.tracker.MyTrackerActivity;
import jp.co.mo.mytracker.repository.AppDataManager;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class LoginActivity extends AbstractBaseActivity {

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

        if (!TextUtils.isEmpty(phoneNumber)) {
            AppDataManager.getInstance().saveStringData(this, Parameter.KEY_PHONE_NUMBER, DatabaseManager.formatPhoneNumber(phoneNumber));
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
