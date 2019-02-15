package org.sample.billing.service.traced;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.service.TaxService;
import org.sample.billing.service.impl.InvoiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TracedInvoiceService extends InvoiceServiceImpl {

    @Autowired
    private TracedNotificationService notificationService;

    @Autowired
    private TracedTaxService taxService;

    @Autowired
    public Tracer tracer;

    @Override
    public Long createInvoice(Invoice invoice) {
        try (Scope scope = tracer.buildSpan("createInvoice").startActive(true)) {
            scope.span().log("createInvoice");
            scope.span().setTag("customer", invoice.getCustomer().getEmail());
            scope.span().setBaggageItem("taxId",
                    invoice.getCustomer().getTaxId());
            return super.createInvoice(invoice);
        }
    }

    @Override
    public TracedNotificationService getNotificationService() {
        return notificationService;
    }

    @Override
    public TaxService getTaxesService() {
        return taxService;
    }
}
