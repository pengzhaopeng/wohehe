package com.messoft.gaoqin.wanyiyuan.view.sku.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.bean.Sku;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAttribute;
import com.messoft.gaoqin.wanyiyuan.databinding.DialogProductSkuBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;

import java.util.List;

/**
 * Created by liufei on 2017/11/30.
 */
public class ProductSkuDialog extends Dialog {
    private DialogProductSkuBinding binding;
    private Context context;
    private List<Sku> skuList;
    private int type; //0-选择属性  1-选完属性直接购买
    private Callback callback;

    private Sku selectedSku;
    private String priceFormat;
    private String updateTime;
    private String stockQuantityFormat;

    public ProductSkuDialog(@NonNull Context context) {
        this(context, R.style.CommonBottomDialogStyle);
    }

    public ProductSkuDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_product_sku, null, false);
        setContentView(binding.getRoot());

        binding.ibSkuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.btnSkuQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt > 1) {
                    String newQuantity = String.valueOf(quantityInt - 1);
                    binding.etSkuQuantityInput.setText(newQuantity);
                    binding.etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(quantityInt - 1);
                }
            }
        });
        binding.btnSkuQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity) || selectedSku == null) {
                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt < selectedSku.getStockQuantity()) {
                    String newQuantity = String.valueOf(quantityInt + 1);
                    binding.etSkuQuantityInput.setText(newQuantity);
                    binding.etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(quantityInt + 1);
                }
            }
        });
        binding.etSkuQuantityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE || selectedSku == null) {
                    return false;
                }
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    return false;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt <= 1) {
                    binding.etSkuQuantityInput.setText("1");
                    binding.etSkuQuantityInput.setSelection(1);
                    updateQuantityOperator(1);
                } else if (quantityInt >= selectedSku.getStockQuantity()) {
                    String newQuantity = String.valueOf(selectedSku.getStockQuantity());
                    binding.etSkuQuantityInput.setText(newQuantity);
                    binding.etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(selectedSku.getStockQuantity());
                } else {
                    binding.etSkuQuantityInput.setSelection(quantity.length());
                    updateQuantityOperator(quantityInt);
                }
                return false;
            }
        });
        //未选中展示默认图片等
        binding.scrollSkuList.setListener(new OnSkuListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onUnselected(SkuAttribute unselectedAttribute) {
                selectedSku = null;
                //TODO默认库存
//                GImageLoader.displayUrl(context, binding.ivSkuLogo, product.getMainImage());
//                binding.tvSkuQuantity.setText(String.format(stockQuantityFormat, product.getStockQuantity()));
                String url = Constants.MASTER_URL + "wyy/img/pimg/" + skuList.get(0).getProductId() + "/" + skuList.get(0).getMainImage() + "/pa1_200_200.jpg";
                ImgLoadUtil.displayEspImage(url, binding.ivSkuLogo, 1, TimeUtils.date2TimeStamp(updateTime));

                String firstUnselectedAttributeName = binding.scrollSkuList.getFirstUnelectedAttributeName();
                binding.tvSkuInfo.setText("请选择：" + firstUnselectedAttributeName);
                binding.btnSubmit.setEnabled(false);
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity));
                } else {
                    updateQuantityOperator(0);
                }
            }

            @Override
            public void onSelect(SkuAttribute selectAttribute) {
                String firstUnselectedAttributeName = binding.scrollSkuList.getFirstUnelectedAttributeName();
                binding.tvSkuInfo.setText("请选择：" + firstUnselectedAttributeName);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSkuSelected(Sku sku) {
                selectedSku = sku;
//                GImageLoader.displayUrl(context, binding.ivSkuLogo, selectedSku.getMainImage());
                String url = Constants.MASTER_URL + "wyy/img/pimg/" + selectedSku.getProductId() + "/" + selectedSku.getMainImage() + "/pa1_200_200.jpg";
                ImgLoadUtil.displayEspImage(url, binding.ivSkuLogo, 1, TimeUtils.date2TimeStamp(updateTime));

                List<SkuAttribute> attributeList = selectedSku.getAttributes();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < attributeList.size(); i++) {
                    if (i != 0) {
                        builder.append("　");
                    }
                    SkuAttribute attribute = attributeList.get(i);
                    builder.append("\"" + attribute.getValue() + "\"");
                }

                //选中后更换价格
//                binding.tvSkuSellingPrice.setText(String.format(priceFormat, NumberUtils.formatNumber(selectedSku.getSellingPrice())));
                setPrice(selectedSku);
                binding.tvSkuSellingPriceUnit.setText("/" + skuList.get(0).getUnitValue());

                binding.tvSkuInfo.setText("已选：" + builder.toString());
                binding.tvSkuQuantity.setText(String.format(stockQuantityFormat, selectedSku.getStockQuantity()));
                binding.btnSubmit.setEnabled(true);
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity));
                } else {
                    updateQuantityOperator(0);
                }
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = binding.etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt > 0 && quantityInt <= selectedSku.getStockQuantity()) {
                    callback.onAdded(type, selectedSku, quantityInt);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "商品数量超出库存，请修改数量", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setData(List<Sku> skuList, String updateTime, Callback callback) {
        this.skuList = skuList;
        this.callback = callback;
        this.updateTime = updateTime;

        priceFormat = context.getString(R.string.comm_price_format);
        stockQuantityFormat = context.getString(R.string.product_detail_sku_stock);

        updateSkuData();
        updateQuantityOperator(1);
    }

    @SuppressLint("SetTextI18n")
    private void updateSkuData() {
        binding.scrollSkuList.setSkuList(skuList);

        Sku firstSku = skuList.get(0);
        if (firstSku.getStockQuantity() > 0) {
            selectedSku = firstSku;
            // 选中第一个sku
            binding.scrollSkuList.setSelectedSku(selectedSku);

//            GImageLoader.displayUrl(context, binding.ivSkuLogo, selectedSku.getMainImage());
            String url = Constants.MASTER_URL + "wyy/img/pimg/" + selectedSku.getProductId() + "/" + selectedSku.getMainImage() + "/pa1_200_200.jpg";
            ImgLoadUtil.displayEspImage(url, binding.ivSkuLogo, 1, TimeUtils.date2TimeStamp(updateTime));

//            binding.tvSkuSellingPrice.setText(String.format(priceFormat, NumberUtils.formatNumber(selectedSku.getSellingPrice())));
            setPrice(selectedSku);
            binding.tvSkuSellingPriceUnit.setText("/" + skuList.get(0).getUnitValue());

            binding.tvSkuQuantity.setText(String.format(stockQuantityFormat, selectedSku.getStockQuantity()));
            binding.btnSubmit.setEnabled(selectedSku.getStockQuantity() > 0);
            List<SkuAttribute> attributeList = selectedSku.getAttributes();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < attributeList.size(); i++) {
                if (i != 0) {
                    builder.append("　");
                }
                SkuAttribute attribute = attributeList.get(i);
                builder.append("\"" + attribute.getValue() + "\"");
            }
            binding.tvSkuInfo.setText("已选：" + builder.toString());
            DebugUtil.error("updateSkuData", "选中sku回调一次");
        } else {
//            GImageLoader.displayUrl(context, binding.ivSkuLogo, product.getMainImage());
            String url = Constants.MASTER_URL + "wyy/img/pimg/" + skuList.get(0).getProductId() + "/" + skuList.get(0).getMainImage() + "/pa1_200_200.jpg";
            ImgLoadUtil.displayEspImage(url, binding.ivSkuLogo, 1, TimeUtils.date2TimeStamp(updateTime));

//            binding.tvSkuSellingPrice.setText(String.format(priceFormat, NumberUtils.formatNumber(skuList.get(0).getSellingPrice() / 100)));
//            binding.tvSkuSellingPrice.setText(String.format(priceFormat, NumberUtils.formatNumber(skuList.get(0).getSellingPrice())));
            setPrice(skuList.get(0));

            binding.tvSkuSellingPriceUnit.setText("/" + skuList.get(0).getUnitValue());
            //TODO 默认库存
//            binding.tvSkuQuantity.setText(String.format(stockQuantityFormat, product.getStockQuantity()));
            binding.tvSkuQuantity.setText(String.format(stockQuantityFormat, skuList.get(0).getStockQuantity()));

            binding.btnSubmit.setEnabled(false);
            binding.tvSkuInfo.setText("请选择：" + skuList.get(0).getAttributes().get(0).getKey());
        }
    }

    /**
     * 设置价格，包括积分商城 和非积分商城
     */
    private void setPrice(Sku selectedSku) {
//        if (selectedSku.getClassCode().equals("jfsc")) { //积分商城
//            binding.rlJfPrice.setVisibility(View.VISIBLE);
//            binding.tvSkuSellingPrice.setVisibility(View.GONE);
//            binding.tvSkuSellingPriceUnit.setVisibility(View.GONE);
//
//            //商品价格0
//            if (selectedSku.getSellingPrice() == 0) {
//                binding.llHasMoney.setVisibility(View.GONE);
//            }else{
//                binding.llHasMoney.setVisibility(View.VISIBLE);
//            }
//            binding.tvPoints.setText(String.valueOf(selectedSku.getPoints()));
//            binding.tvPrice.setText(String.valueOf(selectedSku.getSellingPrice()));
//        }else{ //不是积分商城
//            binding.rlJfPrice.setVisibility(View.GONE);
//            binding.tvSkuSellingPrice.setVisibility(View.VISIBLE);
//            binding.tvSkuSellingPriceUnit.setVisibility(View.VISIBLE);
//            binding.tvSkuSellingPrice.setText(String.format(priceFormat, NumberUtils.formatNumber(selectedSku.getSellingPrice())));
//        }
        BusinessUtils.setPriceAndPoints(binding.tvSkuSellingPrice,11, selectedSku.getPoints(), selectedSku.getSellingPrice());
    }

    private void updateQuantityOperator(int newQuantity) {
        if (selectedSku == null) {
            binding.btnSkuQuantityMinus.setEnabled(false);
            binding.btnSkuQuantityPlus.setEnabled(false);
            binding.etSkuQuantityInput.setEnabled(false);
        } else {
            if (newQuantity <= 1) {
                binding.btnSkuQuantityMinus.setEnabled(false);
                binding.btnSkuQuantityPlus.setEnabled(true);
            } else if (newQuantity >= selectedSku.getStockQuantity()) {
                binding.btnSkuQuantityMinus.setEnabled(true);
                binding.btnSkuQuantityPlus.setEnabled(false);
            } else {
                binding.btnSkuQuantityMinus.setEnabled(true);
                binding.btnSkuQuantityPlus.setEnabled(true);
            }
            binding.etSkuQuantityInput.setEnabled(true);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 解决键盘遮挡输入框问题
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // KeyboardConflictCompat.assistWindow(getWindow());
//        AppUtils.transparencyBar(getWindow());
    }


    public interface Callback {
        void onAdded(int type, Sku sku, int quantity);
    }
}
