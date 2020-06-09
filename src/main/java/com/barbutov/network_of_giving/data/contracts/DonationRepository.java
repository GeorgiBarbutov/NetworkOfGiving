package com.barbutov.network_of_giving.data.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.Donation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface DonationRepository extends CrudRepository<Donation, Long> {
    @Modifying
    @Transactional
    void deleteAllByCharity(Charity charity);
}
