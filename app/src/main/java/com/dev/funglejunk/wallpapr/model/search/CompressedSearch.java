package com.dev.funglejunk.wallpapr.model.search;

public class CompressedSearch {

    public final int farmId;
    public final String serverId;
    public final String imageId;
    public final String secret;

    private CompressedSearch(int farmId, String serverId, String imageId, String secret) {
        this.farmId = farmId;
        this.serverId = serverId;
        this.imageId = imageId;
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "CompressedAnswer{" +
                "farmId=" + farmId +
                ", serverId='" + serverId + '\'' +
                ", imageId='" + imageId + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }

    public static CompressedSearch fromRawSearch(RawSearch.Photos.Photo photo) {
        return new CompressedSearch(
                photo.farm,
                photo.server,
                photo.id,
                photo.secret
        );
    }

}
