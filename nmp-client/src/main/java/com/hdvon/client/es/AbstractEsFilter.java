package com.hdvon.client.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.hdvon.client.vo.PointVo;
/**
 * Geo过滤器
 * @author wanshaojian
 *
 */
public abstract class AbstractEsFilter {
	/**
	 * geodistance filter 一个过滤器来过滤基于一个特定的距离从一个特定的地理位置/点。
	 * 
	 * @author wanshaojian
     * @param x 纬度
     * @param y 经度
	 * @param distance 距离 (米)
	 * @return GeoDistanceQueryBuilder
	 */
	public static GeoDistanceQueryBuilder geoDistanceFilter(Double x,Double y, int distance) {
		return QueryBuilders.geoDistanceQuery("location").point(x, y).distance(distance, DistanceUnit.METERS)
				.geoDistance(GeoDistance.ARC);
	}
	
	/**
	 * geoDistanceSortBuilder filter
	 * 获取距离多少公里 这个才是获取点与点之间的距离的
	 * @author wanshaojian
     * @param x 纬度
     * @param y 经度
	 * @param distance 距离 (米)
	 * @return GeoDistanceQueryBuilder
	 */
	public static GeoDistanceSortBuilder geoDistanceSortBuilderFilter(Double x,Double y) {
		GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location", x, y);
		sort.unit(DistanceUnit.METERS);
		sort.order(SortOrder.ASC);
		sort.point(x, y);
		
		return sort;
	}

	/**
	 * geo bounding box filter
	 * 		定义一个过滤器来过滤基于边界框左上角和右下角的位置/分
	 * @param topleftX 左上角纬度
	 * @param topleftY 左上角经度
	 * @param bottomRightX 右下角纬度
	 * @param bottomRightY 右下角经度
	 * @return GeoBoundingBoxQueryBuilder
	 */
	public static GeoBoundingBoxQueryBuilder geoBoundingBoxFilter(Double topleftX,Double topleftY,Double bottomRightX,Double bottomRightY) {
		return QueryBuilders.geoBoundingBoxQuery("location").setCorners(new GeoPoint(topleftX,topleftY), new GeoPoint(bottomRightX,bottomRightY));				

	}
	
	
	/**
     * geo Polygon box filter
     * 定义一个过滤器来过滤基于多边形查询
     * @author wanshaojian
     * @param pointList 坐标列表
     * @return GeoPolygonQueryBuilder
     */
	public static GeoPolygonQueryBuilder geoPolygonFilter(List<PointVo> pointList) {
		List<GeoPoint> points = new ArrayList<>();
		for(PointVo vo:pointList) {
			points.add(new GeoPoint(vo.getLatitude(),vo.getLongitude()));
		}
		return QueryBuilders.geoPolygonQuery("location", points);
	}
	
}
