package com.stratumtech.realtyauthuser.repository;

import com.stratumtech.realtyauthuser.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, Integer> {
} 