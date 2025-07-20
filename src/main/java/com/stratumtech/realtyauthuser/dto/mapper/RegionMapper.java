package com.stratumtech.realtyauthuser.dto.mapper;

import org.mapstruct.Named;
import org.mapstruct.Mapper;

import com.stratumtech.realtyauthuser.entity.Region;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    @Named("mapRegionId")
    default Long mapRegionId(Region region){
        return region == null
                ? null
                : region.getId();
    }

    @Named("mapRegion")
    default Region mapRegion(Long regionId) {
        Region r = new Region();
        r.setId(regionId);
        return r;
    }
}
