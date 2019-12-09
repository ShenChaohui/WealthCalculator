package com.zydl.wealthcalculator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zydl.wealthcalculator.R;
import com.zydl.wealthcalculator.model.FundAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sch.
 * Date: 2019/11/26
 * description:
 */
public class FundAccountAdapter extends BaseAdapter {
    private ArrayList<FundAccount> mFundAccounts;
    private Context mContext;

    public FundAccountAdapter(Context context) {
        mContext = context;
        mFundAccounts = new ArrayList<>();
    }

    public void updata(ArrayList<FundAccount> fundAccounts) {
        this.mFundAccounts = fundAccounts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFundAccounts.size();
    }

    @Override
    public Object getItem(int i) {
        return mFundAccounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_fund_account, null);
            new ViewHolder(view);
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        FundAccount fundAccount = mFundAccounts.get(i);
        viewHolder.mTvName.setText(fundAccount.getName());
        if (fundAccount.getType() == 1) {
            viewHolder.mTvNum.setTextColor(Color.RED);
        } else {
            viewHolder.mTvNum.setTextColor(Color.parseColor("#32CD32"));
        }
        DecimalFormat df = new DecimalFormat("0.00");
        viewHolder.mTvNum.setText(df.format(fundAccount.getNum()) + "元");
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        view.startAnimation(alphaAnimation);
        return view;
    }

    class ViewHolder {
        private TextView mTvName;
        private TextView mTvNum;

        public ViewHolder(View view) {
            mTvName = view.findViewById(R.id.tv_name);
            mTvNum = view.findViewById(R.id.tv_num);
            view.setTag(this);
        }
    }
}
