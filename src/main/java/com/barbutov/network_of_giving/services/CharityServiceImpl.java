package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.data.contracts.CharityRepository;
import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.util.contracts.CharityValidator;
import com.barbutov.network_of_giving.util.Constants;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharityServiceImpl implements CharityService {
    private static final String CHARITY_NOT_FOUND = "Charity Not Found";

    private final CharityRepository charityRepository;
    private final CharityValidator charityValidator;

    @Autowired
    public CharityServiceImpl(CharityRepository charityRepository, CharityValidator charityValidator) {
        this.charityRepository = charityRepository;
        this.charityValidator = charityValidator;
    }

    @Override
    public List<CharityResponseDto> getAllCharitiesAsResponseDtos() {
        List<Charity> charities = (List<Charity>) this.charityRepository.findAll();

        List<CharityResponseDto> charityResponseDtos = new ArrayList<>();
        for (Charity charity : charities) {
            String creatorName = this.charityRepository.getCreator(charity.getId());
            CharityResponseDto dto = mapCharityToCharityResponseDto(charity, creatorName);

            charityResponseDtos.add(dto);
        }

        return charityResponseDtos;
    }

    @Override
    public CharityResponseDto getCharityByIdAsResponseDto(long id) throws NotFoundException {
        Optional<Charity> optional = this.charityRepository.findById(id);

        if(optional.isPresent()){
            Charity charity = optional.get();
            String creatorName = this.charityRepository.getCreator(charity.getId());

            return mapCharityToCharityResponseDto(charity, creatorName);
        } else {
            throw new NotFoundException(CHARITY_NOT_FOUND);
        }
    }

    @Override
    public Charity getCharityById(long id) throws NotFoundException {
        Optional<Charity> optional = this.charityRepository.findById(id);

        return optionalCheck(optional);
    }

    @Override
    public CharityResponseDto getCharityByName(String name) throws NotFoundException {
        Optional<Charity> optional = this.charityRepository.findByName(name);

        if(optional.isPresent()){
            Charity charity = optional.get();
            String creatorName = this.charityRepository.getCreator(charity.getId());

            return mapCharityToCharityResponseDto(charity, creatorName);
        } else {
            throw new NotFoundException(CHARITY_NOT_FOUND);
        }
    }

    private CharityResponseDto mapCharityToCharityResponseDto(Charity charity, String creatorName){
        return new CharityResponseDto(charity.getId(), charity.getName(),
                charity.getDescription(), charity.getDesiredParticipants(), charity.getBudgetRequired(),
                charity.getVolunteersCount(), charity.getCollectedAmount(), creatorName);
    }

    @Override
    public Charity getCharityByIdAndCreator(long id, User creator) throws NotFoundException {
        Optional<Charity> optional = this.charityRepository.findByIdAndCreator(id, creator);

        return optionalCheck(optional);
    }

    @Override
    public Charity addCharity(CharityRequestDto charityRequestDto, User user) {
        String validation = this.charityValidator.validateCharityDto(charityRequestDto);

        if(!validation.equals(Constants.VALID)){
            throw new IllegalArgumentException(validation);
        }

        Charity charity = new Charity(charityRequestDto.getName(), charityRequestDto.getDescription(),
                charityRequestDto.getDesiredParticipants(), charityRequestDto.getBudgetRequired(), 0, 0,
                user);

        return this.charityRepository.save(charity);
    }

    @Override
    public void deleteCharity(long charityId) throws NotFoundException {
        if(this.charityRepository.existsById(charityId)){
            this.charityRepository.deleteById(charityId);
        } else {
            throw new NotFoundException(CHARITY_NOT_FOUND);
        }
    }

    @Override
    public void donateToCharity(Charity charity, double amount){
        this.charityRepository.updateCollectedAmount(charity.getId(), amount);
    }

    @Override
    public void volunteerToCharity(Charity charity) {
        this.charityRepository.increaseVolunteersCount(charity.getId());
    }

    @Override
    public void withholdFromCharity(Charity charity) {
        this.charityRepository.lowerVolunteersCount(charity.getId());
    }

    @Override
    public List<CharityResponseDto> findAllVolunteeredToCharities(String username) {
        List<Charity> charities = this.charityRepository.findAllVolunteeredToCharities(username);
        List<CharityResponseDto> charityResponseDtos = new ArrayList<>();
        for(Charity charity : charities){
            charityResponseDtos.add(mapCharityToCharityResponseDto(charity, username));
        }

        return charityResponseDtos;
    }

    @Override
    public List<CharityResponseDto> findAllDonatedToCharities(String username) {
        List<Charity> charities = this.charityRepository.findAllDonatedToCharities(username);

        List<CharityResponseDto> charityResponseDtos = new ArrayList<>();
        for(Charity charity : charities){
            charityResponseDtos.add(mapCharityToCharityResponseDto(charity, username));
        }

        return charityResponseDtos;
    }

    @Override
    public List<CharityResponseDto> findAllCreatedCharities(String username) {
        List<Charity> charities = this.charityRepository.findAllCreatedCharities(username);

        List<CharityResponseDto> charityResponseDtos = new ArrayList<>();
        for(Charity charity : charities){
            charityResponseDtos.add(mapCharityToCharityResponseDto(charity, username));
        }

        return charityResponseDtos;
    }

    private Charity optionalCheck(Optional<Charity> optional) throws NotFoundException {
        if(optional.isPresent()){
            return optional.get();
        } else {
            throw new NotFoundException(CHARITY_NOT_FOUND);
        }
    }
}
