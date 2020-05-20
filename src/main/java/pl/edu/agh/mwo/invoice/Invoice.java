package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    private int invoiceNumber;

    private static int nextNumber = 1;

    private Map<Product, Integer> products = new TreeMap<>();

    public Invoice() {
        this.invoiceNumber = nextNumber;
        nextNumber++;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void containsProduct(Product product, Map<Product, Integer> products) {
        int count = 0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

}
