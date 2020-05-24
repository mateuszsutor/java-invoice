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


    public String showInvoice(Invoice invoice) {
        Map<Product, Integer> products = invoice.getProducts();

        StringBuilder invoiceTemplate = new StringBuilder();

        invoiceTemplate.append(printTitles(invoice));
        invoiceTemplate.append(printHeaders());
        invoiceTemplate.append(generateRowsWithPosition(invoice));
        invoiceTemplate.append(printSummaryHeaders());
        invoiceTemplate.append(printSummary(invoice, products));
        invoiceTemplate.append(printSayGoodbye());

        return invoiceTemplate.toString();
    }

    public String generateRowsWithPosition(Invoice invoice) {

        StringBuilder tempRows = new StringBuilder();
        Map<Product, Integer> products = invoice.getProducts();
        int tempId = 1;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {

            Product key = entry.getKey();
            Integer value = entry.getValue();
            BigDecimal priceWithOutTax = key.getPrice();

            BigDecimal taxToShow = key.getTaxPercent().multiply(new BigDecimal("100")).setScale(0);
            BigDecimal netValue = priceWithOutTax.multiply(new BigDecimal(value));
            BigDecimal tax = netValue.multiply(key.getTaxPercent());
            BigDecimal grossValue = netValue.add(tax).setScale(2, RoundingMode.HALF_EVEN);


            tempRows.append(printRows(tempId, key.getName(),
                    value, key.getPrice(), taxToShow,
                    netValue, grossValue));
            tempId++;
        }

        return tempRows.toString();
    }

    public String generateTitle(Invoice invoice) {
        Date todayDate = new Date();

        String titlesTemplate = String.format("| %-44s  %44s %td-%<tb-%<tY |", "Faktura numer: "
                + invoice.getInvoiceNumber(), "Wystawiona dnia:", todayDate);

        return titlesTemplate;
    }


    public String printTitles(Invoice invoice) {
        StringBuilder titlesSection = new StringBuilder();
        titlesSection.append(decorator());
        titlesSection.append(printEmptyRow());
        titlesSection.append(generateTitle(invoice));
        titlesSection.append(printEmptyRow());
        titlesSection.append(decorator());
        titlesSection.append("\n");

        return titlesSection.toString();
    }

    public String generateHeader() {
        String header = String.format("| %-4s | %-20s | %-5s | %14s | %8s | %16s |  %16s |",
                "Id", "Nazwa produtku", "Ilość", "Cena Netto szt",
                "Vat", "Wartość netto", "Wartość brutto");

        return header;
    }

    public String printHeaders() {

        StringBuilder headers = new StringBuilder();
        headers.append(decorator());
        headers.append("\n");
        headers.append(generateHeader());
        headers.append("\n");
        headers.append(decorator());

        return headers.toString();

    }

    public String generateRows(int id, String productName, Integer quantity, BigDecimal netPrice,
                               BigDecimal taxPercent, BigDecimal netValue, BigDecimal grossValue) {

        String rowsTemplate = String.format("\n| %-4s | %-20s | %5d | %14s | %8s | %16s | %17s |\n",
                id, productName, quantity, netPrice + CURRENCY_POLAND, taxPercent + "%",
                netValue + CURRENCY_POLAND, grossValue + CURRENCY_POLAND);

        return rowsTemplate;

    }

    public String printRows(int id, String productName, Integer quantity,
                            BigDecimal netPrice, BigDecimal taxPercent,
                            BigDecimal netValue, BigDecimal grossValue) {

        StringBuilder rows = new StringBuilder();

        rows.append(generateRows(id, productName, quantity,
                netPrice, taxPercent, netValue, grossValue));

        rows.append(decorator());

        return rows.toString();
    }

    public String decorator() {
        StringBuilder sb = new StringBuilder();
        char decor = '-';

        for (int i = 0; i < WIDTH_SIZE; i++) {
            sb.append(decor);
        }
        return sb.toString();
    }

    public String printEmptyRow() {
        String emptyRow = String.format("\n| %102s |\n", "");
        return emptyRow;
    }

    public String generateSummaryHeader() {
        String summaryHeader = String.format("|%-28s | %5s | %14s | %22s | %22s |",
                " Podsumowanie pozycji", "Ilość",
                "Wartość Vat", "Wartość Netto", "Wartość brutto");

        return summaryHeader;
    }

    public String printSummaryHeaders() {

        StringBuilder summaryHeader = new StringBuilder();
        summaryHeader.append("\n");
        summaryHeader.append(decorator());
        summaryHeader.append(printEmptyRow());
        summaryHeader.append(generateSummaryHeader());
        summaryHeader.append("\n");
        summaryHeader.append(decorator());
        summaryHeader.append(printEmptyRow());

        return summaryHeader.toString();

    }

    public String generateSummary(Invoice invoice, Map<Product, Integer> products) {

        Integer quantityAll = products.values().size();
        BigDecimal grossTotal = invoice.getGrossTotal()
                .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal netTotal = invoice.getNetTotal()
                .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal taxTotal = invoice.getTaxTotal().setScale(2, RoundingMode.HALF_EVEN);

        String summary = String.format("|%-28s | %5d | %14s | %22s | %22s |",
                "", quantityAll, taxTotal.setScale(2) + CURRENCY_POLAND,
                netTotal.setScale(2) + CURRENCY_POLAND,
                grossTotal.setScale(2) + CURRENCY_POLAND);

        return summary;
    }

    public String printSummary(Invoice invoice, Map<Product, Integer> products) {

        StringBuilder summary = new StringBuilder();
        summary.append(generateSummary(invoice, products));
        summary.append("\n");
        summary.append(decorator());

        return summary.toString();
    }

    public String generateSayGoodbye() {
        String sayGoodbyeTemplate = String.format("| %102s |",
                "Dziękujemy za zakupy - zapraszamy ponownie");

        return sayGoodbyeTemplate;
    }

    public String printSayGoodbye() {
        StringBuilder sayGoodbye = new StringBuilder();
        sayGoodbye.append(printEmptyRow());
        sayGoodbye.append(generateSayGoodbye());
        sayGoodbye.append(printEmptyRow());
        sayGoodbye.append(decorator());

        return sayGoodbye.toString();
    }
}
