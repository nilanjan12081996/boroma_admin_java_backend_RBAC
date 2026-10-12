package skyware.product.skywareapi.sidebar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubSidebarRequestDto {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "shortName is required")
    private String shortName;

    @NotNull(message = "parentId is required")
    private Long parentId;
}
