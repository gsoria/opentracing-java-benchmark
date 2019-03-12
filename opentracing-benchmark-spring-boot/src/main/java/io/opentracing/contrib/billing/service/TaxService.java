package io.opentracing.contrib.billing.service;

import io.opentracing.contrib.billing.model.Invoice;

public interface TaxService {

    Invoice computeTaxes(Invoice invoice);
}
