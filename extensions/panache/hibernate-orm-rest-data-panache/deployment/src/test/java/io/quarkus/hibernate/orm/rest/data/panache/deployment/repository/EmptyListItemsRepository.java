package io.quarkus.hibernate.orm.rest.data.panache.deployment.repository;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EmptyListItemsRepository implements PanacheRepository<EmptyListItem> {
}
