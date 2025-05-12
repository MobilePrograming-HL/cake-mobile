package nix.cake.android.ui.main.profile.order;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.custom.CustomDialog;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderItemResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ActivityMyOrdersBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.ui.main.profile.order.adapter.OrderItemAdapter;
import nix.cake.android.ui.main.profile.order.detail.OrderDetailActivity;

public class MyOrdersActivity extends BaseActivity<ActivityMyOrdersBinding, MyOrdersViewModel> implements OrderItemAdapter.OnItemClickListener, ProductItemAdapter.OnItemClickListener {
    OrderItemAdapter adapter;
    private ProductItemAdapter productAdapter;
    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    public static MutableLiveData<List<OrderResponse>> ORDER_LIST = new MutableLiveData<>();
    public static MutableLiveData<Boolean> IS_EMPTY= new MutableLiveData<>(false);
    public static Integer STATUS = Constants.ORDER_STATUS_ALL;
    public String filterStatus = "All";
    public static int currentPage = 0;
    private boolean isLoading = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpAdapter();
        setUpObserversProduct();
        setUpAdapterProduct();
        setUpObserversOrder();
        startFakeLoading(viewBinding.progressLoadingFirst.progressBar);
    }
    public void setUpAdapter() {
        adapter = new OrderItemAdapter(this);
        viewBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvOrder.setAdapter(adapter);
    }
    private void setUpObserversOrder() {
        ORDER_LIST.observe(this, order_list -> {
            if (order_list != null && adapter != null) {
                viewBinding.progressOrder.progressBar.setVisibility(View.GONE);
                adapter.setData(order_list);
            }
        });
        IS_EMPTY.observe(this, isEmpty -> {
            if (isEmpty) {
                viewModel.isEmpty.set(true);
            }
        });

    }
    private void setUpObserversProduct() {
        PRODUCT_LIST.observe(this, products -> {
            if (productAdapter != null && currentPage > 0 && !products.isEmpty()) {
                productAdapter.setData(products);
                isLoading = false;
                if (products.isEmpty() && currentPage > 0) {
                    currentPage--;
                }

            }

            if (productAdapter != null && currentPage == 0 && !products.isEmpty()) {
                if (fakeProgressAnimator != null && fakeProgressAnimator.isRunning()) {
                    fakeProgressAnimator.cancel();
                }

                ValueAnimator completeAnimator = ValueAnimator.ofInt(currentProgress, 100);
                completeAnimator.setDuration(300);
                completeAnimator.setInterpolator(new DecelerateInterpolator());
                completeAnimator.addUpdateListener(anim -> {
                    int value = (int) anim.getAnimatedValue();
                    viewBinding.progressLoadingFirst.progressBar.setProgress(value);
                });

                completeAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewBinding.progressLoadingFirst.getRoot().setVisibility(View.GONE);
                        isLoading = false;

                        List<ProductResponse> shortList = products.size() > 10 ? products.subList(0, 10) : products;

                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            productAdapter.setData(shortList);
                        });
                    }
                });

                completeAnimator.start();
            }
        });
    }

    public void setUpAdapterProduct() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns
        viewBinding.rvProduct.setLayoutManager(gridLayoutManager);
        viewBinding.rvProduct.setAdapter(productAdapter);

        viewBinding.rvProduct.setItemViewCacheSize(10);
        viewBinding.rvProduct.setHasFixedSize(true);

        viewBinding.rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        if (isLoading) return;
        isLoading = true;
        currentPage++;
        getListProduct(null, currentPage);
    }
    public void onFilterClicked(String filter) {
        filterStatus = "";
        viewBinding.statusOrder.setText(filter);
        switch (filter) {
            case "All":
                filter = "All";
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_ALL);
                viewBinding.tickAll.setVisibility(View.VISIBLE);
                break;
            case "Pending":
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_PENDING);
                viewBinding.tickWaitForConfirmation.setVisibility(View.VISIBLE);
                break;
            case "Processing":
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_PROCESSING);
                viewBinding.tickProcessing.setVisibility(View.VISIBLE);
                break;
            case "Shipping":
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_SHIPPING);
                viewBinding.tickShipping.setVisibility(View.VISIBLE);
                break;
            case "Delivered":
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_DELIVERED);
                viewBinding.tickDelivered.setVisibility(View.VISIBLE);
                break;
            case "Cancelled":
                visibleAllTick();
                getOrderByFilter(Constants.ORDER_STATUS_CANCELED);
                viewBinding.tickCancelled.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        viewBinding.lMyOrder.closeDrawer(viewBinding.menuFilter, true);
    }
    public void getOrderByFilter(Integer status) {
        STATUS = status;
        viewBinding.progressOrder.progressBar.setVisibility(View.VISIBLE);
        viewModel.isEmpty.set(false);
        viewModel.getListOrder(new MainCalback<ResponseListObj<OrderResponse>>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(ResponseListObj<OrderResponse> object) {
                ORDER_LIST.setValue(object.getContent());
                viewModel.isEmpty.set(object.getContent().isEmpty());

                IS_EMPTY.setValue(object.getContent().isEmpty());
                setUpObserversOrder();
            }
        }, status, Constants.SIZE_ITEM);
    }
    public void showRightMenu(){
        viewBinding.lMyOrder.openDrawer(viewBinding.menuFilter);
    }
    public void closeRightMenu(){
        viewBinding.lMyOrder.closeDrawer(viewBinding.menuFilter, true);
    }
    public void visibleAllTick() {
        viewBinding.tickAll.setVisibility(View.GONE);
        viewBinding.tickWaitForConfirmation.setVisibility(View.GONE);
        viewBinding.tickProcessing.setVisibility(View.GONE);
        viewBinding.tickShipping.setVisibility(View.GONE);
        viewBinding.tickDelivered.setVisibility(View.GONE);
        viewBinding.tickCancelled.setVisibility(View.GONE);
    }
    @Override
    public void onItemClick(OrderResponse order) {
        Intent it = new Intent(this, OrderDetailActivity.class);
        startActivity(it);
        viewModel.getOrder(new MainCalback<OrderResponse>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(OrderResponse object) {
                OrderDetailActivity.ORDER.setValue(object);
            }
        }, order.getId());
    }

    @Override
    public void onCancelClick(OrderResponse order, int position) {
        new CustomDialog(this)
                .setDeleteText(getString(R.string.yes))
                .setCancelText(getString(R.string.no))
                .setTitle(getString(R.string.are_you_sure_to_cancel_this_order))
                .setOnClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onDeleteClick() {
                        viewModel.cancelOrder(order.getId());
                        for (OrderResponse item : Objects.requireNonNull(ORDER_LIST.getValue())) {
                            if (item.getId().equals(order.getId())) {
                                if (!Objects.equals(filterStatus, "All")) {
                                    ORDER_LIST.getValue().remove(item);
                                    break;
                                } else {
                                    item.getStatus()
                                            .setStatus(Constants.ORDER_STATUS_CANCELED);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                        if (ORDER_LIST.getValue().isEmpty()) {
                            IS_EMPTY.setValue(true);
                        }
                        if (!Objects.equals(filterStatus, "All")) {
                            adapter.removeItem(position);
                        }
                    }
                    @Override
                    public void onCancelClick() {
                    }
                })
                .show();

    }

    public void getListProduct(String categoryId, Integer page) {
        viewModel.getListProducts(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                List<ProductResponse> currentList = PRODUCT_LIST.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.addAll(object.getContent());
                PRODUCT_LIST.setValue(currentList);
            }
        }, categoryId, page);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getOrderByFilter(STATUS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_orders;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onItemClick(ProductResponse product) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ORDER_LIST = new MutableLiveData<>();
        IS_EMPTY = new MutableLiveData<>(false);
        PRODUCT_LIST = new MutableLiveData<>();
        currentPage = 0;
        isLoading = false;
        STATUS = Constants.ORDER_STATUS_ALL;
    }
}
