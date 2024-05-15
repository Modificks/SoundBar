package Web.Player.SoundBar.ViewLayers.Mapper;

public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);
}