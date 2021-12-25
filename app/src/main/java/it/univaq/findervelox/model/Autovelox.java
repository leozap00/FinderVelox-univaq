package it.univaq.findervelox.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "autovelox")
public class Autovelox  implements Serializable {

    public static Autovelox parseData(String[] parts) {
        try {
            Autovelox autovelox = new Autovelox();
            autovelox.setCity(parts[0]);
            autovelox.setProvince(parts[1]);
            autovelox.setRegion(parts[2]);
            autovelox.setYearInsertion(Integer.parseInt(parts[4]));
            autovelox.setDateTimeInsertion(convertToTimeStamp(parts[5]));
            autovelox.setLongitude(Double.parseDouble(parts[7].replace(",",".")));
            autovelox.setLatitude(Double.parseDouble(parts[8].replace(",",".")));

            return autovelox;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Date convertToTimeStamp(String text){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(text);
            if(date != null)
                return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String city;
    private String province;
    private String region;
    // TODO: 23/12/2021 E' possibile eliminare year insertion se è gia presente la data in date time insertion o meglio tenere entrambi? 
    @ColumnInfo(name = "year_insertion")
    private Integer yearInsertion;
    @ColumnInfo(name = "date_time_insertion")
    private Date dateTimeInsertion;
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

    public Integer getYearInsertion() {
        return yearInsertion;
    }

    public void setYearInsertion(Integer yearInsertion) {
        yearInsertion = yearInsertion;
    }

    public Date getDateTimeInsertion() {
        return dateTimeInsertion;
    }

    public void setDateTimeInsertion(Date dateTimeInsertion) {
        dateTimeInsertion = dateTimeInsertion;
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
}
