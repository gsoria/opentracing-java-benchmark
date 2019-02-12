package org.sample.billing.service;

import org.sample.billing.model.Invoice;

public interface NotificationService {

    Boolean notifyCustomer(Invoice invoice);

}
