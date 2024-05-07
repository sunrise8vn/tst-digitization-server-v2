package com.tst.api;

import com.tst.exceptions.DataNotFoundException;
import com.tst.exceptions.PermissionDenyException;
import com.tst.models.dtos.extractShort.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.enums.EInputStatus;
import com.tst.models.responses.extractShort.*;
import com.tst.models.entities.User;
import com.tst.models.responses.ResponseObject;
import com.tst.services.birthExtractShort.IBirthExtractShortService;
import com.tst.services.deathExtractShort.IDeathExtractShortService;
import com.tst.services.marryExtractShort.IMarryExtractShortService;
import com.tst.services.parentsChildrenExtractShort.IParentsChildrenExtractShortService;
import com.tst.services.user.IUserService;
import com.tst.services.wedlockExtractShort.IWedlockExtractShortService;
import com.tst.utils.AppUtils;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/extract-short")
@RequiredArgsConstructor
@Validated
public class ExtractShortAPI {

    private final IParentsChildrenExtractShortService parentsChildrenExtractShortService;
    private final IBirthExtractShortService birthExtractShortService;
    private final IMarryExtractShortService marryExtractShortService;
    private final IWedlockExtractShortService wedlockExtractShortService;
    private final IDeathExtractShortService deathExtractShortService;
    private final IUserService userService;

    private final AppUtils appUtils;
    private final ModelMapper modelMapper;


    @GetMapping("/parents-children/new/{id}")
    public ResponseEntity<ResponseObject> getParentsChildrenExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!parentsChildrenExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        ParentsChildrenExtractShortResponse parentsChildrenExtractShortResponse = modelMapper.map(
                parentsChildrenExtractShort,
                ParentsChildrenExtractShortResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(parentsChildrenExtractShortResponse)
                .build());
    }

    @GetMapping("/birth/new/{id}")
    public ResponseEntity<ResponseObject> getBirthExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        BirthExtractShort birthExtractShort = birthExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!birthExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        BirthExtractShortResponse birthExtractShortResponse = modelMapper.map(
                birthExtractShort,
                BirthExtractShortResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(birthExtractShortResponse)
                .build());
    }

    @GetMapping("/marry/new/{id}")
    public ResponseEntity<ResponseObject> getMarryExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        MarryExtractShort marryExtractShort = marryExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!marryExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        MarryExtractShortResponse marryExtractShortResponse = modelMapper.map(
                marryExtractShort,
                MarryExtractShortResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(marryExtractShortResponse)
                .build());
    }

    @GetMapping("/wedlock/new/{id}")
    public ResponseEntity<ResponseObject> getWedlockExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!wedlockExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        WedlockExtractShortResponse wedlockExtractShortResponse = modelMapper.map(
                wedlockExtractShort,
                WedlockExtractShortResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(wedlockExtractShortResponse)
                .build());
    }

    @GetMapping("/death/new/{id}")
    public ResponseEntity<ResponseObject> getDeathExtractShort(
            @PathVariable @Pattern(regexp = "^[1-9]\\d*$", message = "ID biểu mẫu phải là một số") String id
    ) {
        User user = userService.getAuthenticatedUser();

        DeathExtractShort deathExtractShort = deathExtractShortService.findByIdAndStatus(
                Long.parseLong(id),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!deathExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được phân phối nhập liệu cho biểu mẫu này");
        }

        DeathExtractShortResponse deathExtractShortResponse = modelMapper.map(
                deathExtractShort,
                DeathExtractShortResponse.class
        );

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy dữ liệu trường ngắn của biểu mẫu khai tử thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(deathExtractShortResponse)
                .build());
    }

    @PatchMapping("/parents-children/new")
    public ResponseEntity<ResponseObject> importParentsChildrenExtractShort(
            @Validated @RequestBody ParentsChildrenExtractShortDTO parentsChildrenExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu cha mẹ con")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        ParentsChildrenExtractShort parentsChildrenExtractShort = parentsChildrenExtractShortService.findByIdAndStatus(
                Long.parseLong(parentsChildrenExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!parentsChildrenExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        parentsChildrenExtractShortService.update(parentsChildrenExtractShort, parentsChildrenExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu cha mẹ con thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/birth/new")
    public ResponseEntity<ResponseObject> importBirthExtractShort(
            @Validated @RequestBody BirthExtractShortDTO birthExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu khai sinh")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        BirthExtractShort birthExtractShort = birthExtractShortService.findByIdAndStatus(
                Long.parseLong(birthExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!birthExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        birthExtractShortService.update(birthExtractShort, birthExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu khai sinh thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/marry/new")
    public ResponseEntity<ResponseObject> importMarryExtractShort(
            @Validated @RequestBody MarryExtractShortDTO marryExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu kết hôn")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        MarryExtractShort marryExtractShort = marryExtractShortService.findByIdAndStatus(
                Long.parseLong(marryExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!marryExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        marryExtractShortService.update(marryExtractShort, marryExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu kết hôn thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/wedlock/new")
    public ResponseEntity<ResponseObject> importWedlockExtractShort(
            @Validated @RequestBody WedlockExtractShortDTO wedlockExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        WedlockExtractShort wedlockExtractShort = wedlockExtractShortService.findByIdAndStatus(
                Long.parseLong(wedlockExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!wedlockExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        wedlockExtractShortService.update(wedlockExtractShort, wedlockExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu tình trạng hôn nhân thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/death/new")
    public ResponseEntity<ResponseObject> importDeathExtractShort(
            @Validated @RequestBody DeathExtractShortDTO deathExtractShortDTO,
            BindingResult result
    ) {
        if (result.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Lỗi nhập dữ liệu trường ngắn của biểu mẫu khai tử")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .statusText(HttpStatus.BAD_REQUEST)
                    .data(appUtils.mapErrorToResponse(result))
                    .build());
        }

        User user = userService.getAuthenticatedUser();

        DeathExtractShort deathExtractShort = deathExtractShortService.findByIdAndStatus(
                Long.parseLong(deathExtractShortDTO.getId()),
                EInputStatus.NEW
        ).orElseThrow(() -> {
            throw new DataNotFoundException("ID biểu mẫu không tồn tại");
        });

        if (!deathExtractShort.getImporter().getId().equals(user.getId())) {
            throw new PermissionDenyException("Bạn không được quyền nhập liệu cho biểu mẫu này");
        }

        deathExtractShortService.update(deathExtractShort, deathExtractShortDTO);

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Nhập dữ liệu trường ngắn của biểu mẫu tình khai tử")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .build());
    }
}
