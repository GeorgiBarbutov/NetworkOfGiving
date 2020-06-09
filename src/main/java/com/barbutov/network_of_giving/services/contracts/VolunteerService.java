package com.barbutov.network_of_giving.services.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.data.models.Volunteer;

public interface VolunteerService {
    Volunteer addVolunteer(Charity charity, User user);
    void removeVolunteer(Charity charity, User user);
    boolean existsByCharityAndUser(Charity charity, User user);
    void removeAllVolunteersByCharity(Charity charity);
}
