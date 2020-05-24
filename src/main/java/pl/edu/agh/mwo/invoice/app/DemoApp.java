package pl.edu.agh.mwo.invoice.app;

import java.math.BigDecimal;
import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.print.PrintInvoice;
import pl.edu.agh.mwo.invoice.product.Product;


public class DemoApp {
    public static void main(String[] args) {

        Product product1 = new Product("Puzzle 1000el", new BigDecimal("34.00"),
                new BigDecimal("0.23"));
        Product product2 = new Product("Talerz plastikowy", new BigDecimal("0.50"),
                new BigDecimal("0.08"));
        Product product3 = new Product("Gumowa kaczka", new BigDecimal("1.50"),
                new BigDecimal("0.23"));
        Product product4 = new Product("Woda 1.5L", new BigDecimal("1.20"),
                new BigDecimal("0.08"));
        Product product5 = new Product("Chleb", new BigDecimal("3.00"),
                new BigDecimal("0.08"));
        Product product6 = new Product("Mleko 1L", new BigDecimal("2.20"),
                new BigDecimal("0.08"));


        Invoice invoice1 = new Invoice();

        invoice1.addProduct(product1);
        invoice1.addProduct(product2);
        invoice1.addProduct(product3);
        invoice1.addProduct(product4);
        invoice1.addProduct(product5);
        invoice1.addProduct(product6);

        PrintInvoice printer = new PrintInvoice();
        System.out.println(printer.showInvoice(invoice1));



    }
}
