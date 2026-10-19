package najah.edu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MenuItemTest {

    @Test
    void testCreateMenuItemWithValidData() {
        // Arrange & Act
        MenuItem item = new MenuItem("Pizza", 12.5, true);

        // Assert
        assertEquals("Pizza", item.getName());
        assertEquals(12.5, item.getPrice(), 0.0001);
        assertTrue(item.isAvailable());
    }

    @Test
    void testMenuItemNameIsTrimmed() {
        MenuItem item = new MenuItem("  Pizza  ", 12.5, true);

        assertEquals("Pizza", item.getName());
    }

    @Test
    void testNullNameIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(null, 12.5, true)
        );
    }

    @Test
    void testEmptyNameIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem("", 12.5, true)
        );
    }

    @Test
    void testWhitespaceOnlyNameIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem("   ", 12.5, true)
        );
    }

    @Test
    void testNegativePriceIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem("Pizza", -1.0, true)
        );
    }

    @Test
    void testNaNPriceIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem("Pizza", Double.NaN, true)
        );
    }

    @Test
    void testInfinitePriceIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "Pizza",
                        Double.POSITIVE_INFINITY,
                        true
                )
        );
    }

    @Test
    void testZeroPriceIsAllowed() {
        MenuItem item = new MenuItem("Free Item", 0.0, true);

        assertEquals(0.0, item.getPrice(), 0.0001);
    }

    @Test
    void testUnavailableMenuItem() {
        MenuItem item = new MenuItem("Pizza", 12.5, false);

        assertFalse(item.isAvailable());
    }

    @Test
    void testAvailabilityCanBeChanged() {
        MenuItem item = new MenuItem("Pizza", 12.5, true);

        item.setAvailable(false);

        assertFalse(item.isAvailable());
    }
}