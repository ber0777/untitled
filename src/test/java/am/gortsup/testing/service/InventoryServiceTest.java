package am.gortsup.testing.service;

import am.gortsup.testing.exception.InsufficientStockException;
import am.gortsup.testing.exception.ValidationException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class InventoryServiceTest {

    @Test
    public void setStock() {
        InventoryService inventoryService = new InventoryService();

        inventoryService.setStock("sku", 10);

        assertEquals(inventoryService.getStock("sku"), 10);
    }

    @Test
    public void getStockReturnsZeroForUnknownProduct() {
        InventoryService inventoryService = new InventoryService();

        assertEquals(inventoryService.getStock("unknown"), 0);
    }

    @Test
    public void getReservedStockStartsAtZero() {
        InventoryService inventoryService = new InventoryService();

        inventoryService.setStock("sku", 10);

        assertEquals(inventoryService.getReservedStock("sku"), 0);
    }

    @Test
    public void isInStockChecksRequestedQuantity() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 10);

        assertTrue(inventoryService.isInStock("sku", 8));
        assertFalse(inventoryService.isInStock("sku", 11));
    }

    @Test
    public void reserveStockMovesUnitsToReservedStock() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 10);

        inventoryService.reserveStock("sku", 4);

        assertEquals(inventoryService.getStock("sku"), 6);
        assertEquals(inventoryService.getReservedStock("sku"), 4);
    }

    @Test
    public void releaseStockReturnsUnitsToAvailableStock() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 10);
        inventoryService.reserveStock("sku", 4);

        inventoryService.releaseStock("sku", 1);

        assertEquals(inventoryService.getStock("sku"), 7);
        assertEquals(inventoryService.getReservedStock("sku"), 3);
    }

    @Test
    public void deductStockRemovesReservedUnits() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 10);
        inventoryService.reserveStock("sku", 4);

        inventoryService.deductStock("sku", 2);

        assertEquals(inventoryService.getReservedStock("sku"), 2);
        assertEquals(inventoryService.getStock("sku"), 6);
    }

    @Test
    public void restockAddsAvailableUnits() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 10);

        inventoryService.restock("sku", 5);

        assertEquals(inventoryService.getStock("sku"), 15);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void setStockRejectsNegativeQuantity() {
        new InventoryService().setStock("sku", -1);
    }

    @Test(expectedExceptions = InsufficientStockException.class)
    public void reserveStockRejectsQuantityAboveAvailableStock() {
        InventoryService inventoryService = new InventoryService();
        inventoryService.setStock("sku", 1);

        inventoryService.reserveStock("sku", 2);
    }
}
