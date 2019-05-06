
package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InwardSalesPO {

    @SerializedName("StoreId")
    @Expose
    private Integer storeId;
    @SerializedName("StoreCode")
    @Expose
    private String storeCode;
    @SerializedName("CounterId")
    @Expose
    private Integer counterId;
    @SerializedName("CounterCode")
    @Expose
    private String counterCode;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("ProductCode")
    @Expose
    private String productCode;
    @SerializedName("GrnRefNo")
    @Expose
    private String grnRefNo;
    @SerializedName("QTY")
    @Expose
    private Integer qTY;
    @SerializedName("MRP")
    @Expose
    private Integer mRP;
    @SerializedName("UOM")
    @Expose
    private String uOM;
    @SerializedName("GrnDate")
    @Expose
    private String grnDate;
    @SerializedName("GrnType")
    @Expose
    private String grnType;
    @SerializedName("ProductName")
    @Expose
    private String productName;

    public String getProductName() {
        return productName;
    }

    private int stock=0;
    private int reasonId=0;
    private String Reason="";

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getGrnRefNo() {
        return grnRefNo;
    }

    public void setGrnRefNo(String grnRefNo) {
        this.grnRefNo = grnRefNo;
    }

    public Integer getQTY() {
        return qTY;
    }

    public void setQTY(Integer qTY) {
        this.qTY = qTY;
    }

    public Integer getMRP() {
        return mRP;
    }

    public void setMRP(Integer mRP) {
        this.mRP = mRP;
    }

    public String getUOM() {
        return uOM;
    }

    public void setUOM(String uOM) {
        this.uOM = uOM;
    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public String getGrnType() {
        return grnType;
    }

    public void setGrnType(String grnType) {
        this.grnType = grnType;
    }

}
