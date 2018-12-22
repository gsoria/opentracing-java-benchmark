package org.sample.billing.service.impl;

import io.opentracing.Tracer;
import org.sample.billing.model.*;
import org.sample.billing.persistence.InvoiceBox;
import org.sample.billing.service.InvoiceService;
import org.sample.billing.service.NotificationService;
import org.sample.billing.service.TaxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceBox box;

    @Autowired
    private NotificationService notifications;

    @Autowired
    private TaxesService taxes;

    @Autowired
    @Qualifier("noopTracer")
    public Tracer tracer;

    @Override
    public Long createInvoice(Invoice invoice) {

        try (io.opentracing.Scope scope = tracer.buildSpan("createInvoice").startActive(true)) {
            System.out.println("creating invoice");
            invoice.setInvoiceDate(LocalDateTime.now());
            invoice.setDueDate(LocalDate.now().plusDays(30));
            invoice.setState(InvoiceState.DRAFT);

            invoice.setInvoiceNumber(getRandomNumber());
            box.persistInvoice(invoice);

            scope.span().log("createInvoice");
            scope.span().setTag("customer", invoice.getCustomer().getEmail());

            return invoice.getInvoiceNumber();
        }
    }

    @Override
    public void addLineItem(Long invoiceNumber, LineItem item) {
        //calculate total
        item.setTotal(item.getTotal()*item.getRate());

        Invoice invoice = box.getInvoice(invoiceNumber);
        invoice.addLineItem(item);
        box.persistInvoice(invoice);
    }

    @Override
    public void addLineItems(Long invoiceNumber, List<LineItem> items) {
        //calculate total
        items.forEach((i)-> i.setTotal(i.getTotal()*i.getRate()));

        Invoice invoice = box.getInvoice(invoiceNumber);
        invoice.addLineItems(items);
        box.persistInvoice(invoice);
    }

    @Override
    public Invoice issueInvoice(Invoice invoice) {
        //compute taxes
        Invoice taxedInvoice = taxes.computeTaxes(invoice);

        //notify to customer
        Boolean notified = notifications.notifyCustomer(taxedInvoice);
        if(notified){
            taxedInvoice.setNotified(Boolean.TRUE);
        }

        box.persistInvoice(taxedInvoice);
        IssueInvoice issuedInvoice = new IssueInvoice();
        issuedInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        issuedInvoice.setAccountNumber(invoice.getAccountNumber());
        issuedInvoice.setAmountDue(invoice.getAmountDue());
        issuedInvoice.setCurrency(invoice.getCurrency());
        issuedInvoice.setCustomer(invoice.getCustomer());
        issuedInvoice.setInvoiceDate(LocalDateTime.now());
        issuedInvoice.setState(InvoiceState.ISSUED);
        issuedInvoice.setDueDate(invoice.getDueDate());
        issuedInvoice.setLineItems(invoice.getLineItems());
        issuedInvoice.setNotified(invoice.getNotified());

        return issuedInvoice;
    }

    public static Long getRandomNumber(){

        long min = 1000000000L;
        long max = 9999999999L;

        Long number =  new java.util.Random().nextLong() % (max - min) + max;
        System.out.println("Random number " + number);
        return number;
    }
}
