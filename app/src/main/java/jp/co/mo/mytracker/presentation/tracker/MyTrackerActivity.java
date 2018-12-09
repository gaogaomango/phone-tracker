package jp.co.mo.mytracker.presentation.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.mytracker.R;

public class MyTrackerActivity extends AppCompatActivity {

    @BindView(R.id.contactListView)
    ListView mContactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tracker);
        ButterKnife.bind(this);

        List<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("user1", "123456789"));
        list.add(new UserInfo("user2", "0982"));

        UserAdapter adapter = new UserAdapter(list, this);
        mContactListView.setAdapter(adapter);
    }
}
