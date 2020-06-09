package com.barbutov.network_of_giving.services.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import javassist.NotFoundException;

import java.util.Optional;

public interface DonationService {
    void addDonation(Charity charity, double amount, User user);
    void removeAllDonationsByCharity(Charity charity);
}
