package com.barbutov.network_of_giving.data.contracts;

import com.barbutov.network_of_giving.data.models.Charity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CharityRepository extends CrudRepository<Charity, Long> {
    @Modifying
    @Query("update Charity as c set c.volunteersCount = c.volunteersCount + 1 where c.id = ?1")
    @Transactional
    void increaseVolunteersCount(long charityId);

    @Modifying
    @Query("update Charity as c set c.volunteersCount = c.volunteersCount - 1 where c.id = ?1")
    @Transactional
    void lowerVolunteersCount(long charityId);

    @Modifying
    @Query("update Charity as c set c.collectedAmount = c.collectedAmount + ?2 where c.id = ?1")
    @Transactional
    void updateCollectedAmount(long charityId, double newAmount);

    @Query(nativeQuery = true, value = "select c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id from Users as u join Donations as d on u.id = d.user_id join Charities as c on c.id = d.charity_id where username = ?1 group by c.id")
    List<Charity> findAllDonatedToCharities(String username);

    @Query(nativeQuery = true, value = "select c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id from Users as u join Volunteers as v on u.id = v.user_id join Charities as c on c.id = v.charity_id where username = ?1")
    List<Charity> findAllVolunteeredToCharities(String username);

    @Query(nativeQuery = true, value = "select c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id from Users as u join Charities as c on c.creator_id = u.id where username = ?1")
    List<Charity> findAllCreatedCharities(String username);

    @Query(nativeQuery = true, value = "select c.id, collected_amount, budget_required, description, desired_participants, name, volunteers_count, creator_id from Users as u join Charities as c on c.creator_id = u.id where c.id = ?1 and username = ?2")
    Optional<Charity> findByIdAndCreatorName(long id, String creatorName);

    @Query(nativeQuery = true, value = "select u.username from Charities as c join Users as u on u.id = c.creator_id where c.id = ?1")
    String getCreator(long charityId);

    Optional<Charity> findByName(String name);

    @Modifying
    @Query("update Charity as c set c.budgetRequired = ?1, c.collectedAmount = ?2, c.description = ?3, c.desiredParticipants = ?4, c.name = ?5, c.volunteersCount = ?6 where c.id = ?7")
    @Transactional
    void updateCharity(double budgetRequired, double collectedAmount, String description, int desiredParticipants,
                       String name, int volunteersCount, long charityId);
}
