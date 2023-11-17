package swtp12.modulecrediting.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DummyServiceTest {
    @Test
    public void whenPalindrom_thenAccept() {
        DummyService palindromeTester = new DummyService();
        assertTrue(palindromeTester.isPalindrome("noon"));
    }

    @Test
    public void whenNearPalindrom_thenReject(){
        DummyService palindromeTester = new DummyService();
        assertFalse(palindromeTester.isPalindrome("neon"));
    }
}
