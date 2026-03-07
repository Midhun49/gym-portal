package com.gymportal.service;

import com.gymportal.entity.Membership;
import com.gymportal.entity.User;
import com.gymportal.repository.MembershipRepository;
import com.gymportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    public Membership createDefaultMembership(User user) {
        Membership m = new Membership();
        m.setUser(user);
        m.setPlan(Membership.Plan.BASIC);
        m.setStartDate(LocalDate.now());
        m.setEndDate(LocalDate.now().plusMonths(1));
        m.setStatus(Membership.Status.ACTIVE);
        m.setAmountPaid(Membership.Plan.BASIC.getPrice());
        return membershipRepository.save(m);
    }

    public Optional<Membership> getMembership(long userId) {
        return membershipRepository.findByUserId(userId);
    }

    public Membership upgradeMembership(long userId, Membership.Plan newPlan) {
        Membership m = membershipRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Membership newM = new Membership();
                    newM.setUser(user);
                    return newM;
                });

        m.setPlan(newPlan);
        m.setStartDate(LocalDate.now());
        m.setEndDate(LocalDate.now().plusMonths(1));
        m.setStatus(Membership.Status.ACTIVE);
        m.setAmountPaid(newPlan.getPrice());
        return membershipRepository.save(m);
    }
}
