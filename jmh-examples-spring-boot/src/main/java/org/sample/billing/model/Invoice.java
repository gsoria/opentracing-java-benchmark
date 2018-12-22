package org.sample.billing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {

    private Long invoiceNumber;
    private Customer customer;
    private LocalDateTime invoiceDate;
    private Integer accountNumber;
    private LocalDate dueDate;
    private Double amountDue;
    private Currency currency = Currency.USD;
    private Boolean notified;
    private InvoiceState state;
    private List lineItems = new ArrayList();

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List getLineItems() {
        return lineItems;
    }

    public void setLineItems(List lineItems) {
        this.lineItems = lineItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public InvoiceState getState() {
        return state;
    }

    public void setState(InvoiceState state) {
        this.state = state;
    }

    public void addLineItem(LineItem item){
        this.lineItems.add(item);
    }

    public void addLineItems(List<LineItem> items){
        this.lineItems.addAll(items);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber=" + invoiceNumber +
                ", customer=" + customer.getEmail() +
                ", invoiceDate=" + invoiceDate +
                ", accountNumber=" + accountNumber +
                ", dueDate=" + dueDate +
                ", amountDue=" + amountDue +
                ", currency=" + currency +
                ", notified=" + notified +
                ", state=" + state +
                '}';
    }
}
