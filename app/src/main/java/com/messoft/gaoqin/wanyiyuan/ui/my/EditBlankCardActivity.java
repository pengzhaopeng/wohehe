package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.adapter.SelectBlankAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.DictionaryList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.BlankCardModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityEditBlankCardBinding;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.util.List;

/**
 * 编辑或新增银行卡
 */
public class EditBlankCardActivity extends BaseActivity<ActivityEditBlankCardBinding> {

    private int mType; //0-新增 1-编辑
    private BlankCard mBlankCard;

    private String typeCode;
    private String typeName;
    private String branchName;
    private String name;
    private String cardNo;

    private BaseNiceDialog mStreetDialog;
    private RecyclerView mXrcStreet; //银行选择
    private SelectBlankAdapter mSelectStreetAdapter;

    private BlankCardModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blank_card);
    }

    @Override
    protected void initSetting() {
        showContentView();

        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mBlankCard = (BlankCard) getIntent().getSerializableExtra("data");
        }
        mModel = new BlankCardModel();
        mSelectStreetAdapter = new SelectBlankAdapter(getActivity());


        if (mType == 0) {
            setTitle("新增银行卡");
        } else if (mType == 1) {
            setTitle("编辑银行卡");
            bindingView.etCode.setText(mBlankCard.getCardNo());
            bindingView.etType.setText(mBlankCard.getTypeName());
            bindingView.etType1.setText(mBlankCard.getBranchName());
            bindingView.etName.setText(mBlankCard.getName());

            typeCode = mBlankCard.getTypeCode();
            typeName = mBlankCard.getTypeName();
            branchName = mBlankCard.getBranchName();
            name = mBlankCard.getName();
            cardNo = mBlankCard.getCardNo();
        }
    }

    @Override
    protected void initListener() {
        bindingView.tvSure.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doSure();
            }
        });

        //选街道
        bindingView.rlType.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                loadStreetList();
            }
        });
        //点击街道
        mSelectStreetAdapter.setOnItemClickListener(new OnItemClickListener<DictionaryList>() {
            @Override
            public void onClick(DictionaryList street, int position) {
                if (street != null) {
                    typeCode = street.getCode() + "";
                    typeName = street.getName();
                    bindingView.etType.setText(typeName);
                    if (mStreetDialog != null) {
                        mStreetDialog.dismiss();
                    }
                }
            }
        });
    }

    private void loadStreetList() {
        mModel.getDataDictionaryList(getActivity(), "bank_type", new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<DictionaryList> data = (List<DictionaryList>) object;
                showPopStreet(data);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void doSure() {
        typeName = bindingView.etType.getText().toString().trim();
        branchName = bindingView.etType1.getText().toString().trim();
        name = bindingView.etName.getText().toString().trim();
        cardNo = bindingView.etCode.getText().toString().trim();

        if (!StringUtils.isNoEmpty(cardNo)) {
            ToastUtil.showShortToast("请输入银行卡号");
            return;
        }

        if (!StringUtils.isNoEmpty(typeName)) {
            ToastUtil.showShortToast("请选择开户银行");
            return;
        }

        if (!StringUtils.isNoEmpty(branchName)) {
            ToastUtil.showShortToast("请输入开户支行");
            return;
        }

        if (!StringUtils.isNoEmpty(name)) {
            ToastUtil.showShortToast("请输入持卡人姓名");
            return;
        }

        if (mType == 0) { //新增
            mModel.addMemberBankForm(getActivity(),
                    typeCode,
                    typeName, branchName,
                    name,
                    cardNo,
                    null,
                    new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            ToastUtil.showShortToast("添加成功");
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_BLANK_CARD, 0));
                            finish();
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
        } else { //编辑
            mModel.updateMemberBankForm(getActivity(),
                    mBlankCard.getId(),
                    typeCode,
                    typeName, branchName,
                    name,
                    cardNo,
                    null,
                    new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            ToastUtil.showShortToast("修改成功");
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_BLANK_CARD, 0));
                            finish();
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
        }
    }

    private void showPopStreet(final List<DictionaryList> streetList) {
        if (streetList == null || streetList.size() < 1) {
            ToastUtil.showShortToast("暂无银行可以选择");
            return;
        }
        mStreetDialog = NiceDialog.init()
                .setLayoutId(R.layout.dialog_select_street)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        mXrcStreet = holder.getConvertView().findViewById(R.id.xrc_street);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mXrcStreet.setLayoutManager(mLayoutManager);
                        mXrcStreet.setAdapter(mSelectStreetAdapter);
                        // 需加，不然滑动不流畅
                        mXrcStreet.setNestedScrollingEnabled(false);
                        mXrcStreet.setHasFixedSize(false);

                        mSelectStreetAdapter.clear();
                        mSelectStreetAdapter.addAll(streetList);
                        mSelectStreetAdapter.notifyDataSetChanged();
                    }
                })
//                .setDimAmount(0.3f)
                .setShowBottom(true)
                .setOutCancel(true);
        mStreetDialog.show(getActivity().getSupportFragmentManager());
    }

    public static void goPage(Context context, int type, BlankCard blankCard) {
        Intent intent = new Intent(context, EditBlankCardActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("data", blankCard);
        context.startActivity(intent);
    }
}
