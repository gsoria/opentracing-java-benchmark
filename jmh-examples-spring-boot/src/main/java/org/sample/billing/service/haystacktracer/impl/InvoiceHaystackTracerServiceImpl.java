package org.sample.billing.service.haystacktracer.impl;

import io.opentracing.Scope;
import io.opentracing.Tracer;
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
public class InvoiceHaystackTracerServiceImpl implements InvoiceService {
    // TODO: I think you can have the following classes instead:
    // InvoiceServiceImpl, with the "business" code
    // abstract TracedInvoiceService extending the InvoiceServiceImpl, wrapping the calls in spans, with an abstract getTracer() method
    // final JaegerInvoiceService extending TracedInvoiceService, implementing the getTracer() method
    // final HaystackInvoiceService extending TracedInvoiceService, implementing the getTracer() method
    // this way, your "business" code is the same for all tests, avoiding code duplication

    @Autowired
    private InvoiceRepository repository;

    @Autowired
    @Qualifier("notificationHaystackTracerServiceImpl")
    private NotificationService notifications;

    @Autowired
    @Qualifier("taxHaystackTracerServiceImpl")
    private TaxService taxes;

    @Autowired
    @Qualifier("haystackTracer")
    public Tracer tracer;

    // TODO: try to avoid this random as well
    private static final Random random;

    static {
        random = new Random();
    }

    @Override
    public Long createInvoice(Invoice invoice) {
        try (Scope scope = tracer.buildSpan("createInvoice").startActive(true)) {
            invoice.setInvoiceDate(LocalDateTime.now());
            invoice.setState(InvoiceState.DRAFT);

            invoice.setInvoiceNumber(generateInvoiceNumber());
            repository.persistInvoice(invoice);

            scope.span().log("createInvoice");
            scope.span().setTag("customer", invoice.getCustomer().getEmail());
            scope.span().setBaggageItem("taxId", invoice.getCustomer().getTaxId());

            return invoice.getInvoiceNumber();
        }
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
        item.setTotal(item.getRate().multiply(new BigDecimal(item.getQuantity())));

        invoice.addLineItem(item);

        repository.persistInvoice(invoice);
    }

    @Override
    public void addLineItems(Long invoiceNumber, List<LineItem> items) {
        Invoice invoice = repository.getInvoice(invoiceNumber);

        //calculate total
        items.forEach((item) -> {
            item.setInvoiceNumber(invoiceNumber);
            item.setTotal(item.getRate().multiply(new BigDecimal(item.getQuantity())));
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
        taxedInvoice.setNotified(notifications.notifyCustomer(taxedInvoice));

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
