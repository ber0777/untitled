package am.gortsup.testing.service;

import am.gortsup.testing.exception.ValidationException;
import am.gortsup.testing.model.LoyaltyTier;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LoyaltyServiceTest {

    @Test
    public void addPointsIncreasesCurrentAndLifetimeBalances() {
        LoyaltyService loyaltyService = new LoyaltyService();

        loyaltyService.addPoints("alice", 1200);

        assertEquals(loyaltyService.getPoints("alice"), 1200);
        assertEquals(loyaltyService.getLifetimePoints("alice"), 1200);
    }

    @Test
    public void redeemPointsReducesCurrentBalance() {
        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.addPoints("bob", 300);

        loyaltyService.redeemPoints("bob", 50);

        assertEquals(loyaltyService.getPoints("bob"), 250);
        assertEquals(loyaltyService.getLifetimePoints("bob"), 300);
    }

    @Test
    public void getPointsReturnsCurrentBalance() {
        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.addPoints("carol", 25);

        assertEquals(loyaltyService.getPoints("carol"), 25);
    }

    @Test
    public void getLifetimePointsReturnsEarnedPoints() {
        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.addPoints("dave", 40);
        loyaltyService.redeemPoints("dave", 10);

        assertEquals(loyaltyService.getLifetimePoints("dave"), 40);
    }

    @Test
    public void calculateTierReturnsCorrectTierAtBoundaries() {
        LoyaltyService loyaltyService = new LoyaltyService();

        assertEquals(loyaltyService.calculateTier(0), LoyaltyTier.BRONZE);
        assertEquals(loyaltyService.calculateTier(1000), LoyaltyTier.SILVER);
        assertEquals(loyaltyService.calculateTier(5000), LoyaltyTier.GOLD);
        assertEquals(loyaltyService.calculateTier(10000), LoyaltyTier.PLATINUM);
    }

    @Test
    public void getUserTierUsesLifetimePoints() {
        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.addPoints("ella", 1200);

        assertEquals(loyaltyService.getUserTier("ella"), LoyaltyTier.SILVER);
    }

    @Test
    public void getTierDiscountPercentageReturnsTierDiscount() {
        LoyaltyService loyaltyService = new LoyaltyService();

        assertEquals(
                loyaltyService.getTierDiscountPercentage(LoyaltyTier.GOLD),
                10.0,
                0.001
        );
    }

    @Test
    public void transferPointsMovesPointsBetweenUsers() {
        LoyaltyService loyaltyService = new LoyaltyService();
        loyaltyService.addPoints("frank", 500);
        loyaltyService.addPoints("gina", 100);

        loyaltyService.transferPoints("frank", "gina", 200);

        assertEquals(loyaltyService.getPoints("frank"), 300);
        assertEquals(loyaltyService.getPoints("gina"), 300);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void transferPointsRejectsSelfTransfer() {
        LoyaltyService loyaltyService = new LoyaltyService();

        loyaltyService.transferPoints("harry", "harry", 1);
    }
}
