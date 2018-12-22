package org.sample.billing.service;

import org.sample.billing.model.Invoice;

public interface TaxesService {

    Invoice computeTaxes(Invoice invoice);
}
