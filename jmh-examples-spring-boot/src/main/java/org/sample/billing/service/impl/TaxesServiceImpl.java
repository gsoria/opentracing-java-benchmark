package org.sample.billing.service.impl;

import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.model.LineItem;
import org.sample.billing.service.TaxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxesServiceImpl implements TaxesService {

    @Autowired
    @Qualifier("noopTracer")
    public Tracer tracer;

    @Override
    public Invoice computeTaxes(Invoice invoice) {

        try (io.opentracing.Scope scope = tracer.buildSpan("computeTaxes").startActive(true)) {

            //total
            List<LineItem> items = invoice.getLineItems();
            Double total = new Double(0);
            for (LineItem item : items) {
                total = total + item.getTotal();
            }

            switch (invoice.getCurrency()) {
                case CAD:
                    total = total + total * 0.3;
                    scope.span().setTag("currency", "CAD");
                    break;
                case USD:
                    total = total + total * 0.4;
                    scope.span().setTag("currency", "USD");
                    break;
            }

            invoice.setAmountDue(total);

            scope.span().log("computeTaxes");
            scope.span().setTag("total", total);

            return invoice;
        }
    }
}
