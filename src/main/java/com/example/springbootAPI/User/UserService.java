package com.example.springbootAPI.User;

import com.example.springbootAPI.User.dto.UserCreateRequest;
import com.example.springbootAPI.User.dto.UserPatchRequest;
import com.example.springbootAPI.User.dto.UserResponse;
import com.example.springbootAPI.User.dto.UserUpdateRequest;
import com.example.springbootAPI.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 esta


@Service
@Transactional(readOnly = true)
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

    public UserResponse getById(Long id) {
        UserEntity entity = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toResponse(entity);
    }

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

    public UserResponse update(Long id, UserUpdateRequest req) {
        UserEntity entity = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Valida email único excluyendo al propio id
        if (repo.existsByEmailIgnoreCaseAndIdNot(req.email(), id)) {
            throw new DuplicateEmailException(req.email());
        }

        entity.setName(req.name());
        entity.setEmail(req.email());
        // JPA dirty checking hará el UPDATE al commit; opcional: repo.save(entity);
        return toResponse(entity);
    }

    @Transactional
    public UserResponse patch(Long id, UserPatchRequest req) {
        UserEntity entity = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        boolean changed = false;

        if (req.name() != null) {
            if (req.name().isBlank()) {
                throw new IllegalArgumentException("name no puede estar vacío");
            }
            entity.setName(req.name());
            changed = true;
        }

        if (req.email() != null) {
            if (req.email().isBlank()) {
                throw new IllegalArgumentException("email no puede estar vacío");
            }
            // Solo valida unicidad si realmente cambia
            if (!req.email().equalsIgnoreCase(entity.getEmail())
                    && repo.existsByEmailIgnoreCase(req.email())) {
                throw new DuplicateEmailException(req.email());
            }
            entity.setEmail(req.email());
            changed = true;
        }

        if (!changed) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }

        return toResponse(entity);
    }

    public void delete(Long id){
        UserEntity entity = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        repo.delete(entity);
    }

    private UserResponse toResponse(UserEntity e) {
        return new UserResponse(e.getId(), e.getName(), e.getEmail());
    }
}
