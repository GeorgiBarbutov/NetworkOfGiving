package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.contracts.VolunteerRepository;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.data.models.Volunteer;
import com.barbutov.network_of_giving.services.contracts.VolunteerService;
import com.barbutov.network_of_giving.util.Constants;
import org.springframework.stereotype.Service;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public void addVolunteer(Charity charity, User user) {
        nullCharityCheck(charity);
        nullUserCheck(user);

        Volunteer newVolunteer = new Volunteer(user, charity);

        this.volunteerRepository.save(newVolunteer);
    }

    @Override
    public void removeVolunteer(Charity charity, User user) {
        nullCharityCheck(charity);
        nullUserCheck(user);

        this.volunteerRepository.deleteByCharityAndUser(charity.getId(), user.getId());
    }

    @Override
    public boolean existsByCharityAndUser(Charity charity, User user) {
        nullCharityCheck(charity);
        nullUserCheck(user);

        return this.volunteerRepository.findByCharityAndUser(charity, user).isPresent();
    }

    @Override
    public boolean existsByCharityIdAndUsername(long charityId, String username) {
        return this.volunteerRepository.findByCharityIdAndUsername(charityId, username).isPresent();
    }

    @Override
    public void removeAllVolunteersByCharity(Charity charity) {
        nullCharityCheck(charity);

        this.volunteerRepository.deleteAllByCharity(charity);
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
