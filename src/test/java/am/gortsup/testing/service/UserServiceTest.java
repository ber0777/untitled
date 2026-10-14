package am.gortsup.testing.service;

import am.gortsup.testing.exception.AccountLockedException;
import am.gortsup.testing.exception.AuthenticationException;
import am.gortsup.testing.exception.ValidationException;
import am.gortsup.testing.model.Role;
import am.gortsup.testing.model.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class UserServiceTest {

    @Test
    public void registerUserCreatesCustomerWhenRoleIsNull() {
        UserService userService = new UserService();

        User user = userService.registerUser(
                "alice_1", "alice@example.com", "Strong1!", null
        );

        assertEquals(user.getRole(), Role.CUSTOMER);
        assertTrue(user.isActive());
    }

    @Test
    public void authenticateAcceptsCorrectCredentials() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "bob", "bob@example.com", "Strong1!", Role.CUSTOMER
        );

        assertEquals(userService.authenticate("bob", "Strong1!"), user);
    }

    @Test
    public void authenticateLocksAccountAfterThreeFailures() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "carol", "carol@example.com", "Strong1!", Role.CUSTOMER
        );

        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                userService.authenticate("carol", "wrong");
                fail("Expected authentication failure");
            } catch (AuthenticationException expected) {
                // The first two failed attempts do not yet lock the account.
            }
        }

        try {
            userService.authenticate("carol", "wrong");
            fail("Expected account to be locked");
        } catch (AccountLockedException expected) {
            // The third failed attempt locks the account.
        }

        assertTrue(user.isLocked());
    }

    @Test
    public void changePasswordAllowsLoginWithNewPassword() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "dave", "dave@example.com", "Strong1!", Role.CUSTOMER
        );

        userService.changePassword(user.getId(), "Strong1!", "Better2@");

        assertEquals(userService.authenticate("dave", "Better2@"), user);
    }

    @Test
    public void lockUserLocksTheAccount() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "ella", "ella@example.com", "Strong1!", Role.CUSTOMER
        );

        userService.lockUser(user.getId());

        assertTrue(user.isLocked());
    }

    @Test
    public void unlockUserClearsLockAndFailedAttempts() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "frank", "frank@example.com", "Strong1!", Role.CUSTOMER
        );
        userService.lockUser(user.getId());

        userService.unlockUser(user.getId());

        assertFalse(user.isLocked());
        assertEquals(user.getFailedLoginAttempts(), 0);
    }

    @Test
    public void getUserByIdFindsRegisteredUser() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "gina", "gina@example.com", "Strong1!", Role.CUSTOMER
        );

        assertEquals(userService.getUserById(user.getId()), user);
    }

    @Test
    public void getUserByUsernameIgnoresCase() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "harry", "harry@example.com", "Strong1!", Role.CUSTOMER
        );

        assertEquals(userService.getUserByUsername("HARRY"), user);
    }

    @Test
    public void updateEmailChangesUserEmail() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "iris", "iris@example.com", "Strong1!", Role.CUSTOMER
        );

        userService.updateEmail(user.getId(), "iris.new@example.com");

        assertEquals(user.getEmail(), "iris.new@example.com");
    }

    @Test
    public void hasPermissionChecksCustomerPermissions() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "john", "john@example.com", "Strong1!", Role.CUSTOMER
        );

        assertTrue(userService.hasPermission(user.getId(), "READ"));
        assertFalse(userService.hasPermission(user.getId(), "MANAGE_ORDERS"));
    }

    @Test
    public void getAllUsersReturnsRegisteredUsers() {
        UserService userService = new UserService();
        userService.registerUser("kate", "kate@example.com", "Strong1!", Role.CUSTOMER);
        userService.registerUser("leo", "leo@example.com", "Strong1!", Role.MANAGER);

        assertEquals(userService.getAllUsers().size(), 2);
    }

    @Test
    public void deactivateUserMarksAccountInactive() {
        UserService userService = new UserService();
        User user = userService.registerUser(
                "mike", "mike@example.com", "Strong1!", Role.CUSTOMER
        );

        userService.deactivateUser(user.getId());

        assertFalse(user.isActive());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void registerUserRejectsDuplicateUsername() {
        UserService userService = new UserService();
        userService.registerUser("nina", "n1@example.com", "Strong1!", Role.CUSTOMER);

        userService.registerUser("nina", "n2@example.com", "Strong1!", Role.CUSTOMER);
    }
}
