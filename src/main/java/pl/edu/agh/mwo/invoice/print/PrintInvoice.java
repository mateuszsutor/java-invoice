package pl.edu.agh.mwo.invoice.print;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.Product;


public class PrintInvoice {

    public static final int WIDTH_SIZE = 86;
    public static final BigDecimal TEST_VALUE = new BigDecimal(100);
    public static final BigDecimal TEST_VALUE_VAT = new BigDecimal(23);
    public static final BigDecimal TEST_VALUE_VAT_PERCENT = new BigDecimal(0.23);

    public static void printInvoice(Invoice invoice) {
        Map<Product, Integer> products = invoice.getProducts();
        printTitles(invoice);
        printHeaders();
        int tempId = 1;
        Integer quantityAll = products.values().size();

        for (Product product : products.keySet()) {
            String productName = product.getName();
            int quantity = products.get(product);
            BigDecimal netPrice = product.getPrice().setScale(2, RoundingMode.HALF_UP);
            BigDecimal tax = product.getTaxPercent()
                    .setScale(2, RoundingMode.HALF_UP)
                    .multiply(TEST_VALUE);
            BigDecimal priceWithTax = invoice.getGrossTotal().setScale(2, RoundingMode.HALF_UP);
            BigDecimal grossTotal = priceWithTax.multiply(new BigDecimal(quantity));

            printRows(tempId, productName, quantity, netPrice, tax, priceWithTax);
            tempId++;
        }
        printSummaryHeaders();
        printSummary(quantityAll, TEST_VALUE, TEST_VALUE_VAT, TEST_VALUE);
        printSayGoodbye();
    }

    public static void printTitles(Invoice invoice) {
        Date todayDate = new Date();
        decorator();
        printEmptyRow();
        System.out.printf("| %-30s  %38s %td-%<tb-%<tY |", "Faktura numer: "
                + invoice.getInvoiceNumber(), "Wystawiona dnia:", todayDate);
        printEmptyRow();
    }

    public static void printHeaders() {
        decorator();
        System.out.printf("\n| %-4s | %-20s | %-5s | %14s | %8s | %16s |\n",
                "ID", "Nazwa produtku", "Ilość", "Cena Netto", "Vat", "Wartość brutto");
        decorator();
    }

    public static void printRows(int id, String productName, Integer quantity, BigDecimal netPrice,
                                 BigDecimal taxPercent, BigDecimal priceWithTax) {
        System.out.printf("\n| %-4s | %-20s | %5d | %14s | %8s | %16s |\n",
                id, productName, quantity, netPrice, taxPercent + "%", priceWithTax);
        decorator();
    }

    public static void decorator() {
        char decor = '-';
        for (int i = 0; i < WIDTH_SIZE; i++) {
            System.out.print(decor);
        }
    }

    public static void printEmptyRow() {
        System.out.printf("\n| %82s |\n", "");
    }

    public static void printSummaryHeaders() {
        System.out.println();
        decorator();
        printEmptyRow();
        System.out.printf("|%-28s | %5s | %14s | %8s | %16s |\n",
                " Podsumowanie pozycji", "Ilość", "Wartość Netto", "Vat", "Wartość brutto");
        decorator();

    }

    public static void printSummary(Integer quantity, BigDecimal summaryNetValue,
                                    BigDecimal taxPercent, BigDecimal summaryValueWithTax) {

        System.out.printf("\n|%-28s | %5d | %14s | %8s | %16s |\n",
                "", quantity, summaryNetValue, taxPercent + "%", summaryValueWithTax);
        decorator();
    }

    public static void printSayGoodbye() {
        printEmptyRow();
        System.out.printf("| %82s |", "Dziękujemy za zakupy - zapraszamy ponownie");
        printEmptyRow();
        decorator();
    }

    public static void main(String[] args) {

        Product product1 = new Product("zabawka", TEST_VALUE, TEST_VALUE_VAT_PERCENT);

        Product product2 = new Product("rowerek", TEST_VALUE, TEST_VALUE_VAT_PERCENT);

        Invoice invoice = new Invoice();

        invoice.addProduct(product1, 1);
        invoice.addProduct(product2, 1);

        printInvoice(invoice);

    }
}
