package com.zydl.wealthcalculator;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coder.zzq.smartshow.dialog.DialogBtnClickListener;
import com.coder.zzq.smartshow.dialog.EnsureDialog;
import com.coder.zzq.smartshow.dialog.SmartDialog;
import com.coder.zzq.smartshow.snackbar.SmartSnackbar;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import com.zydl.wealthcalculator.adapter.FundAccountAdapter;
import com.zydl.wealthcalculator.database.BaseDao;
import com.zydl.wealthcalculator.database.BaseDaoImpl;
import com.zydl.wealthcalculator.model.FundAccount;
import com.zydl.wealthcalculator.model.HistoryBean;
import com.zydl.wealthcalculator.utils.SPUtils;
import com.zydl.wealthcalculator.utils.TimeUtils;
import com.zydl.wealthcalculator.view.RainView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private FloatingActionButton mFabAdd;
    private SlideAndDragListView mSlideAndDragListView;
    private FundAccountAdapter mAdapter;
    private TextView mTvTotalAsset, mTvNetAsset, mTvLiabilities;
    private TextView mTvTip;
    private RainView mRainView;
    private ArrayList<FundAccount> mFundAccounts;//账户列表
    private FundAccount moveFundAccountTemp;//移动
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initMenu();
        initUiAndListener();
        initData();
        getWelcomeText();
        getPoison();
    }

    private void getWelcomeText() {
        RequestParams params = new RequestParams("https://api.lovelive.tools/api/SweetNothings");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                SPUtils.put("welcomeText", result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getPoison() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://8zt.cc/").get();
                    Element element = doc.getElementById("sentence");
                    String poison = element.text();
                    SPUtils.put("poisonText", poison);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initUiAndListener() {
        mTvTotalAsset = findViewById(R.id.tv_total_asset);
        mTvNetAsset = findViewById(R.id.tv_net_asset);
        mTvLiabilities = findViewById(R.id.tv_liabilities);
        mTvTip = findViewById(R.id.tv_tip);
        mRainView = findViewById(R.id.rain_view);
        mSlideAndDragListView = findViewById(R.id.sdlv);
        mSlideAndDragListView.setMenu(mMenu);
        mAdapter = new FundAccountAdapter(mContext);
        mSlideAndDragListView.setAdapter(mAdapter);
        mSlideAndDragListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                final FundAccount fundAccount = mFundAccounts.get(itemPosition);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        View dialogView = View.inflate(mContext, R.layout.dialog_add_fund_account, null);
                        builder.setView(dialogView);
                        builder.setTitle("修改账户");
                        final EditText etName = dialogView.findViewById(R.id.et_name);
                        etName.setText(fundAccount.getName());
                        final EditText etNum = dialogView.findViewById(R.id.et_num);
                        if (fundAccount.getNum() != 0) {
                            etNum.setText(fundAccount.getNum() + "");
                        }
                        final RadioGroup radioGroup = dialogView.findViewById(R.id.rg_type);
                        RadioButton rbSavings = dialogView.findViewById(R.id.rb_savings);
                        RadioButton rbOverdraft = dialogView.findViewById(R.id.rb_overdraft);
                        if (fundAccount.getType() == 0) {
                            rbSavings.setChecked(true);
                        } else {
                            rbOverdraft.setChecked(true);
                        }
                        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = etName.getText().toString().trim();
                                String num = etNum.getText().toString().trim();
                                if (name.length() == 0) {
                                    etName.setError("账户名不能为空");
                                    return;
                                }
                                if (num.length() == 0) {
                                    num = "0.0";
                                }
                                fundAccount.setName(name);
                                fundAccount.setNum(Double.valueOf(num));
                                fundAccount.setType(radioGroup.getCheckedRadioButtonId() == R.id.rb_savings ? 0 : 1);
                                updataUI();
                                saveChange();

                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                        break;
                }
                return Menu.ITEM_NOTHING;
            }
        });
        mSlideAndDragListView.setOnItemDeleteListener(new SlideAndDragListView.OnItemDeleteListener() {
            @Override
            public void onItemDeleteAnimationFinished(View view, int position) {
                mFundAccounts.remove(position - mSlideAndDragListView.getHeaderViewsCount());
                updataUI();
                saveChange();
            }
        });
        mSlideAndDragListView.setOnDragDropListener(new SlideAndDragListView.OnDragDropListener() {
            @Override
            public void onDragViewStart(int beginPosition) {
                moveFundAccountTemp = mFundAccounts.get(beginPosition);
            }

            @Override
            public void onDragDropViewMoved(int fromPosition, int toPosition) {
                FundAccount fundAccount = mFundAccounts.remove(fromPosition);
                mFundAccounts.add(toPosition, fundAccount);
            }

            @Override
            public void onDragViewDown(int finalPosition) {
                mFundAccounts.set(finalPosition, moveFundAccountTemp);
            }
        });
        mFabAdd = findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dialogView = View.inflate(mContext, R.layout.dialog_add_fund_account, null);
                builder.setView(dialogView);
                builder.setTitle("添加账户");
                final EditText etName = dialogView.findViewById(R.id.et_name);
                final EditText etNum = dialogView.findViewById(R.id.et_num);
                final RadioGroup radioGroup = dialogView.findViewById(R.id.rg_type);
                builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = etName.getText().toString().trim();
                        String num = etNum.getText().toString().trim();
                        if (name.length() == 0) {
                            etName.setError("账户名不能为空");
                            return;
                        }
                        if (num.length() == 0) {
                            num = "0.0";
                        }
                        FundAccount fundAccount = new FundAccount();
                        fundAccount.setName(name);
                        fundAccount.setNum(Double.valueOf(num));
                        fundAccount.setType(radioGroup.getCheckedRadioButtonId() == R.id.rb_savings ? 0 : 1);
                        mFundAccounts.add(fundAccount);
                        updataUI();
                        saveChange();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        findViewById(R.id.fab_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String welcomeText = (String) SPUtils.get("welcomeText", "往后余生，请多多指教。");
                EnsureDialog dialog = new EnsureDialog();
                dialog.message(welcomeText);
                dialog.confirmBtn("亲一下", new DialogBtnClickListener() {
                    @Override
                    public void onBtnClick(SmartDialog smartDialog, int i, Object o) {
                        smartDialog.dismiss();
                        mRainView.setImgResId(R.mipmap.ic_kiss);
                        mRainView.start(true);
                    }
                });
                dialog.cancelBtn("不亲", null);
                dialog.showInActivity((Activity) mContext);
                getWelcomeText();
            }
        });
        findViewById(R.id.fab_poison).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String poisonText = (String) SPUtils.get("poisonText", "当你怀疑人生的时候，其实这就是你的人生。");
                SmartSnackbar.get((Activity) mContext).showLong(poisonText);
                getPoison();
            }
        });
    }

    private void initMenu() {
        mMenu = new Menu(true);
        mMenu.addItem(new MenuItem.Builder().setWidth(200)
                .setBackground(new ColorDrawable(Color.parseColor("#ff0000")))
                .setText("删除")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .build()
        );
        mMenu.addItem(new MenuItem.Builder().setWidth(200)
                .setBackground(new ColorDrawable(Color.parseColor("#cccccc")))
                .setText("修改")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .build()
        );
    }

    private void initData() {
        try {
            BaseDao<FundAccount, Integer> mFundAccountBaseDao = new BaseDaoImpl<>(mContext, FundAccount.class);
            mFundAccounts = (ArrayList<FundAccount>) mFundAccountBaseDao.queryAll();
            updataUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updataUI() {
        double totalAsset = 0;
        double netAsset = 0;
        double liabilities = 0;
        if (mFundAccounts != null && mFundAccounts.size() > 0) {
            mTvTip.setVisibility(View.GONE);
            mAdapter.updata(mFundAccounts);
            for (int i = 0; i < mFundAccounts.size(); i++) {
                FundAccount fundAccount = mFundAccounts.get(i);
                if (fundAccount.getType() == 0) {
                    totalAsset += fundAccount.getNum();
                    netAsset = netAsset + fundAccount.getNum();
                } else {
                    liabilities += fundAccount.getNum();
                    netAsset = netAsset - fundAccount.getNum();
                }
            }
        } else {
            mTvTip.setVisibility(View.VISIBLE);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        mTvTotalAsset.setText(df.format(totalAsset) + "元");
        mTvNetAsset.setText(df.format(netAsset) + "元");
        mTvLiabilities.setText(df.format(liabilities) + "元");
    }

    private void saveChange() {

        BaseDao<HistoryBean, Integer> dao = new BaseDaoImpl<>(mContext, HistoryBean.class);
        try {
            HistoryBean historyBean = dao.queryT("time", TimeUtils.getCurrentDate());
            if (historyBean == null) {
                historyBean = new HistoryBean();
                historyBean.setTime(TimeUtils.getCurrentDate());
            }
            historyBean.setNum(Double.valueOf(mTvNetAsset.getText().toString().split("元")[0]));
            dao.saveOrUpdate(historyBean);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_chart) {
            startActivity(new Intent(mContext, HistoryActivity.class));
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            BaseDao<FundAccount, Integer> mFundAccountBaseDao = new BaseDaoImpl<>(mContext, FundAccount.class);
            mFundAccountBaseDao.deleteAll();
            mFundAccountBaseDao.save(mFundAccounts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
