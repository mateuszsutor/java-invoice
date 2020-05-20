package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product implements Comparable<Product> {

    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    public Product(String name, BigDecimal price, BigDecimal tax) {
        if (name == null || name.equals("") || price == null
                || tax == null || tax.compareTo(new BigDecimal(0)) < 0
                || price.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(name, product.name)
                && Objects.equals(price, product.price)
                && Objects.equals(taxPercent, product.taxPercent);
    }

    @Override
    public String toString() {
        BigDecimal multiplierValue = new BigDecimal("100");
        return "Product name: " + name
                + ", cena:" + price
                + ", Vat:"
                + (taxPercent.setScale(2, RoundingMode.HALF_DOWN).multiply(multiplierValue));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, taxPercent);
    }

    @Override
    public int compareTo(Product product) {
        return this.name.compareTo(product.name);
    }
}
