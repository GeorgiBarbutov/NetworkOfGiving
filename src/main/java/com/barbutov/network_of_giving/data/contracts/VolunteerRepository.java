package com.barbutov.network_of_giving.data.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.data.models.Volunteer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface VolunteerRepository extends CrudRepository<Volunteer, Long> {
    Optional<Volunteer> findByCharityAndUser(Charity charity, User user);

    @Modifying
    @Query("DELETE FROM Volunteer WHERE charity_id = ?1 AND user_id = ?2")
    @Transactional
    void deleteByCharityAndUser(long charityId, long userId);

    @Modifying
    @Transactional
    void deleteAllByCharity(Charity charity);
}
