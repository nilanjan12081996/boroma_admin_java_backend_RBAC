package skyware.product.skywareapi.sidebar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skyware.product.error.ValidationError;
import skyware.product.skywareapi.sidebar.dto.SidebarRequestDto;
import skyware.product.skywareapi.sidebar.dto.SubSidebarRequestDto;
import skyware.product.skywareapi.sidebar.dto.SubSidebarResponseDto;
import skyware.product.skywareapi.sidebar.entity.SidebarEntity;
import skyware.product.skywareapi.sidebar.mapper.SidebarMapper;
import skyware.product.skywareapi.sidebar.repository.SidebarRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SidebarService {

    private final SidebarRepository sidebarRepository;

    public void saveSidebar(SidebarRequestDto sidebarRequestDto) {
        if (sidebarRepository.existsByShortName(sidebarRequestDto.getShortName())) {
            throw new ValidationError("Sidebar short name already exists: " + sidebarRequestDto.getShortName());
        }

        SidebarEntity sidebarEntity = SidebarEntity.builder()
                .name(sidebarRequestDto.getName())
                .shortName(sidebarRequestDto.getShortName())
                .parentId(sidebarRequestDto.getParentId() != null ? sidebarRequestDto.getParentId() : 0L)
                .statusId(1L)
                .build();
        sidebarRepository.save(sidebarEntity);
    }

    public List<SidebarEntity> getAllSidebars() {
        return sidebarRepository.findAll();
    }

    public SubSidebarResponseDto saveSubSidebar(SubSidebarRequestDto subSidebarRequestDto) {
        if (subSidebarRequestDto.getParentId() == null || subSidebarRequestDto.getParentId() <= 0) {
            throw new ValidationError("Valid parentId is required for sub-sidebar");
        }

        SidebarEntity parentSidebar = sidebarRepository.findById(subSidebarRequestDto.getParentId())
                .orElseThrow(() -> new ValidationError("Parent sidebar not found with ID: " + subSidebarRequestDto.getParentId()));

        if (sidebarRepository.existsByShortName(subSidebarRequestDto.getShortName())) {
            throw new ValidationError("Sidebar short name already exists: " + subSidebarRequestDto.getShortName());
        }

        SidebarEntity subSidebarEntity = SidebarEntity.builder()
                .name(subSidebarRequestDto.getName())
                .shortName(subSidebarRequestDto.getShortName())
                .parentId(parentSidebar.getId())
                .statusId(1L)
                .isActive(true)
                .build();

        SidebarEntity savedSubSidebar = sidebarRepository.save(subSidebarEntity);

        return SidebarMapper.toSubSidebarResponseDto(savedSubSidebar, parentSidebar);
    }

    public List<SubSidebarResponseDto> getAllSubSidebars() {
        List<SidebarEntity> subSidebars = sidebarRepository.findAllSubSidebars();
        Map<Long, SidebarEntity> parentCache = new HashMap<>();
        List<SubSidebarResponseDto> responseList = new ArrayList<>();

        for (SidebarEntity subSidebar : subSidebars) {
            SidebarEntity parent = null;
            if (subSidebar.getParentId() != null && subSidebar.getParentId() > 0) {
                parent = parentCache.computeIfAbsent(subSidebar.getParentId(),
                        id -> sidebarRepository.findById(id).orElse(null));
            }
            responseList.add(SidebarMapper.toSubSidebarResponseDto(subSidebar, parent));
        }

        return responseList;
    }

    public List<SubSidebarResponseDto> getSubSidebarsByParentId(Long parentId) {
        SidebarEntity parentSidebar = sidebarRepository.findById(parentId)
                .orElseThrow(() -> new ValidationError("Parent sidebar not found with ID: " + parentId));

        List<SidebarEntity> subSidebars = sidebarRepository.findByParentId(parentId);
        List<SubSidebarResponseDto> responseList = new ArrayList<>();

        for (SidebarEntity subSidebar : subSidebars) {
            responseList.add(SidebarMapper.toSubSidebarResponseDto(subSidebar, parentSidebar));
        }

        return responseList;
    }

    public SubSidebarResponseDto getSubSidebarById(Long id) {
        SidebarEntity subSidebar = sidebarRepository.findById(id)
                .orElseThrow(() -> new ValidationError("Sub-sidebar not found with ID: " + id));

        SidebarEntity parentSidebar = null;
        if (subSidebar.getParentId() != null && subSidebar.getParentId() > 0) {
            parentSidebar = sidebarRepository.findById(subSidebar.getParentId()).orElse(null);
        }

        return SidebarMapper.toSubSidebarResponseDto(subSidebar, parentSidebar);
    }
}

