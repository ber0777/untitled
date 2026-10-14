package am.gortsup.testing.service;

import am.gortsup.testing.exception.IllegalOrderStateException;
import am.gortsup.testing.exception.PaymentFailedException;
import am.gortsup.testing.model.Address;
import am.gortsup.testing.model.CartItem;
import am.gortsup.testing.model.Order;
import am.gortsup.testing.model.OrderStatus;
import am.gortsup.testing.model.Product;
import am.gortsup.testing.model.ProductCategory;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OrderServiceTest {

    private Address createAddress() {
        return new Address("1 Main St", "Yerevan", "Kentron", "0001", "AM");
    }

    private Order createOrder(OrderService orderService, String userId) {
        Product product = new Product(
                "book-1", "Book", ProductCategory.BOOKS, 100
        );
        List<CartItem> items = List.of(new CartItem(product, 2));

        return orderService.createOrder(userId, items, createAddress(), 10, null);
    }

    @Test
    public void createOrderCreatesPendingOrder() {
        OrderService orderService = new OrderService();

        Order order = createOrder(orderService, "user-1");

        assertEquals(order.getStatus(), OrderStatus.PENDING);
        assertEquals(order.getSubtotal(), 200.0, 0.001);
        assertEquals(order.getTotal(), 220.0, 0.001);
    }

    @Test
    public void payOrderChangesStatusToPaid() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-2");

        orderService.payOrder(order.getId(), "4532015112830366", order.getTotal());

        assertEquals(order.getStatus(), OrderStatus.PAID);
    }

    @Test
    public void shipOrderSetsTrackingNumberAndStatus() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-3");
        orderService.payOrder(order.getId(), "4532015112830366", order.getTotal());

        orderService.shipOrder(order.getId(), "TRACK-1");

        assertEquals(order.getTrackingNumber(), "TRACK-1");
        assertEquals(order.getStatus(), OrderStatus.SHIPPED);
    }

    @Test
    public void deliverOrderChangesStatusToDelivered() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-4");
        orderService.payOrder(order.getId(), "4532015112830366", order.getTotal());
        orderService.shipOrder(order.getId(), "TRACK-2");

        orderService.deliverOrder(order.getId());

        assertEquals(order.getStatus(), OrderStatus.DELIVERED);
    }

    @Test
    public void cancelOrderCancelsPendingOrder() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-5");

        orderService.cancelOrder(order.getId());

        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
    }

    @Test
    public void refundOrderChangesPaidOrderToRefunded() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-6");
        orderService.payOrder(order.getId(), "4532015112830366", order.getTotal());

        orderService.refundOrder(order.getId(), 10);

        assertEquals(order.getStatus(), OrderStatus.REFUNDED);
    }

    @Test
    public void getOrderReturnsOrderById() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-7");

        assertEquals(orderService.getOrder(order.getId()), order);
    }

    @Test
    public void getOrdersByUserReturnsUsersOrders() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-8");

        assertTrue(orderService.getOrdersByUser("user-8").contains(order));
    }

    @Test
    public void getOrdersByStatusReturnsMatchingOrders() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-9");

        assertTrue(orderService.getOrdersByStatus(OrderStatus.PENDING).contains(order));
    }

    @Test(expectedExceptions = IllegalOrderStateException.class)
    public void shipOrderRejectsUnpaidOrder() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-10");

        orderService.shipOrder(order.getId(), "TRACK-3");
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void payOrderRejectsInvalidCardNumber() {
        OrderService orderService = new OrderService();
        Order order = createOrder(orderService, "user-11");

        orderService.payOrder(order.getId(), "12345678", order.getTotal());
    }
}
