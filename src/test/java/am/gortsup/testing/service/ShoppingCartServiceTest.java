package am.gortsup.testing.service;

import am.gortsup.testing.exception.InvalidCouponException;
import am.gortsup.testing.model.Coupon;
import am.gortsup.testing.model.DiscountType;
import am.gortsup.testing.model.Product;
import am.gortsup.testing.model.ProductCategory;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ShoppingCartServiceTest {

    private Product createBook() {
        return new Product("book-1", "Book", ProductCategory.BOOKS, 20);
    }

    private ShoppingCartService createCartWithTwoBooks() {
        ShoppingCartService cartService = new ShoppingCartService();
        cartService.addItem("cart", createBook(), 2);
        return cartService;
    }

    @Test
    public void addItemAddsProductToCart() {
        ShoppingCartService cartService = new ShoppingCartService();

        cartService.addItem("cart", createBook(), 2);

        assertEquals(cartService.getCartItems("cart").size(), 1);
    }

    @Test
    public void removeItemRemovesProductFromCart() {
        ShoppingCartService cartService = createCartWithTwoBooks();

        cartService.removeItem("cart", "book-1");

        assertTrue(cartService.getCartItems("cart").isEmpty());
    }

    @Test
    public void updateQuantityChangesItemCount() {
        ShoppingCartService cartService = createCartWithTwoBooks();

        cartService.updateQuantity("cart", "book-1", 4);

        assertEquals(cartService.getItemCount("cart"), 4);
    }

    @Test
    public void applyCouponAttachesValidCoupon() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon coupon = new Coupon(
                "SAVE10", DiscountType.PERCENTAGE, 10, 0,
                LocalDate.parse("2030-12-31"), 10
        );

        cartService.applyCoupon("cart", coupon, LocalDate.parse("2026-01-01"));

        assertEquals(cartService.getAppliedCoupon("cart").orElse(null), coupon);
    }

    @Test
    public void removeCouponRemovesAppliedCoupon() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon coupon = new Coupon(
                "SAVE5", DiscountType.FIXED_AMOUNT, 5, 0,
                LocalDate.parse("2030-12-31"), 10
        );
        cartService.applyCoupon("cart", coupon, LocalDate.parse("2026-01-01"));

        cartService.removeCoupon("cart");

        assertTrue(cartService.getAppliedCoupon("cart").isEmpty());
    }

    @Test
    public void calculateSubtotalMultipliesPriceByQuantity() {
        ShoppingCartService cartService = createCartWithTwoBooks();

        assertEquals(cartService.calculateSubtotal("cart"), 40.0, 0.001);
    }

    @Test
    public void calculateDiscountAmountCalculatesCouponDiscount() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon coupon = new Coupon(
                "SAVE10", DiscountType.PERCENTAGE, 10, 0,
                LocalDate.parse("2030-12-31"), 10
        );
        cartService.applyCoupon("cart", coupon, LocalDate.parse("2026-01-01"));

        assertEquals(cartService.calculateDiscountAmount("cart"), 4.0, 0.001);
    }

    @Test
    public void calculateTotalIncludesDiscountAndTax() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon coupon = new Coupon(
                "SAVE10", DiscountType.PERCENTAGE, 10, 0,
                LocalDate.parse("2030-12-31"), 10
        );
        cartService.applyCoupon("cart", coupon, LocalDate.parse("2026-01-01"));

        assertEquals(cartService.calculateTotal("cart", 10), 39.6, 0.001);
    }

    @Test
    public void clearCartRemovesItemsAndCoupon() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon coupon = new Coupon(
                "SAVE5", DiscountType.FIXED_AMOUNT, 5, 0,
                LocalDate.parse("2030-12-31"), 10
        );
        cartService.applyCoupon("cart", coupon, LocalDate.parse("2026-01-01"));

        cartService.clearCart("cart");

        assertTrue(cartService.getCartItems("cart").isEmpty());
        assertTrue(cartService.getAppliedCoupon("cart").isEmpty());
    }

    @Test
    public void getCartItemsReturnsItemsInCart() {
        ShoppingCartService cartService = createCartWithTwoBooks();

        assertEquals(cartService.getCartItems("cart").get(0).getQuantity(), 2);
    }

    @Test
    public void getItemCountReturnsSumOfQuantities() {
        ShoppingCartService cartService = new ShoppingCartService();
        cartService.addItem("cart", createBook(), 2);
        cartService.addItem("cart", new Product(
                "book-2", "Another Book", ProductCategory.BOOKS, 10
        ), 3);

        assertEquals(cartService.getItemCount("cart"), 5);
    }

    @Test(expectedExceptions = InvalidCouponException.class)
    public void applyCouponRejectsExpiredCoupon() {
        ShoppingCartService cartService = createCartWithTwoBooks();
        Coupon expiredCoupon = new Coupon(
                "OLD", DiscountType.FIXED_AMOUNT, 5, 0,
                LocalDate.parse("2020-01-01"), 10
        );

        cartService.applyCoupon(
                "cart", expiredCoupon, LocalDate.parse("2026-01-01")
        );
    }
}
