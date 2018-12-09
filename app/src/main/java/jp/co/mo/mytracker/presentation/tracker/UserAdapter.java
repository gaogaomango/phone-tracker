package jp.co.mo.mytracker.presentation.tracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.mytracker.R;

public class UserAdapter extends BaseAdapter {

    List<UserInfo> mList;
    WeakReference<Activity> mActivity;

    public UserAdapter(List<UserInfo> list, Activity activity) {
        this.mList = list;
        this.mActivity = new WeakReference<>(activity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = mActivity.get().getLayoutInflater();
            convertView = inflater.inflate(R.layout.contact_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.userName.setText(mList.get(position).getUserName());
        holder.userPhoneNumber.setText(mList.get(position).getPhoneNumber());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userPhoneNumber)
        TextView userPhoneNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
