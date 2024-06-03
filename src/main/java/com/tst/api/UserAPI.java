package com.tst.api;

import com.tst.components.LocalizationUtils;
import com.tst.exceptions.DataInputException;
import com.tst.models.entities.Project;
import com.tst.models.entities.ProjectUser;
import com.tst.models.entities.User;
import com.tst.models.responses.PaginationResponseObject;
import com.tst.models.responses.PagingResponseObject;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.user.UserAssignResponse;
import com.tst.models.responses.user.UserAssignWithRemainingTotalResponse;
import com.tst.models.responses.user.UserResponse;
import com.tst.services.project.IProjectService;
import com.tst.services.projectUser.IProjectUserService;
import com.tst.services.user.IUserService;
import com.tst.utils.MessageKeys;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserAPI {

    private final LocalizationUtils localizationUtils;
    private final IUserService userService;
    private final IProjectService projectService;
    private final IProjectUserService projectUserService;


    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUser(
            @RequestParam(value = "q", defaultValue = "") String keyword,
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "10") int limit,
            @RequestParam(value = "_sort", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "_order", defaultValue = "desc") String order
    ) throws Exception {
        keyword = "%" + keyword + "%";
        Pageable pageable;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page - 1, limit, Sort.by(sortBy).descending());
        }

        Page<UserResponse> userResponses = userService.findAllUserResponse(keyword, pageable);

        if (userResponses.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.NO_CONTENT))
                            .status(HttpStatus.OK.value())
                            .statusText(HttpStatus.OK)
                            .build(),
                    HttpStatus.OK
            );
        } else {
            PagingResponseObject pagingResponseObject = new PagingResponseObject();
            pagingResponseObject.setCount(userResponses.getTotalElements());
            pagingResponseObject.setLimit(limit);
            pagingResponseObject.setTotalPages(userResponses.getTotalPages());
            pagingResponseObject.setOffset(page);

            PaginationResponseObject paginationResponseObject = new PaginationResponseObject();
            paginationResponseObject.setContent(userResponses.getContent());
            paginationResponseObject.setPaging(pagingResponseObject);

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Get user list successfully")
                    .status(HttpStatus.OK.value())
                    .statusText(HttpStatus.OK)
                    .data(paginationResponseObject)
                    .build());
        }
    }


    @GetMapping("/get-all-by-project/{projectId}")
    public ResponseEntity<ResponseObject> getAllUserByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
           throw new DataInputException("Dự án không tồn tại");
        });

        List<UserAssignResponse> users = projectUserService.findAllByProject(project);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get user list with project successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(users)
                .build());
    }

    @GetMapping("/get-all-with-remaining-total-by-project/{projectId}")
    public ResponseEntity<ResponseObject> getAllUserWithRemainingTotalByProject(
            @PathVariable @Pattern(regexp = "^\\d+$", message = "ID dự án phải là một số") String projectId
    ) {
        Project project = projectService.findById(
                Long.parseLong(projectId)
        ).orElseThrow(() -> {
            throw new DataInputException("Dự án không tồn tại");
        });

        List<UserAssignResponse> users = projectUserService.findAllByProject(project);

        Long remainingTotal = projectService.getRemainingTotal(project);

        UserAssignWithRemainingTotalResponse userAssignWithRemainingTotal = new UserAssignWithRemainingTotalResponse()
                .setUsers(users)
                .setRemainingTotal(remainingTotal)
                ;

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get user list with project successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(userAssignWithRemainingTotal)
                .build());
    }
}
