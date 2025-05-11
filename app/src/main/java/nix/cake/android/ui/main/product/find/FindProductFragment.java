package nix.cake.android.ui.main.product.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.databinding.FragmentFindProductBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.home.adapter.ItemCategoryHomeAdapter;

public class FindProductFragment extends BaseFragment<FragmentFindProductBinding, FindProductFragmentViewModel> implements ItemCategoryHomeAdapter.OnItemClickListener{
    public static MutableLiveData<List<CategoryResponse>> CATEGORIES_LIST = new MutableLiveData<>();
    private ItemCategoryHomeAdapter categoryAdapter;

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObserversCategory();
        binding.search.requestFocus();
        showKeyboard(binding.search);
        setUpCategoryAdapter();
    }

    private void showKeyboard(View view) {
        view.post(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    public void onSearchClick(){
        Intent it = new Intent(getContext(), FindProductActivity.class);
        startActivity(it);
        String name = binding.search.getText().toString();
        if (name.isEmpty()) {
            ((MainActivity) requireActivity()).getListProductForSearch(null,"", "asc");
        } else {
            ((MainActivity) requireActivity()).getListProductForSearch(null, name, "asc");
        }
        ((MainActivity) requireActivity()).getListCategoriesForSearch();
    }

    public void setUpObserversCategory() {
        CATEGORIES_LIST.observe(this, categories -> {
            if (categoryAdapter != null) {
                categoryAdapter.setData(categories);
            }
        });
    }
    public void setUpCategoryAdapter() {
        categoryAdapter = new ItemCategoryHomeAdapter(this);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setAdapter(categoryAdapter);
    }
    public void onBackClick() {
        ((MainActivity) requireActivity()).navigateToShopFragment();
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_product;
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onItemCateSearchClick(CategoryResponse category) {
        Intent it = new Intent(getContext(), FindProductActivity.class);
        startActivity(it);
        ((MainActivity) requireActivity()).getListCategoriesForSearch();
        ((MainActivity) requireActivity()).getListProductForSearch(category.getId(),"", "asc");
    }
}
