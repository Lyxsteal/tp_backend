package com.example.ms_rutas.model.dto;
import java.util.List;

public class OsrmResponseDto {
    private List<OsrmRouteDto> routes;
    private String code;

    public List<OsrmRouteDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<OsrmRouteDto> routes) {
        this.routes = routes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}