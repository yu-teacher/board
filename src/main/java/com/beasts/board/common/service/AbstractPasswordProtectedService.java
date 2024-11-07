package com.beasts.board.common.service;

import com.beasts.board.common.entity.PasswordProtectedEntity;
import com.beasts.board.common.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public abstract class AbstractPasswordProtectedService<T extends PasswordProtectedEntity, ID>
        implements PasswordProtectedService<T, ID> {

    protected final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}
