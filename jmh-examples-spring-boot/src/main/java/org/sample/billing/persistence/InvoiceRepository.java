package org.sample.billing.persistence;

import org.sample.billing.model.Invoice;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InvoiceRepository {

    private ConcurrentHashMap<Long, Invoice> invoices =
            new ConcurrentHashMap<>(65536);

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
