package it.univaq.findervelox.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "autovelox")
public class Autovelox  implements Serializable {

    /*

    public static Autovelox parseData(JSONObject json) {
        try{
            Autovelox autovelox = new Autovelox();
            autovelox.setCity(json.getString("ccomune"));
            autovelox.setProvince(json.getString("cprovincia"));
            autovelox.setRegion(json.getString("cregione"));
            autovelox.setDateTimeInsertion(convertToTimeStamp(json.getString("canno_inserimento")));
            autovelox.setLongitude(Double.parseDouble(json.getString("clongitudine").replace(",",".")));
            autovelox.setLatitude(Double.parseDouble(json.getString("clatitudine").replace(",",".")));

            return autovelox;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     */

    public static Autovelox parseData(String[] parts) {
        try {
            Autovelox autovelox = new Autovelox();
            autovelox.setCity(parts[0]);
            autovelox.setProvince(parts[1]);
            autovelox.setRegion(parts[2]);
            autovelox.setDateTimeInsertion(convertToTimeStamp(parts[5]));
            autovelox.setLongitude(Double.parseDouble(parts[7].replace(",",".")));
            autovelox.setLatitude(Double.parseDouble(parts[8].replace(",",".")));

            return autovelox;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static long convertToTimeStamp(String text){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(text);
            if(date != null)
                return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String city;
    private String province;
    private String region;
    // TODO: 23/12/2021 E' possibile eliminare year insertion se è gia presente la data in date time insertion o meglio tenere entrambi?
    @ColumnInfo(name ="date_time_insertion")
    private long dateTimeInsertion;
    private double longitude;
    private double latitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getDateTimeInsertion() {
        return dateTimeInsertion;
    }

    public void setDateTimeInsertion(long dateTimeInsertion) {
        this.dateTimeInsertion = dateTimeInsertion;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getInfoPosition() {
        return latitude + ", " + longitude;
    }

}
