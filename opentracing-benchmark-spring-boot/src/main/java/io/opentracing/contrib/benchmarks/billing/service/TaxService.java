package io.opentracing.contrib.benchmarks.billing.service;

import io.opentracing.contrib.benchmarks.billing.model.Invoice;

public interface TaxService {

    Invoice computeTaxes(Invoice invoice);
}
