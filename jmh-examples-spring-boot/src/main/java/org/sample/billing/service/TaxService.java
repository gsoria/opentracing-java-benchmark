package org.sample.billing.service;

import org.sample.billing.model.Invoice;

public interface TaxService {

    Invoice computeTaxes(Invoice invoice);
}
