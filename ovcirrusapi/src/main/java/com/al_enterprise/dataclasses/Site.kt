package com.al_enterprise.dataclasses


data class FloorPlanImageCoordinates(
    val type: String? = null,
    val coordinates: List<List<Double>>? = null  // Nested list for coordinates
)

data class AreaGeometry(
    val type: String? = null,
    val coordinates: List<List<List<Double>>>? = null  // Nested list for coordinates
)

data class FloorDetail(
    val id: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val floorNumber: Int? = null,
    val floorPlanUrl: String? = null,
    val floorPlanImageCoordinates: FloorPlanImageCoordinates? = null,
    val relativeAltitude: Int? = null,
    val areaGeometry: AreaGeometry? = null,
    val area: Int? = null,
    val areaUnit: String? = null,
    val building: String? = null,
    val site: String? = null,
    val organization: String? = null
)

data class BuildingDetail(
    val id: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val site: String? = null,
    val organization: String? = null,
    val floors: List<FloorDetail>? = null
)

data class Site(
    val id: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val countryCode: String? = null,
    val timezone: String? = null,
    val address: String? = null,
    val location: Location? = null,
    val imageUrl: String? = null,
    val isDefault: Boolean? = null,
    val zoom: Int? = null,
    val organization: String? = null,
    val buildings: List<BuildingDetail>? = null
)