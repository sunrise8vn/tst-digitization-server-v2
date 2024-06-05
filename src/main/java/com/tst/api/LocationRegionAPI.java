package com.tst.api;

import com.tst.exceptions.DataInputException;
import com.tst.models.responses.ResponseObject;
import com.tst.models.responses.locationRegion.*;
import com.tst.services.locationDistrict.ILocationDistrictService;
import com.tst.services.locationNation.ILocationNationService;
import com.tst.services.locationNationality.ILocationNationalityService;
import com.tst.services.locationProvince.ILocationProvinceService;
import com.tst.services.locationWard.ILocationWardService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/location-region")
@RequiredArgsConstructor
@Validated
public class LocationRegionAPI {

    private final ILocationNationService locationNationService;
    private final ILocationNationalityService locationNationalityService;
    private final ILocationProvinceService locationProvinceService;
    private final ILocationDistrictService locationDistrictService;
    private final ILocationWardService locationWardService;


    @GetMapping("/nations")
    public ResponseEntity<ResponseObject> getAllNation() {
        List<LocationNationResponse> locationNationResponses = locationNationService.findAllLocationNationResponse();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách dân tộc thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationNationResponses)
                .build());
    }

    @GetMapping("/nationalities")
    public ResponseEntity<ResponseObject> getAllNationality() {
        List<LocationNationalityResponse> locationNationalityResponses = locationNationalityService.findAllLocationNationalityResponse();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Lấy danh sách quốc gia thành công")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationNationalityResponses)
                .build());
    }

    @GetMapping("/provinces")
    public ResponseEntity<ResponseObject> getAllProvinces() {
        List<LocationProvinceResponse> locationProvinceResponses = locationProvinceService.findAllLocationProvinceResponse();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get all province locations successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationProvinceResponses)
                .build());
    }

    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<ResponseObject> getAllDistrictsByProvinceId(
        @PathVariable @Pattern(regexp = "\\d+", message = "ID tỉnh/thành phố phải là một số") String provinceId
    ) {
        locationProvinceService.findById(Long.parseLong(provinceId)).orElseThrow(() -> {
            throw new DataInputException("ID tỉnh/thành phố không tồn tại");
        });

        List<LocationDistrictResponse> locationDistrictResponses = locationDistrictService.findAllLocationDistrictResponse(Long.parseLong(provinceId));

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get all district locations successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationDistrictResponses)
                .build());
    }

    @GetMapping("/wards/{districtId}")
    public ResponseEntity<ResponseObject> getAllWardsByDistrictId(
        @PathVariable @Pattern(regexp = "\\d+", message = "ID thành phố/quận/huyện phải là một số") String districtId
    ) {
        locationDistrictService.findById(Long.parseLong(districtId)).orElseThrow(() -> {
            throw new DataInputException("ID thành phố/quận/huyện không tồn tại");
        });

        List<LocationWardResponse> locationWardResponses = locationWardService.findAllLocationWardResponse(Long.parseLong(districtId));

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get all ward locations successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data(locationWardResponses)
                .build());
    }
}
