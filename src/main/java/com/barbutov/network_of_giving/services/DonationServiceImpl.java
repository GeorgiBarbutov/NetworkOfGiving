package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.contracts.DonationRepository;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.Donation;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.DonationService;
import com.barbutov.network_of_giving.util.Constants;
import org.springframework.stereotype.Service;

@Service
public class DonationServiceImpl implements DonationService {

    private DonationRepository donationRepository;

    public DonationServiceImpl(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Override
    public void addDonation(Charity charity, double amount, User user) {
        nullCharityCheck(charity);
        nullUserCheck(user);
        Donation newDonation = new Donation(amount, user, charity);

        this.donationRepository.save(newDonation);
    }

    @Override
    public void removeAllDonationsByCharity(Charity charity) {
        nullCharityCheck(charity);
        this.donationRepository.deleteAllByCharity(charity);
    }

    private void nullCharityCheck(Charity charity){
        if(charity == null){
            throw new IllegalArgumentException(Constants.NULL_CHARITY);
        }
    }

    private void nullUserCheck(User user){
        if(user == null){
            throw new IllegalArgumentException(Constants.NULL_USER);
        }
    }
}
