package skyware.product.skywareapi.sidebar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import skyware.product.error.ValidationError;
import skyware.product.skywareapi.sidebar.dto.SidebarRequestDto;
import skyware.product.skywareapi.sidebar.dto.SubSidebarRequestDto;
import skyware.product.skywareapi.sidebar.dto.SubSidebarResponseDto;
import skyware.product.skywareapi.sidebar.service.SidebarService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("skyware/api/sidebar")
public class SidebarController {

    private final SidebarService sidebarService;

    @PostMapping("/add")
    public ResponseEntity<?> sideBarAdd(@Valid @RequestBody SidebarRequestDto sidebarRequestDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.put("message", "validation error");
            response.put("status", false);
            response.put("statusCode", 422);
            response.put("error", errors);

            return ResponseEntity.status(422).body(response);
        }
        try {
            sidebarService.saveSidebar(sidebarRequestDto);
            response.put("message", "Sidebar added successfully");
            response.put("status", true);
            response.put("statusCode", 201);
            return ResponseEntity.status(201).body(response);
        } catch (ValidationError e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            response.put("error", e.getMessage());

            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> sideBarList() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("message", "Sidebar listed successfully");
            response.put("status", true);
            response.put("statusCode", 200);
            response.put("data", sidebarService.getAllSidebars());
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping({"/sub-sidebar/add", "/subsidebar/add"})
    public ResponseEntity<?> addSubSidebar(@Valid @RequestBody SubSidebarRequestDto subSidebarRequestDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.put("message", "validation error");
            response.put("status", false);
            response.put("statusCode", 422);
            response.put("error", errors);

            return ResponseEntity.status(422).body(response);
        }
        try {
            SubSidebarResponseDto createdSubSidebar = sidebarService.saveSubSidebar(subSidebarRequestDto);
            response.put("message", "Sub-sidebar added successfully");
            response.put("status", true);
            response.put("statusCode", 201);
            response.put("data", createdSubSidebar);
            return ResponseEntity.status(201).body(response);
        } catch (ValidationError e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            response.put("error", e.getMessage());

            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping({"/sub-sidebar/list", "/subsidebar/list"})
    public ResponseEntity<?> listSubSidebars(@RequestParam(value = "parentId", required = false) Long parentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SubSidebarResponseDto> subSidebars;
            if (parentId != null) {
                subSidebars = sidebarService.getSubSidebarsByParentId(parentId);
            } else {
                subSidebars = sidebarService.getAllSubSidebars();
            }
            response.put("message", "Sub-sidebars fetched successfully");
            response.put("status", true);
            response.put("statusCode", 200);
            response.put("data", subSidebars);
            return ResponseEntity.status(200).body(response);
        } catch (ValidationError e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping({"/sub-sidebar/parent/{parentId}", "/subsidebar/parent/{parentId}"})
    public ResponseEntity<?> getSubSidebarsByParentId(@PathVariable("parentId") Long parentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SubSidebarResponseDto> subSidebars = sidebarService.getSubSidebarsByParentId(parentId);
            response.put("message", "Sub-sidebars fetched successfully for parent ID: " + parentId);
            response.put("status", true);
            response.put("statusCode", 200);
            response.put("data", subSidebars);
            return ResponseEntity.status(200).body(response);
        } catch (ValidationError e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping({"/sub-sidebar/{id}", "/subsidebar/{id}"})
    public ResponseEntity<?> getSubSidebarById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            SubSidebarResponseDto subSidebar = sidebarService.getSubSidebarById(id);
            response.put("message", "Sub-sidebar fetched successfully");
            response.put("status", true);
            response.put("statusCode", 200);
            response.put("data", subSidebar);
            return ResponseEntity.status(200).body(response);
        } catch (ValidationError e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("status", false);
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }
}

