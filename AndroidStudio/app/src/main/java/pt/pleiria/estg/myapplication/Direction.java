package pt.pleiria.estg.myapplication;

import com.google.type.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

public class Direction {
    private Object html_instructions;
    private Object distance;
    private Object lat;
    private Object lng;

    public Direction(Object html_instructions, Object distance, Object lat, Object lng) {
        this.html_instructions = html_instructions;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }

    public Object getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(Object html_instructions) {
        this.html_instructions = html_instructions;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(Object distance) {
        this.distance = distance;
    }

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lag) {
        this.lat = lag;
    }

    public Object getLng() {
        return lng;
    }

    public void setLng(Object lng) {
        this.lng = lng;
    }
}
