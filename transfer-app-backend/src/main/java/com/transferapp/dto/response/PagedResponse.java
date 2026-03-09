package com.transferapp.dto.response;

import lombok.*;

import java.util.List;

/**
 * Generic paginated response wrapper.
 * Returned from paginated endpoints so the frontend can render
 * pagination controls without needing a separate metadata call.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {

    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
