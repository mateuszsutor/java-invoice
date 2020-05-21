package pl.edu.agh.mwo.invoice.print;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Mateusz Sutor on 21/05/2020, 23:34
 */
public class PrintInvoiceTest {

    private final int WIDTH_SIZE = 106;

    @Test
    public void testDecoratorLengthMustBeIdentityToWidthSize() {
        StringBuilder sb = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            sb.append(decor);
        }
        Assert.assertEquals(WIDTH_SIZE, sb.length());
    }

    @Test
    public void testDecoratorMustConsistOnlyForSpecificDecor() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        char decor = '-';
        int randomPositionDecor = random.nextInt(WIDTH_SIZE);
        for (int i = 0; i < WIDTH_SIZE; i++) {
            sb.append(decor);
        }
        Assert.assertEquals(decor, sb.charAt(randomPositionDecor));
    }

}