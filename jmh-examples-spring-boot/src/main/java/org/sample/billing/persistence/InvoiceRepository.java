package org.sample.billing.persistence;

import org.sample.billing.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InvoiceRepository {

    private Map<Long, Invoice> invoices = new HashMap<>();

    public void persistInvoice(Invoice invoice) {
        this.invoices.put(invoice.getInvoiceNumber(), invoice);
    }

    public Invoice getInvoice(Long invoiceNumber) {
        return this.invoices.get(invoiceNumber);
    }

    public void reset(){
        this.invoices.clear();
    }
}
