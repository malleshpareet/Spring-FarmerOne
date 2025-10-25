package com.FarmerOne.FarmerOne.util;

import com.FarmerOne.FarmerOne.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CustomIdGenerator implements InitializingBean {
    
    @Autowired
    private ProfileRepository profileRepository;
    
    private final AtomicLong idCounter = new AtomicLong(1);
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Find the maximum numeric ID in the existing data
        profileRepository.findAll().forEach(profile -> {
            try {
                if (profile.getId() != null) {
                    long id = Long.parseLong(profile.getId());
                    if (id >= idCounter.get()) {
                        idCounter.set(id + 1);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric IDs
            }
        });
    }
    
    public String generateNextId() {
        // For new installations, start with 1
        // For existing installations, find the max ID and increment
        return String.valueOf(idCounter.getAndIncrement());
    }
}