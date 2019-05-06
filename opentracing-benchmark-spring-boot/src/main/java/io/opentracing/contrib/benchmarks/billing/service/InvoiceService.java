package io.opentracing.contrib.benchmarks.billing.service;

import io.opentracing.contrib.benchmarks.billing.model.Invoice;
import io.opentracing.contrib.benchmarks.billing.model.LineItem;

import java.util.List;

public interface InvoiceService {

    Long createInvoice(Invoice invoice);
    Invoice getInvoice(Long invoiceNumber);
    void addLineItem(Long invoiceNumber, LineItem item);
    void addLineItems(Long invoiceNumber, List<LineItem> items);
    Invoice issueInvoice(Long invoiceNumber);

}
