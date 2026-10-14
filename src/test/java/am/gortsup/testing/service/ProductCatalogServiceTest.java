package am.gortsup.testing.service;

import am.gortsup.testing.exception.ValidationException;
import am.gortsup.testing.model.Product;
import am.gortsup.testing.model.ProductCategory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProductCatalogServiceTest {

    private Product createBook(String id, String name, double price) {
        return new Product(id, name, ProductCategory.BOOKS, price);
    }

    @Test
    public void addProductAddsProductToCatalog() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-1", "Testing Guide", 20);

        Product addedProduct = catalog.addProduct(product);

        assertEquals(addedProduct, product);
    }

    @Test
    public void getProductByIdReturnsMatchingProduct() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-2", "Java Guide", 25);
        catalog.addProduct(product);

        assertEquals(catalog.getProductById("book-2"), product);
    }

    @Test
    public void updateProductPriceRoundsToTwoDecimals() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-3", "Price Guide", 10);
        catalog.addProduct(product);

        catalog.updateProductPrice("book-3", 12.347);

        assertEquals(product.getPrice(), 12.35, 0.001);
    }

    @Test
    public void getProductsByCategoryReturnsMatchingActiveProducts() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product book = createBook("book-4", "Book", 15);
        catalog.addProduct(book);

        assertTrue(catalog.getProductsByCategory(ProductCategory.BOOKS).contains(book));
    }

    @Test
    public void searchProductsByNameFindsPartialCaseInsensitiveMatch() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-5", "Java Testing Guide", 30);
        catalog.addProduct(product);

        assertTrue(catalog.searchProductsByName("TESTING").contains(product));
    }

    @Test
    public void deactivateProductHidesProductFromSearch() {
        ProductCatalogService catalog = new ProductCatalogService();
        catalog.addProduct(createBook("book-6", "Hidden Book", 10));

        catalog.deactivateProduct("book-6");

        assertTrue(catalog.searchProductsByName("Hidden").isEmpty());
    }

    @Test
    public void activateProductMakesProductActiveAgain() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-7", "Visible Book", 10);
        catalog.addProduct(product);
        catalog.deactivateProduct("book-7");

        catalog.activateProduct("book-7");

        assertTrue(catalog.getAllActiveProducts().contains(product));
    }

    @Test
    public void getProductsInPriceRangeReturnsProductsWithinBounds() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-8", "Midrange Book", 20);
        catalog.addProduct(product);

        assertTrue(catalog.getProductsInPriceRange(15, 25).contains(product));
    }

    @Test
    public void applyCategoryDiscountChangesMatchingPrices() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-9", "Discount Book", 100);
        catalog.addProduct(product);

        int changedProducts = catalog.applyCategoryDiscount(ProductCategory.BOOKS, 10);

        assertEquals(changedProducts, 1);
        assertEquals(product.getPrice(), 90.0, 0.001);
    }

    @Test
    public void getAllActiveProductsReturnsActiveCatalogEntries() {
        ProductCatalogService catalog = new ProductCatalogService();
        Product product = createBook("book-10", "Active Book", 10);
        catalog.addProduct(product);

        assertTrue(catalog.getAllActiveProducts().contains(product));
    }

    @Test(expectedExceptions = ValidationException.class)
    public void addProductRejectsNegativePrice() {
        ProductCatalogService catalog = new ProductCatalogService();

        catalog.addProduct(createBook("bad-book", "Invalid Book", -1));
    }
}
