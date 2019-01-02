package org.sample.billing.service.jaegertracer.impl;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.sample.billing.model.Currency;
import org.sample.billing.model.Invoice;
import org.sample.billing.model.InvoiceState;
import org.sample.billing.model.LineItem;
import org.sample.billing.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaxJaegerTracerServiceImpl implements TaxService {

    @Autowired
    @Qualifier("jaegerTracer")
    public Tracer tracer;

    private static final BigDecimal TAX_RATE_CAD = new BigDecimal(0.3);
    private static final BigDecimal TAX_RATE_USD = new BigDecimal(0.4);

    private static Map<Currency, BigDecimal> TAX_RATES = new HashMap<>();

    static {
        TAX_RATES.put(Currency.CAD, TAX_RATE_CAD);
        TAX_RATES.put(Currency.USD, TAX_RATE_USD);
    }

    @Override
    public Invoice computeTaxes(Invoice invoice) {

        try (Scope scope = tracer
                .buildSpan("computeTaxes")
                .startActive(true)) {

            //total
            List<LineItem> items = invoice.getLineItems();
            BigDecimal total = BigDecimal.ZERO;
            for (LineItem item : items) {
                total = total.add(item.getTotal());
            }

            BigDecimal rate = TAX_RATES.get(invoice.getCurrency());
            BigDecimal taxes = total.multiply(rate);
            total = total.add(taxes);

            invoice.setAmountDue(total);
            invoice.setState(InvoiceState.TAXED);

            String taxId = scope.span().getBaggageItem("taxId");

            scope.span().setTag("currency", invoice.getCurrency().toString());
            scope.span().log("computeTaxes");
            scope.span().setTag("total", total);
            scope.span().setTag("customer taxId", taxId);

            return invoice;
        }
    }
}
