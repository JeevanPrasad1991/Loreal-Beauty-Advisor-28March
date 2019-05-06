package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerSalesHistory {
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Qtysold")
    @Expose
    private Integer qtysold;
    @SerializedName("ConsumerName")
    @Expose
    private String consumerName;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("VisitDate")
    @Expose
    private String visitDate;

    @SerializedName("EanCode")
    @Expose
    private String Ean_Code;

    public String getEan_Code() {
        return Ean_Code;
    }

    public void setEan_Code(String ean_Code) {
        Ean_Code = ean_Code;
    }



    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQtysold() {
        return qtysold;
    }

    public void setQtysold(Integer qtysold) {
        this.qtysold = qtysold;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}
