package br.com.coeur.api.shared;

public final class Pagination {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    private Pagination() {
    }

    public record Normalized(int page, int pageSize) {
    }

    public static Normalized normalize(Integer page, Integer pageSize) {
        int normalizedPage = Math.max(page != null ? page : 1, 1);
        int normalizedPageSize = Math.clamp(pageSize != null ? pageSize : DEFAULT_PAGE_SIZE, 1, MAX_PAGE_SIZE);
        return new Normalized(normalizedPage, normalizedPageSize);
    }
}
