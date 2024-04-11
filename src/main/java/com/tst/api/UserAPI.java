package com.tst.api;

import com.tst.models.responses.PaginationResponseObject;
import com.tst.models.responses.PagingResponseObject;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.user.UserResponse;
import com.tst.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserAPI {

    private final IUserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUser(
            @RequestParam(value = "q", defaultValue = "", required = false) String keyword,
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
                    .status(HttpStatus.OK)
                    .data(paginationResponseObject)
                    .build());
        }
    }
}
