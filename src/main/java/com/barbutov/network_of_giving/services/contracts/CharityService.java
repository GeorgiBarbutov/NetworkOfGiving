package com.barbutov.network_of_giving.services.contracts;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import javassist.NotFoundException;

import java.util.List;

public interface CharityService {
    List<CharityResponseDto> getAllCharitiesAsResponseDtos();
    Charity getCharityById(long id) throws NotFoundException;
    CharityResponseDto getCharityByName(String name) throws NotFoundException;
    CharityResponseDto getCharityByIdAsResponseDto(long id) throws NotFoundException;
    Charity getCharityByIdAndCreatorName(long id, String creatorName) throws NotFoundException;
    Charity addCharity(CharityRequestDto charityRequestDto, User user);
    void deleteCharity(long charityId) throws NotFoundException;
    void donateToCharity(Charity charity, double amount);
    void volunteerToCharity(Charity charity);
    void withholdFromCharity(Charity charity);
    List<CharityResponseDto> findAllVolunteeredToCharities(String username);
    List<CharityResponseDto> findAllDonatedToCharities(String username);
    List<CharityResponseDto> findAllCreatedCharities(String username);
    void editCharity(CharityRequestDto charityRequestDto, long id, String creatorName) throws NotFoundException;
}
