package com.nowbartend.global.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeometryUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    // 위도, 경도를 이용해 Point 객체를 생성하는 메소드
    public static Point createPoint(double latitude, double longitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);

        return point;
    }
}
