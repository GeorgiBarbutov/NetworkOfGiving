package com.barbutov.network_of_giving.data.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CharityRepository extends CrudRepository<Charity, Long> {
    @Modifying
    @Query("Update Charity as c set c.volunteersCount = c.volunteersCount + 1 where c.id = ?1")
    @Transactional
    void increaseVolunteersCount(long charityId);

    @Modifying
    @Query("Update Charity as c set c.volunteersCount = c.volunteersCount - 1 where c.id = ?1")
    @Transactional
    void lowerVolunteersCount(long charityId);

    @Modifying
    @Query("Update Charity as c set c.collectedAmount = c.collectedAmount + ?2 where c.id = ?1")
    @Transactional
    void updateCollectedAmount(long charityId, double newAmount);

    @Query(nativeQuery = true, value = "SELECT c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id FROM Users AS u JOIN Donations AS d ON u.id = d.user_id JOIN Charities AS c ON c.id = d.charity_id WHERE username = ?1 GROUP BY c.id")
    List<Charity> findAllDonatedToCharities(String username);

    @Query(nativeQuery = true, value = "SELECT c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id FROM Users AS u JOIN Volunteers AS v ON u.id = v.user_id JOIN Charities AS c ON c.id = v.charity_id WHERE username = ?1")
    List<Charity> findAllVolunteeredToCharities(String username);

    @Query(nativeQuery = true, value = "SELECT c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id FROM Users AS u JOIN Charities AS c ON c.creator_id = u.id WHERE username = ?1")
    List<Charity> findAllCreatedCharities(String username);

    Optional<Charity> findByIdAndCreator(long id, User creator);

    @Query(nativeQuery = true, value = "SELECT u.username FROM Charities AS c JOIN Users AS u ON u.id = c.creator_id WHERE c.id = ?1")
    String getCreator(long charityId);

    Optional<Charity> findByName(String name);
}
