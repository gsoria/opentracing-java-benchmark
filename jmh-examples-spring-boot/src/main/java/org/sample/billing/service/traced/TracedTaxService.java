package org.sample.billing.service.traced;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.service.impl.TaxServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tracedTaxService")
public class TracedTaxService extends TaxServiceImpl {

    @Autowired
    public Tracer tracer;

    @Override
    public Invoice computeTaxes(Invoice invoice) {
        try (Scope scope = tracer.buildSpan("computeTaxes")
                .startActive(true)) {

            Invoice computedInvoice =  super.computeTaxes(invoice);

            String taxId = scope.span().getBaggageItem("taxId");

            scope.span().setTag("currency", computedInvoice.getCurrency().toString());
            scope.span().log("computeTaxes");
            scope.span().setTag("total", computedInvoice.getAmountDue());
            scope.span().setTag("customer taxId", taxId);

            return computedInvoice;
        }
    }
}
