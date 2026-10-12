package skyware.product.skywareapi.sidebar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import skyware.product.skywareapi.sidebar.entity.SidebarEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubSidebarResponseDto {

    private Long id;
    private String name;
    private String shortName;
    private Long parentId;
    private Long statusId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SidebarEntity parentSidebar;
}
