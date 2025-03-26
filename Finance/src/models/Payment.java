package models;

public class Payment {
    private Long id;
    private Double amount;
    private String paymentDate;
    private String description;
    private String paymentSource;
    private Long invoiceId;

    // Constructeurs
    public Payment() {}

    public Payment(Long id, Double amount, String paymentDate, String description, String paymentSource, Long invoiceId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.description = description;
        this.paymentSource = paymentSource;
        this.invoiceId = invoiceId;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
} 