package ba.cpm.com.lorealba.gsonGetterSetter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMaster {
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("LorealCode")
    @Expose
    private String lorealCode;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductType")
    @Expose
    private String productType;
    @SerializedName("NuanceId")
    @Expose
    private Integer nuanceId;
    @SerializedName("NuanceName")
    @Expose
    private String nuanceName;
    @SerializedName("SubAxeId")
    @Expose
    private Integer subAxeId;
    @SerializedName("SubAxeName")
    @Expose
    private String subAxeName;
    @SerializedName("AxeId")
    @Expose
    private Integer axeId;
    @SerializedName("AxeName")
    @Expose
    private String axeName;
    @SerializedName("ReferenceId")
    @Expose
    private Integer referenceId;
    @SerializedName("ReferenceName")
    @Expose
    private String referenceName;
    @SerializedName("SubBrandId")
    @Expose
    private Integer subBrandId;
    @SerializedName("SubBrandName")
    @Expose
    private String subBrandName;
    @SerializedName("BrandId")
    @Expose
    private Integer brandId;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("SignatureId")
    @Expose
    private Integer signatureId;
    @SerializedName("SignatureName")
    @Expose
    private String signatureName;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getLorealCode() {
        return lorealCode;
    }

    public void setLorealCode(String lorealCode) {
        this.lorealCode = lorealCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getNuanceId() {
        return nuanceId;
    }

    public void setNuanceId(Integer nuanceId) {
        this.nuanceId = nuanceId;
    }

    public String getNuanceName() {
        return nuanceName;
    }

    public void setNuanceName(String nuanceName) {
        this.nuanceName = nuanceName;
    }

    public Integer getSubAxeId() {
        return subAxeId;
    }

    public void setSubAxeId(Integer subAxeId) {
        this.subAxeId = subAxeId;
    }

    public String getSubAxeName() {
        return subAxeName;
    }

    public void setSubAxeName(String subAxeName) {
        this.subAxeName = subAxeName;
    }

    public Integer getAxeId() {
        return axeId;
    }

    public void setAxeId(Integer axeId) {
        this.axeId = axeId;
    }

    public String getAxeName() {
        return axeName;
    }

    public void setAxeName(String axeName) {
        this.axeName = axeName;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public Integer getSubBrandId() {
        return subBrandId;
    }

    public void setSubBrandId(Integer subBrandId) {
        this.subBrandId = subBrandId;
    }

    public String getSubBrandName() {
        return subBrandName;
    }

    public void setSubBrandName(String subBrandName) {
        this.subBrandName = subBrandName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Integer signatureId) {
        this.signatureId = signatureId;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }
}
