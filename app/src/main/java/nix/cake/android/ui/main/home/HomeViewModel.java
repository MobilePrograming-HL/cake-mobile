package nix.cake.android.ui.main.home;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class HomeViewModel extends BaseFragmentViewModel {

    List<ProductResponse> productSaleList;
    List<ProductResponse> productNewList;
    List<ProductResponse> productPopularList;
    public HomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        productSaleList = new ArrayList<>();
        productNewList = new ArrayList<>();
        productPopularList = new ArrayList<>();
        generateDummyData();
    }
    private void generateDummyData() {
        // Tạo các category mẫu
        CategoryResponse category1 = new CategoryResponse(1L, "Electronics", "Electronic products", "electronics.jpg");
        CategoryResponse category2 = new CategoryResponse(2L, "Fashion", "Clothing and accessories", "fashion.jpg");
        CategoryResponse category3 = new CategoryResponse(3L, "Books", "Books and literature", "books.jpg");

        productSaleList.add(new ProductResponse(1L, "Smartphone X", "Latest smartphone", 299.99, "smartphone.jpg", category1));
        productSaleList.add(new ProductResponse(2L, "Summer Dress", "Floral summer dress", 49.99, "dress.jpg", category2));
        productSaleList.add(new ProductResponse(3L, "LED TV 55'", "Smart LED television", 499.99, "tv.jpg", category1));
        productSaleList.add(new ProductResponse(4L, "Leather Jacket", "Winter leather jacket", 89.99, "jacket.jpg", category2));
        productSaleList.add(new ProductResponse(5L, "Cookbook", "Best recipes 2025", 29.99, "cookbook.jpg", category3));

        productNewList.add(new ProductResponse(6L, "Wireless Earbuds", "New gen earbuds", 89.99, "earbuds.jpg", category1));
        productNewList.add(new ProductResponse(7L, "Novel 2025", "Bestseller novel", 19.99, "novel.jpg", category3));
        productNewList.add(new ProductResponse(8L, "Smart Speaker", "Voice-controlled speaker", 129.99, "speaker.jpg", category1));
        productNewList.add(new ProductResponse(9L, "Sneakers", "Trendy running shoes", 79.99, "sneakers.jpg", category2));
        productNewList.add(new ProductResponse(10L, "Tech Magazine", "Latest tech trends", 9.99, "magazine.jpg", category3));

        // 5 sản phẩm cho productPopularList
        productPopularList.add(new ProductResponse(11L, "Smart Watch", "Fitness tracker", 149.99, "smartwatch.jpg", category1));
        productPopularList.add(new ProductResponse(12L, "Jeans Classic", "Classic style jeans", 59.99, "jeans.jpg", category2));
        productPopularList.add(new ProductResponse(13L, "Gaming Mouse", "High precision mouse", 69.99, "mouse.jpg", category1));
        productPopularList.add(new ProductResponse(14L, "T-shirt Basic", "Comfortable cotton shirt", 19.99, "tshirt.jpg", category2));
        productPopularList.add(new ProductResponse(15L, "Fantasy Book", "Epic fantasy series", 24.99, "fantasy.jpg", category3));
    }
}
