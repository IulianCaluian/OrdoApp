package ordo.azurewebsites.net.ordo;

import org.junit.Test;

import ordo.azurewebsites.net.ordo.helper.EmailValidator;
import ordo.azurewebsites.net.ordo.helper.QRValidator;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    // Teste validator email.
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.co.uk"));
    }

    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email..com"));
    }

    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("@email.com"));
    }
    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(null));
    }

    // validatori coduri QR
    @Test
    public void qrValidator_ReturnTrue1() {
        assertTrue(QRValidator.isValid("000101"));
    }

    @Test
    public void qrValidator_ReturnTrue2() {
        assertTrue(QRValidator.isValid("102100"));
    }

    @Test
    public void qrValidator_ReturnTrue3() {
        assertTrue(QRValidator.isValid("345678"));
    }

    @Test
    public void qrValidator_ReturnFalse1() {
        assertFalse(QRValidator.isValid("00010"));
    }

    @Test
    public void qrValidator_ReturnFalse2() {
        assertFalse(QRValidator.isValid("A02100"));
    }

    @Test
    public void qrValidator_ReturnFalse3() {
        assertFalse(QRValidator.isValid("3456784"));
    }
}