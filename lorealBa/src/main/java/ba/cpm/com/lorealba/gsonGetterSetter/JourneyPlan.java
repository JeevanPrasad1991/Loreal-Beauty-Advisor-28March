
package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JourneyPlan {
    @SerializedName("StoreId")
    @Expose
    private Integer storeId;

    @SerializedName("CounterId")
    @Expose
    private Integer counterId;

    @SerializedName("VisitDate")
    @Expose
    private String visitDate;
    @SerializedName("DistributorName")
    @Expose
    private String distributorName;
    @SerializedName("StoreName")
    @Expose
    private String storeName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Landmark")
    @Expose
    private String landmark;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("ContactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("StoreTypeName")
    @Expose
    private String storeTypeName;
    @SerializedName("StoreCategoryName")
    @Expose
    private String storeCategoryName;
    @SerializedName("StateId")
    @Expose
    private Integer stateId;
    @SerializedName("StoreTypeId")
    @Expose
    private Integer storeTypeId;
    @SerializedName("StoreCategoryId")
    @Expose
    private Integer storeCategoryId;
    @SerializedName("ReasonId")
    @Expose
    private Integer reasonId;
    @SerializedName("Upload_Status")
    @Expose
    private String uploadStatus;
    @SerializedName("Geo_Tag")
    @Expose
    private String geoTag;
    @SerializedName("DistributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("ClassificationId")
    @Expose
    private Integer classificationId;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("GeoFencing")
    @Expose
    private Integer geoFencing;
    @SerializedName("StoreCode")
    @Expose
    private String storeCode;
    @SerializedName("ClassificationName")
    @Expose
    private String classificationName;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getVisitDate() {
        return visitDate;
    }
    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String getStoreCategoryName() {
        return storeCategoryName;
    }

    public void setStoreCategoryName(String storeCategoryName) {
        this.storeCategoryName = storeCategoryName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(Integer storeTypeId) {
        this.storeTypeId = storeTypeId;
    }

    public Integer getStoreCategoryId() {
        return storeCategoryId;
    }

    public void setStoreCategoryId(Integer storeCategoryId) {
        this.storeCategoryId = storeCategoryId;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(String geoTag) {
        this.geoTag = geoTag;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Integer classificationId) {
        this.classificationId = classificationId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getGeoFencing() {
        return geoFencing;
    }

    public void setGeoFencing(Integer geoFencing) {
        this.geoFencing = geoFencing;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

}
