package pl.edu.agh.mwo.invoice.print;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Mateusz Sutor on 21/05/2020, 23:34
 */
public class PrintInvoiceTest {


    private final int WIDTH_SIZE = 106;
    private PrintInvoice printInvoice;
    private Invoice invoice = new Invoice();

    @Before
    public void createPrintInvoiceForTheTest() {
        printInvoice = new PrintInvoice();
    }


    @Test
    public void testDecoratorLengthMustBeIdentityToWidthSize() {
        String decorator = printInvoice.decorator();
        Assert.assertEquals(WIDTH_SIZE, decorator.length());
    }

    @Test
    public void testEmptyRowMustReturnFormattedString() {
        String value = String.format("\n| %102s |\n", "");
        String emptyRow = printInvoice.printEmptyRow();
        Assert.assertEquals(value, emptyRow);
    }

    @Test
    public void testGenerateInvoiceTitle() {
        String value = String.format("| %-4s | %-20s | %-5s | %14s | %8s | %16s |  %16s |",
                "Id", "Nazwa produtku", "Ilość", "Cena Netto szt",
                "Vat", "Wartość netto", "Wartość brutto");
        String generateHeader = printInvoice.generateHeader();
        Assert.assertEquals(value, generateHeader);
    }

    @Test
    public void testGenerateInvoiceHeader() {
        Date todayDate = new Date();
        String value = String.format("| %-44s  %44s %td-%<tb-%<tY |", "Faktura numer: "
                + invoice.getInvoiceNumber(), "Wystawiona dnia:", todayDate);
        String titlesTemplate = printInvoice.generateTitle(invoice);
        Assert.assertEquals(value, titlesTemplate);
    }

    @Test
    public void testGenerateSayGoodbye() {
        String expectedValue = String.format("| %102s |",
                "Dziękujemy za zakupy - zapraszamy ponownie");
        String sayGoodbye = printInvoice.generateSayGoodbye();
        Assert.assertEquals(expectedValue, sayGoodbye);
    }

    @Test
    public void testGenerateSummary() {

        String testSummary = printInvoice.generateSummary(100, new BigDecimal("23"),
                new BigDecimal("100"), new BigDecimal("123"));

        String expectedSummary = String.format("|%-28s | %5d | %14s | %22s | %22s |",
                "", 100, "100" + printInvoice.CURRENCY_POLAND,
                "23" + printInvoice.CURRENCY_POLAND,
                123 + printInvoice.CURRENCY_POLAND);

        Assert.assertEquals(expectedSummary, testSummary);
    }

    @Test
    public void testGenerateSummaryHeader() {

        String expectedSummaryHeader = String.format("|%-28s | %5s | %14s | %22s | %22s |",
                " Podsumowanie pozycji", "Ilość",
                "Wartość Vat", "Wartość Netto", "Wartość brutto");

        String valueHeader = printInvoice.generateSummaryHeader();

        Assert.assertEquals(expectedSummaryHeader, valueHeader);
    }



}