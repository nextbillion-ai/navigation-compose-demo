package ai.nextbillion.compose.example.kotlin.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ai.nextbillion.kits.core.constants.Constants;
import ai.nextbillion.kits.directions.models.DirectionsRoute;
import ai.nextbillion.kits.geojson.LineString;
import ai.nextbillion.kits.geojson.Point;
import ai.nextbillion.maps.camera.CameraPosition;
import ai.nextbillion.maps.camera.CameraUpdate;
import ai.nextbillion.maps.camera.CameraUpdateFactory;
import ai.nextbillion.maps.exceptions.InvalidLatLngBoundsException;
import ai.nextbillion.maps.geometry.LatLng;
import ai.nextbillion.maps.geometry.LatLngBounds;
import ai.nextbillion.navigation.ui.camera.CameraUpdateMode;
import ai.nextbillion.navigation.ui.camera.NavigationCameraUpdate;
import ai.nextbillion.navigation.ui.map.NavNextbillionMap;


public class CameraAnimateUtils {

    static LatLngBounds calculateBounds(List<DirectionsRoute> directionsRoutes) {
        if (!directionsRoutes.isEmpty()) {
            List<LatLng> bboxPoints = new ArrayList<>();
            for (DirectionsRoute directionsRoute : directionsRoutes) {
                List<Point> routeCoords = LineString.fromPolyline(directionsRoute.geometry(),
                        Constants.PRECISION_6).coordinates();
                for (Point point : routeCoords) {
                    bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
                }
            }
            if (bboxPoints.size() > 1) {
                try {
                    return new LatLngBounds.Builder().includes(bboxPoints).build();
                } catch (InvalidLatLngBoundsException exception) {
                }
            }
        }
        return null;
    }


    public static void frameCameraToBounds(NavNextbillionMap map, List<DirectionsRoute> routes, int[] padding) {
        LatLngBounds bounds = calculateBounds(routes);
        if (bounds != null) {
            CameraPosition position = map.retrieveMap().getCameraForLatLngBounds(bounds, padding);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
            NavigationCameraUpdate navigationCameraUpdate = new NavigationCameraUpdate(cameraUpdate);
            navigationCameraUpdate.setMode(CameraUpdateMode.DEFAULT);
            map.retrieveCamera().update(navigationCameraUpdate, 1000);
        }
    }

    public static int[] createPadding(Context context) {
        int horPadding = dpToPx(context, 55);
        int topPadding = dpToPx(context, 100);
        int bottomPadding = (int) (dpToPx(context, 135));
        return new int[]{horPadding, topPadding, horPadding, bottomPadding};
    }

    static int dpToPx(Context context, double dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}