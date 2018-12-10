package jp.co.mo.mytracker.presentation.tracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.mytracker.PermissionCallBackAction;
import jp.co.mo.mytracker.R;
import jp.co.mo.mytracker.presentation.AbstractBaseActivity;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class MyTrackerActivity extends AbstractBaseActivity {

    private static final String TAG = MyTrackerActivity.class.getSimpleName();

    @BindView(R.id.contactListView)
    ListView mContactListView;

    private static final int PICK_CONTACT = 1;

    private List<UserInfo> mUserList;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tracker);
        ButterKnife.bind(this);

        mUserList = new ArrayList<>();

        mUserAdapter = new UserAdapter(mUserList, this);
        mContactListView.setAdapter(mUserAdapter);
        refresh();
        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneNumber = mUserList.get(position).getPhoneNumber();
                if(!TextUtils.isEmpty(phoneNumber)) {
                    DatabaseManager.myTrackers.remove(phoneNumber);
                    DatabaseManager.deleteFindersInfo(getApplicationContext(), phoneNumber);
                    refresh();
                }
            }
        });
    }

    private void refresh() {
        mUserList.clear();
        for(Map.Entry e : DatabaseManager.myTrackers.entrySet()) {
            mUserList.add(new UserInfo(e.getValue().toString(), e.getKey().toString()));
        }
        mUserAdapter.notifyDataSetChanged();
        DatabaseManager.saveMyTrackers(this);
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
                checkedPermission(new PermissionCallBackAction() {
                    @Override
                    public void success() {
                        pickContact();
                    }

                    @Override
                    public void failed() {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void pickContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String cNumber = "No number";
                        if (!TextUtils.isEmpty(hasPhone) && hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = DatabaseManager.formatPhoneNumber(phones.getString(phones.getColumnIndex("data1")));
                            Log.d(TAG, "number: " + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        DatabaseManager.myTrackers.put(cNumber, name);
                        DatabaseManager.putFindersInfo(this, cNumber);
                        refresh();
                    }
                }
                break;
        }
    }
}
