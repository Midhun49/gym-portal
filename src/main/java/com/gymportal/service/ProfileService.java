package com.gymportal.service;

import com.gymportal.entity.MemberProfile;
import com.gymportal.entity.User;
import com.gymportal.dto.ProfileRequest;
import com.gymportal.repository.MemberProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private MemberProfileRepository profileRepository;

    public MemberProfile saveOrUpdate(User user, ProfileRequest req) {
        MemberProfile profile = profileRepository.findByUserId(user.getId())
                .orElse(new MemberProfile());
        profile.setUser(user);
        profile.setFullName(req.getFullName());
        profile.setAge(req.getAge());
        profile.setGender(req.getGender());
        profile.setHeightCm(req.getHeightCm());
        profile.setWeightKg(req.getWeightKg());
        profile.setFitnessGoal(req.getFitnessGoal());
        profile.setActivityLevel(req.getActivityLevel());
        profile.setDietType(req.getDietType());
        profile.setPhoneNumber(req.getPhoneNumber());
        profile.setAddress(req.getAddress());
        return profileRepository.save(profile);
    }

    public Optional<MemberProfile> getProfile(long userId) {
        return profileRepository.findByUserId(userId);
    }
}
