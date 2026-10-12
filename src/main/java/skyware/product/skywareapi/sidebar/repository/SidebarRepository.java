package skyware.product.skywareapi.sidebar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skyware.product.skywareapi.sidebar.entity.SidebarEntity;

import java.util.List;
import java.util.Optional;

public interface SidebarRepository extends JpaRepository<SidebarEntity, Long>, JpaSpecificationExecutor<SidebarEntity> {

    List<SidebarEntity> findByParentId(Long parentId);

    @Query("SELECT s FROM SidebarEntity s WHERE s.parentId IS NOT NULL AND s.parentId > 0")
    List<SidebarEntity> findAllSubSidebars();

    @Query("SELECT s FROM SidebarEntity s WHERE s.parentId IS NULL OR s.parentId = 0")
    List<SidebarEntity> findAllMainSidebars();

    boolean existsByShortName(String shortName);

    Optional<SidebarEntity> findByShortName(String shortName);
}

