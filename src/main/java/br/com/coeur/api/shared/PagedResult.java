package br.com.coeur.api.shared;

import java.util.List;

public record PagedResult<T>(
        List<T> items,
        int page,
        int pageSize,
        long totalCount
) {
    public int totalPages() {
        return pageSize <= 0 ? 0 : (int) Math.ceil(totalCount / (double) pageSize);
    }
}
