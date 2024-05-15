package Web.Player.SoundBar.Repositories.Criterias;

import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.ViewLayers.Criterias.SongSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class SongCriteriaRepo {

    private final EntityManager entityManager;

    private final CriteriaBuilder criteriaBuilder;

    public SongCriteriaRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Song> findAllWithFilters(SongSearchCriteria songSearchCriteria, Pageable pageable) {
        CriteriaQuery<Song> criteriaQuery = criteriaBuilder.createQuery(Song.class);
        Root<Song> songRoot = criteriaQuery.from(Song.class);

        Predicate predicate = getPredicate(songSearchCriteria, songRoot);

        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(songRoot.get("title")));

        TypedQuery<Song> typedQuery = entityManager.createQuery(criteriaQuery);

        return new PageImpl<>(typedQuery.getResultList(), pageable, getSongCount(predicate));
    }

    private Predicate getPredicate(SongSearchCriteria songSearchCriteria, Root<Song> songRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(songSearchCriteria.getTitle())) {
            String titleInLowerCase = songSearchCriteria.getTitle().toLowerCase();

            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(songRoot.get("title")),
                            "%" + titleInLowerCase + "%")
            );
        }

        if (Objects.nonNull(songSearchCriteria.getGenre())) {
            predicates.add(songRoot.get("genre").in(songSearchCriteria.getGenre()));
        }

        if (Objects.nonNull(songSearchCriteria.getArtistNickname())) {
            String artistNicknameInLowerCase = songSearchCriteria.getArtistNickname().toLowerCase();

            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(songRoot.get("artist").get("nickname")),
                            "%" + artistNicknameInLowerCase + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

//    private void setOrder(Pageable pageable, CriteriaQuery<Song> criteriaQuery, Root<Song> songRoot) {
//        if (pageable.getSort().isSorted()) {
//            List<Sort.Order> orders = pageable.getSort().stream()
//                    .collect(Collectors.toList());
//
//            List<Order> jpaOrders = orders.stream()
//                    .map(order -> order.isAscending()
//                            ? criteriaBuilder.asc(songRoot.get(order.getProperty()))
//                            : criteriaBuilder.desc(songRoot.get(order.getProperty())))
//                    .collect(Collectors.toList());
//            criteriaQuery.orderBy(jpaOrders);
//        }
//    }

    private long getSongCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Song> countRoot = countQuery.from(Song.class);

        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}