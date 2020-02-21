package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAndQuantity;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderProductBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;

/**
 * 下单界面商品列表
 * https://www.jianshu.com/p/1c91bdc96d16
 */
public class OrderProductListAdapter extends BaseRecyclerViewAdapter<SkuAndQuantity> {

    private BaseActivity mActivity;
    //输入法
    private InputMethodManager inputMethodManager;
    //edittext里的文字内容集合
    private SparseArray<String> etTextAry; //备注留言
    private SparseArray<String> psWayAry; //配送方式
    //edittext的焦点位置
    private int etFocusPos = -1;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            //每次修改文字后，保存在数据集合中
            etTextAry.put(etFocusPos, s.toString());
        }
    };

    public SparseArray<String> getEtTextAry() {
        return etTextAry;
    }

    public  SparseArray<String> getPsWayAry(){
        return psWayAry;
    }

    public OrderProductListAdapter(BaseActivity activity) {
        this.mActivity = activity;
        etTextAry = new SparseArray();
        psWayAry = new SparseArray<>();
        inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_product);
    }


    class ViewHolder extends BaseRecyclerViewHolder<SkuAndQuantity, ItemOrderProductBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final SkuAndQuantity data, final int position) {

            final int pos = position;
            binding.etBeizhu.setText(etTextAry.get(position));
            binding.etBeizhu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        //记录焦点位置
                        etFocusPos = position;
                    }
                }
            });

            String url = Constants.MASTER_URL + "wyy/img/pimg/" + data.getSku().getProductId() + "/" + data.getSku().getMainImage() + "/pa1_200_200.jpg";
            ImgLoadUtil.displayEspImage(url, binding.iv, 1);
            binding.tvTitle.setText(data.getSku().getProductName());
            int quantity = data.getQuantity();
            double sellingPrice = data.getSku().getSellingPrice();
            binding.tvNumber.setText("数量 " + quantity);
//            binding.tvPrice.setText("¥ " + sellingPrice);
            BusinessUtils.setPriceAndPoints1(binding.tvPrice,data.getSku().getPoints(),sellingPrice);

            //单item总数
            binding.tvTotalNumber.setText("共" + quantity + "件");
//            binding.tvTotalMoney.setText("¥ " + NumberUtils.formatNumber(sellingPrice * quantity));

            BusinessUtils.setPriceAndPoints(binding.tvTotalMoney,11,data.getSku().getPoints()*quantity,sellingPrice * quantity);

            //配送方式
//            binding.rlPeisongType.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int[] ids = {1, 2, 3};
//                    psWayAry.put(pos, String.valueOf(ids[0]));
//                    String[] names = {"只工作日送货(双休日、假日不用送)", "只双休日、假日送货(工作日不用送)", "工作日、双休日与假日均可送货"};
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                    builder.setTitle("选择配送方式类型");
//                    //设置列表
//                    builder.setItems(names, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            binding.tvPeisongType.setText(names[which]);
//                            //将 id 设置进 tag 中 方便点击使用
////                            binding.tvNumber.setTag(pos, ids[which]);
//                            psWayAry.put(pos, String.valueOf(ids[which]));
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolder viewHolder = (ViewHolder) holder;
        //删除文字变化监听器
        viewHolder.binding.etBeizhu.removeTextChangedListener(textWatcher);
        //清除焦点
        viewHolder.binding.etBeizhu.clearFocus();
        //如果当前隐藏的item是焦点所在的位置，那么隐藏输入法，否则输入法不会自动关闭
        if (etFocusPos == holder.getAdapterPosition()) {
            inputMethodManager.hideSoftInputFromWindow(((ViewHolder) holder).binding.etBeizhu.getWindowToken(), 0);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewHolder viewHolder = (ViewHolder) holder;
        //添加文字变化监听器
        viewHolder.binding.etBeizhu.addTextChangedListener(textWatcher);
        //如果当前显示的item是焦点记录位置，那么获取焦点，并把光标位置置于文字最后，需要显示输入法的话可自行添加操作
        if (etFocusPos == holder.getAdapterPosition()) {
            viewHolder.binding.etBeizhu.requestFocus();
            viewHolder.binding.etBeizhu.setSelection(viewHolder.binding.etBeizhu.getText().length());
        }
    }
}
