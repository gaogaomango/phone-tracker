package jp.co.mo.mytracker.presentation.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_list, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goback:
                finish();
                return true;
            case R.id.add:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
