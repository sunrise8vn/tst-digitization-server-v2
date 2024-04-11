package com.tst.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagingResponseObject {
    private long count;
    private int limit;
    private int totalPages;
    private int offset;
}
