package Web.Player.SoundBar.Repositories.Criterias;

import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Domains.Criterias.SongPage;
import Web.Player.SoundBar.Domains.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.Enums.SongGenres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Song> findAllWithFilters(SongPage songPage, SongSearchCriteria songSearchCriteria) {

        CriteriaQuery<Song> criteriaQuery = criteriaBuilder.createQuery(Song.class);
        Root<Song> songRoot = criteriaQuery.from(Song.class);

        Predicate predicate = getPredicate(songSearchCriteria, songRoot);
        criteriaQuery.where(predicate);
        
        setOrder(songPage, criteriaQuery, songRoot);

        TypedQuery<Song> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(songPage.getPageNumber() * songPage.getPageSize());
        typedQuery.setMaxResults(songPage.getPageSize());

        Pageable pageable = getPageable(songPage);

        long songCount = getSongCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, songCount);

    }

    private Predicate getPredicate(SongSearchCriteria songSearchCriteria, Root<Song> songRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(songSearchCriteria.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(songRoot.get("title"),
                            "%" + songSearchCriteria.getTitle() + "%")
            );
        }

        if (Objects.nonNull(songSearchCriteria.getGenre())) {

            List<Predicate> genrePredicates = new ArrayList<>();
            for (SongGenres genre : songSearchCriteria.getGenre()) {
                genrePredicates.add(criteriaBuilder.equal(songRoot.get("genre"), genre));
            }

            predicates.add(criteriaBuilder.or(genrePredicates.toArray(new Predicate[0])));
        }

        if (Objects.nonNull(songSearchCriteria.getArtistNickname())) {
            predicates.add(
                    criteriaBuilder.like(songRoot.get("artist").get("nickname"),
                            "%" + songSearchCriteria.getArtistNickname() + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(SongPage songPage, CriteriaQuery<Song> criteriaQuery, Root<Song> songRoot) {

        if (songPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(songRoot.get(songPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(songRoot.get(songPage.getSortBy())));
        }
    }

    private Pageable getPageable(SongPage songPage) {
        Sort sort = Sort.by(songPage.getSortDirection(), songPage.getSortBy());
        return PageRequest.of(songPage.getPageNumber(), songPage.getPageSize(), sort);
    }

    private long getSongCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Song> countRoot = countQuery.from(Song.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}