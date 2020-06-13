package com.barbutov.network_of_giving.services.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;

public interface VolunteerService {
    void addVolunteer(Charity charity, User user);
    void removeVolunteer(Charity charity, User user);
    boolean existsByCharityAndUser(Charity charity, User user);
    boolean existsByCharityIdAndUsername(long charityId, String username);
    void removeAllVolunteersByCharity(Charity charity);
}
