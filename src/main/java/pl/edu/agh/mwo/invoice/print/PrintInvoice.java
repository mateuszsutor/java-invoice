package pl.edu.agh.mwo.invoice.print;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.Product;


public class PrintInvoice {

    public static final int WIDTH_SIZE = 106;
    public static final String CURRENCY_POLAND = " Zł";


    public void printInvoice(Invoice invoice) {
        StringBuilder sb = new StringBuilder();

        printTitles(invoice);
        printHeaders();

        Map<Product, Integer> products = invoice.getProducts();
        int tempId = 1;
        Integer quantityAll = products.values().size();
        BigDecimal grossTotal = invoice.getGrossTotal()
                .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal taxTotal = invoice.getTaxTotal().setScale(2, RoundingMode.HALF_EVEN);

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {

            Product key = entry.getKey();
            Integer value = entry.getValue();
            BigDecimal priceWithOutTax = key.getPrice();

            BigDecimal taxToShow = key.getTaxPercent().multiply(new BigDecimal("100")).setScale(0);
            BigDecimal netValue = priceWithOutTax.multiply(new BigDecimal(value));
            BigDecimal tax = netValue.multiply(key.getTaxPercent());
            BigDecimal grossValue = netValue.add(tax).setScale(2, RoundingMode.HALF_EVEN);

            printRows(tempId, key.getName(),
                    value, key.getPrice(), taxToShow,
                    netValue, grossValue);
            tempId++;
        }
        printSummaryHeaders();
        printSummary(quantityAll, invoice.getNetTotal(), taxTotal, grossTotal);
        printSayGoodbye();
        System.out.println();
    }

    public void printTitles(Invoice invoice) {
        Date todayDate = new Date();
        System.out.println(decorator());
        printEmptyRow();
        System.out.printf("| %-44s  %44s %td-%<tb-%<tY |", "Faktura numer: "
                + invoice.getInvoiceNumber(), "Wystawiona dnia:", todayDate);
        printEmptyRow();
    }

    public void printHeaders() {
        decorator();
        System.out.printf("\n| %-4s | %-20s | %-5s | %14s | %8s | %16s |  %16s |\n",
                "Id", "Nazwa produtku", "Ilość", "Cena Netto szt",
                "Vat", "Wartość netto", "Wartość brutto");
        decorator();
    }

    public void printRows(int id, String productName, Integer quantity, BigDecimal netPrice,
                          BigDecimal taxPercent, BigDecimal netValue, BigDecimal grossValue) {
        System.out.printf("\n| %-4s | %-20s | %5d | %14s | %8s | %16s | %17s |\n",
                id, productName, quantity, netPrice + CURRENCY_POLAND, taxPercent + "%",
                netValue + CURRENCY_POLAND, grossValue + CURRENCY_POLAND);
        decorator();
    }

    public String decorator() {
        StringBuilder sb = new StringBuilder();
        char decor = '-';

        for (int i = 0; i < WIDTH_SIZE; i++) {
            sb.append(decor);
        }
        return sb.toString();
    }

    public void printEmptyRow() {
        System.out.printf("\n| %102s |\n", "");
    }

    public void printSummaryHeaders() {
        System.out.println();
        decorator();
        printEmptyRow();
        System.out.printf("|%-28s | %5s | %14s | %22s | %22s |\n",
                " Podsumowanie pozycji", "Ilość",
                "Wartość Vat", "Wartość Netto", "Wartość brutto");
        decorator();

    }

    public void printSummary(Integer quantity, BigDecimal taxPercent, BigDecimal summaryNetValue,
                             BigDecimal summaryValueWithTax) {

        System.out.printf("\n|%-28s | %5d | %14s | %22s | %22s |\n",
                "", quantity, summaryNetValue + CURRENCY_POLAND,
                taxPercent + CURRENCY_POLAND, summaryValueWithTax + CURRENCY_POLAND);
        decorator();
    }

    public void printSayGoodbye() {
        printEmptyRow();
        System.out.printf("| %102s |", "Dziękujemy za zakupy - zapraszamy ponownie");
        printEmptyRow();
        decorator();
    }
}
