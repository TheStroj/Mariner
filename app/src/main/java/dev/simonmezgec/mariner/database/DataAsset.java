package dev.simonmezgec.mariner.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import dev.simonmezgec.mariner.Constant;

/** Plain Old Java Object (POJO) class for storing data assets. */
@SuppressWarnings({"unused", "WeakerAccess"})
@Entity(tableName = "data_assets")
public class DataAsset implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String oceanId;
    private String owner;
    private String type;
    private String name;
    @ColumnInfo(name = "date_created")
    private Date dateCreated;
    @ColumnInfo(name = "date_published")
    private Date datePublished;
    private String author;
    private String license;
    private double price;
    private String fileContentTypes;
    private String fileSizes;
    private String tags;
    private String categories;
    private String description;
    private String link;

    @Ignore
    public DataAsset() {
    }

    @Ignore
    public DataAsset(String oceanId, String owner, String type, String name, Date dateCreated,
                     Date datePublished, String author, String license, double price,
                     String fileContentTypes, String fileSizes, String tags, String categories,
                     String description, String link) {

        this.oceanId = oceanId;
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.dateCreated = dateCreated;
        this.datePublished = datePublished;
        this.author = author;
        this.license = license;
        this.price = price;
        this.fileContentTypes = fileContentTypes;
        this.fileSizes = fileSizes;
        this.tags = tags;
        this.categories = categories;
        this.description = description;
        this.link = link;
    }

    public DataAsset(int id, String oceanId, String owner, String type, String name,
                     Date dateCreated, Date datePublished, String author, String license,
                     double price, String fileContentTypes, String fileSizes, String tags,
                     String categories, String description, String link) {
        this.id = id;
        this.oceanId = oceanId;
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.dateCreated = dateCreated;
        this.datePublished = datePublished;
        this.author = author;
        this.license = license;
        this.price = price;
        this.fileContentTypes = fileContentTypes;
        this.fileSizes = fileSizes;
        this.tags = tags;
        this.categories = categories;
        this.description = description;
        this.link = link;
    }

    private DataAsset(Parcel in) {
        this.id = in.readInt();
        this.oceanId = in.readString();
        this.owner = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.dateCreated = new Date(in.readLong());
        this.datePublished = new Date(in.readLong());
        this.author = in.readString();
        this.license = in.readString();
        this.price = in.readDouble();
        this.fileContentTypes = in.readString();
        this.fileSizes = in.readString();
        this.tags = in.readString();
        this.categories = in.readString();
        this.description = in.readString();
        this.link = in.readString();
    }

    public static final Parcelable.Creator<DataAsset> CREATOR
            = new Parcelable.Creator<DataAsset>() {
        public DataAsset createFromParcel(Parcel in) {
            return new DataAsset(in);
        }

        public DataAsset[] newArray(int size) {
            return new DataAsset[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(oceanId);
        out.writeString(owner);
        out.writeString(type);
        out.writeString(name);
        out.writeLong(dateCreated.getTime());
        out.writeLong(datePublished.getTime());
        out.writeString(author);
        out.writeString(license);
        out.writeDouble(price);
        out.writeString(fileContentTypes);
        out.writeString(fileSizes);
        out.writeString(tags);
        out.writeString(categories);
        out.writeString(description);
        out.writeString(link);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOceanId() {
        return oceanId;
    }

    public void setOceanId(String oceanId) {
        this.oceanId = oceanId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getParsedDateCreated() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(Constant.TIME_ZONE));
        return dateFormat.format(dateCreated);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public String getParsedDatePublished() {
        if (datePublished != null) {
            SimpleDateFormat dateFormat
                    = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone(Constant.TIME_ZONE));
            return dateFormat.format(datePublished);
        }
        else return "";
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public double getPrice() {
        return price;
    }

    public String getParsedPrice() {
        DecimalFormat decimalFormat = new DecimalFormat(Constant.DECIMAL_FORMAT);
        return decimalFormat.format(price) + " " + Constant.OCEAN_TOKENS;
    }

    public boolean isFree() {
        return price == 0.0;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFileContentTypes() {
        return fileContentTypes;
    }

    public String[] getParsedFileContentTypes() {
        return fileContentTypes.split(",");
    }

    public void setFileContentTypes(String fileContentTypes) {
        this.fileContentTypes = fileContentTypes;
    }

    public String getFileSizes() {
        return fileSizes;
    }

    public int[] getParsedFileSizes() {
        String[] fileSizesStringArray = fileSizes.split(",");
        int[] fileSizesIntArray = new int[fileSizesStringArray.length];
        for(int i = 0; i < fileSizesStringArray.length; i++) {
            fileSizesIntArray[i] = Integer.parseInt(fileSizesStringArray[i]);
        }
        return fileSizesIntArray;
    }

    public void setFileSizes(String fileSizes) {
        this.fileSizes = fileSizes;
    }

    public String getTags() {
        return tags;
    }

    public String[] getParsedTags() {
        return tags.split(",");
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public String[] getParsedCategories() {
        return categories.split(",");
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}