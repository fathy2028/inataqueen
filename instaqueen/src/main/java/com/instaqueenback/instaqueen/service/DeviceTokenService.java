package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.DeviceTokenRequest;
import com.instaqueenback.instaqueen.entity.DeviceToken;
import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.DeviceTokenRepository;
import com.instaqueenback.instaqueen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public void registerToken(UUID userId, DeviceTokenRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (deviceTokenRepository.findByToken(request.getToken()).isEmpty()) {
            DeviceToken token = DeviceToken.builder()
                    .user(user).token(request.getToken()).platform(request.getPlatform()).build();
            deviceTokenRepository.save(token);
        }
    }

    @Transactional
    public void removeToken(String token) {
        deviceTokenRepository.deleteByToken(token);
    }
}
