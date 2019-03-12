package io.opentracing.contrib.billing.service.impl;

import io.opentracing.contrib.billing.model.Currency;
import io.opentracing.contrib.billing.model.Invoice;
import io.opentracing.contrib.billing.model.InvoiceState;
import io.opentracing.contrib.billing.model.LineItem;
import io.opentracing.contrib.billing.service.TaxService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaxServiceImpl implements TaxService {

    private static final BigDecimal TAX_RATE_CAD = new BigDecimal(0.3);
    private static final BigDecimal TAX_RATE_USD = new BigDecimal(0.4);

    private static Map<Currency, BigDecimal> TAX_RATES = new HashMap<>();

    static {
        TAX_RATES.put(Currency.CAD, TAX_RATE_CAD);
        TAX_RATES.put(Currency.USD, TAX_RATE_USD);
    }

    @Override
    public Invoice computeTaxes(Invoice invoice) {
        //total
        List<LineItem> items = invoice.getLineItems();
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem item : items) {
            BigDecimal t = BigDecimal.ZERO;
            t = t.add(item.getTotal());
            total = t;
        }

        BigDecimal rate = TAX_RATES.get(invoice.getCurrency());
        BigDecimal taxes = total.multiply(rate);
        total = total.add(taxes);

        invoice.setAmountDue(total);
        invoice.setState(InvoiceState.TAXED);

        return invoice;
    }
}
