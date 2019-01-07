package org.sample.billing.service.notinstrumented.impl;

import org.sample.billing.model.Invoice;
import org.sample.billing.model.InvoiceState;
import org.sample.billing.model.LineItem;
import org.sample.billing.persistence.InvoiceRepository;
import org.sample.billing.service.InvoiceService;
import org.sample.billing.service.NotificationService;
import org.sample.billing.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    @Autowired
    @Qualifier("notificationServiceImpl")
    private NotificationService notifications;

    @Autowired
    @Qualifier("taxServiceImpl")
    private TaxService taxes;

    private static final Random random;

    static {
        random = new java.util.Random();
    }

    @Override
    public Long createInvoice(Invoice invoice) {
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setState(InvoiceState.DRAFT);

        invoice.setInvoiceNumber(generateInvoiceNumber());
        repository.persistInvoice(invoice);

        return invoice.getInvoiceNumber();
    }

    @Override
    public Invoice getInvoice(Long invoiceNumber) {
        return repository.getInvoice(invoiceNumber);
    }

    @Override
    public void addLineItem(Long invoiceNumber, LineItem item) {
        Invoice invoice = repository.getInvoice(invoiceNumber);

        item.setInvoiceNumber(invoiceNumber);
        //calculate total
        item.setTotal(item.getRate().multiply(new BigDecimal(
                item.getQuantity())));

        invoice.addLineItem(item);

        repository.persistInvoice(invoice);
    }

    @Override
    public void addLineItems(Long invoiceNumber, List<LineItem> items) {
        Invoice invoice = repository.getInvoice(invoiceNumber);

        //calculate total
        items.forEach((item) -> {
            item.setInvoiceNumber(invoiceNumber);
            item.setTotal(item.getRate().multiply(
                    new BigDecimal(item.getQuantity())));
        });

        invoice.addLineItems(items);

        repository.persistInvoice(invoice);
    }

    @Override
    public Invoice issueInvoice(Long invoiceNumber) {
        //Retrieve invoice
        Invoice invoice = repository.getInvoice(invoiceNumber);

        //compute taxes
        Invoice taxedInvoice = taxes.computeTaxes(invoice);

        //notify to customer
        Boolean notified = notifications.notifyCustomer(taxedInvoice);
        if (notified) {
            taxedInvoice.setNotified(Boolean.TRUE);
        }

        taxedInvoice.setState(InvoiceState.ISSUED);
        taxedInvoice.setInvoiceDate(LocalDateTime.now());
        taxedInvoice.setDueDate(LocalDate.now().plusDays(30));

        repository.persistInvoice(taxedInvoice);

        return taxedInvoice;
    }

    private static Long generateInvoiceNumber() {
        long min = 1000000000L;
        long max = 9999999999L;

        Long number = random.nextLong() % (max - min) + max;
        return number;
    }
}
