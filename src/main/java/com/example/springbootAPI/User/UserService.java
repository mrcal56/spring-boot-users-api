package com.example.springbootAPI.User;

import com.example.springbootAPI.User.dto.UserCreateRequest;
import com.example.springbootAPI.User.dto.UserResponse;
import com.example.springbootAPI.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 esta


@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (repo.existsByEmailIgnoreCase(req.email())) {
            throw new DuplicateEmailException(req.email());
        }
        UserEntity saved = repo.save(new UserEntity(req.name(), req.email()));
        return toResponse(saved);
    }

    // ✅ leer por id
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        UserEntity entity = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toResponse(entity);
    }

    // ✅ listar con paginación
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> list(int page, int size) {
        page = Math.max(page, 0);
        size = Math.max(1, Math.min(size, 100));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<UserEntity> result = repo.findAll(pageable);

        return new PageResponse<>(
                result.map(this::toResponse).getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    private UserResponse toResponse(UserEntity e) {
        return new UserResponse(e.getId(), e.getName(), e.getEmail());
    }
}
