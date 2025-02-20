package com.tst.api;

import com.tst.components.LocalizationUtils;
import com.tst.models.entities.User;
import com.tst.models.enums.EUserRole;
import com.tst.models.responses.ResponseObject;
import com.tst.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/test")
@RequiredArgsConstructor
public class TestAPI {

    private final LocalizationUtils localizationUtils;

    @GetMapping("/hello")
    public ResponseEntity<ResponseObject> getHello() {
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get Hello successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PostMapping("/hello")
    public ResponseEntity<ResponseObject> postHello() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDetails.getRole().getName().equals(EUserRole.ROLE_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.ERROR_FORBIDDEN))
                    .status(HttpStatus.FORBIDDEN.value())
                    .statusText(HttpStatus.FORBIDDEN)
                    .build());
        }

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Post Hello successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
