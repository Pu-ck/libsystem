package com.system.libsystem.rest.administration;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CommonAdminPanelEntitySearch<R extends JpaRepository<E, Long>, E> {

    private final R repository;

    protected List<E> getAdminPanelEntities() {
        return repository.findAll();
    }

    protected List<E> getAdminPanelEntityById(Long id, RuntimeException exception) {
        if (id == null) {
            return getAdminPanelEntities();
        }
        final List<E> entities = new ArrayList<>();
        final E entity = repository.findById(id).orElseThrow(() -> exception);
        entities.add(entity);
        return entities;
    }

}
