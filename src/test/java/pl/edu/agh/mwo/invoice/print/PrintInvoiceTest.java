package pl.edu.agh.mwo.invoice.print;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.mwo.invoice.Invoice;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by Mateusz Sutor on 21/05/2020, 23:34
 */
public class PrintInvoiceTest {

    private static final int WIDTH_SIZE = 106;
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
    public void testGenerateRow() {
        int id = 10;
        Integer quantity = 2;
        String productName = "Product Test";
        BigDecimal netPrice = new BigDecimal("100");
        BigDecimal taxPercent = new BigDecimal("0.23");
        BigDecimal netValue = netPrice.multiply(new BigDecimal(quantity));
        BigDecimal grossValue = new BigDecimal("123.00");


        String expectedRow = String.format("\n| %-4s | %-20s | %5d | %14s | %8s | %16s | %17s |\n",
                id, productName, quantity, netPrice + printInvoice.CURRENCY_POLAND, taxPercent + "%",
                netValue + printInvoice.CURRENCY_POLAND, grossValue + printInvoice.CURRENCY_POLAND);

        String rowValue = printInvoice.generateRows(id, productName, quantity,
                netPrice, taxPercent, netValue, grossValue);

        Assert.assertEquals(expectedRow, rowValue);
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

    @Test
    public void testPrintingSayGoodByeSection() {

        StringBuilder expectedDecorator = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            expectedDecorator.append(decor);
        }

        String expectedGeneratedSayGoodbye = String.format("| %102s |",
                "Dziękujemy za zakupy - zapraszamy ponownie");


        String expectedEmptyRow = String.format("\n| %102s |\n", "");

        StringBuilder expectedLayout = new StringBuilder();
        expectedLayout.append(expectedEmptyRow);
        expectedLayout.append(expectedGeneratedSayGoodbye);
        expectedLayout.append(expectedEmptyRow);
        expectedLayout.append(expectedDecorator);

        String sayGoodByeSection = printInvoice.printSayGoodbye();

        Assert.assertEquals(expectedLayout.toString(), sayGoodByeSection);
    }

    @Test
    public void testPrintingHeaders() {

        StringBuilder expectedDecorator = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            expectedDecorator.append(decor);
        }

        String expectedHeader = String.format("| %-4s | %-20s | %-5s | %14s | %8s | %16s |  %16s |",
                "Id", "Nazwa produtku", "Ilość", "Cena Netto szt",
                "Vat", "Wartość netto", "Wartość brutto");

        StringBuilder expectedHeaders = new StringBuilder();
        expectedHeaders.append(expectedDecorator);
        expectedHeaders.append("\n");
        expectedHeaders.append(expectedHeader);
        expectedHeaders.append("\n");
        expectedHeaders.append(expectedDecorator);

        String valueHeader = printInvoice.printHeaders();

        Assert.assertEquals(expectedHeaders.toString(), valueHeader);
    }

    @Test
    public void testPrintingRows() {
        int id = 10;
        Integer quantity = 2;
        String productName = "Product Test";
        BigDecimal netPrice = new BigDecimal("100");
        BigDecimal taxPercent = new BigDecimal("0.23");
        BigDecimal netValue = netPrice.multiply(new BigDecimal(quantity));
        BigDecimal grossValue = new BigDecimal("123.00");

        StringBuilder expectedDecorator = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            expectedDecorator.append(decor);
        }

        String generatedRows = printInvoice.generateRows(id, productName, quantity,
                netPrice, taxPercent, netValue, grossValue);

        StringBuilder expectedPrintingRows = new StringBuilder();
        expectedPrintingRows.append(generatedRows);
        expectedPrintingRows.append(expectedDecorator);

        String printingRowsValue = printInvoice.printRows(id, productName, quantity,
                netPrice, taxPercent, netValue, grossValue);

        Assert.assertEquals(expectedPrintingRows.toString(), printingRowsValue);
    }

    @Test
    public void testPrintingSummary() {

        Integer quantity = 10;
        BigDecimal taxPercent = new BigDecimal("0.23");
        BigDecimal summaryNetValue = new BigDecimal("100");
        BigDecimal summaryValueWithTax = summaryNetValue.multiply(taxPercent);

        StringBuilder expectedDecorator = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            expectedDecorator.append(decor);
        }

        String generateSummaryHeader = printInvoice.generateSummary(quantity, taxPercent,
                summaryNetValue, summaryValueWithTax);

        StringBuilder expectedPrintingSummary = new StringBuilder();
        expectedPrintingSummary.append(generateSummaryHeader);
        expectedPrintingSummary.append("\n");
        expectedPrintingSummary.append(expectedDecorator);

        String printingSummaryValue = printInvoice.printSummary(quantity, taxPercent,
                summaryNetValue, summaryValueWithTax);

        Assert.assertEquals(expectedPrintingSummary.toString(), printingSummaryValue);
    }

    @Test
    public void testPrintingSummaryHeader() {

        StringBuilder expectedDecorator = new StringBuilder();
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            expectedDecorator.append(decor);
        }

        String header = String.format("|%-28s | %5s | %14s | %22s | %22s |",
                " Podsumowanie pozycji", "Ilość",
                "Wartość Vat", "Wartość Netto", "Wartość brutto");

        String expectedEmptyRow = String.format("\n| %102s |\n", "");

        StringBuilder expectedSummaryHeader = new StringBuilder();
        expectedSummaryHeader.append("\n");
        expectedSummaryHeader.append(expectedDecorator);
        expectedSummaryHeader.append(expectedEmptyRow);
        expectedSummaryHeader.append(header);
        expectedSummaryHeader.append("\n");
        expectedSummaryHeader.append(expectedDecorator);
        expectedSummaryHeader.append(expectedEmptyRow);

        String printingSummaryHeader = printInvoice.printSummaryHeaders();

        Assert.assertEquals(expectedSummaryHeader.toString(), printingSummaryHeader);
    }
}
