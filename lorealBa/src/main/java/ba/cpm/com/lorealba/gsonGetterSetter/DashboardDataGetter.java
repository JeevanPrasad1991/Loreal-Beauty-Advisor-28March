package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataGetter {
    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("Value")
    @Expose
    private Integer value;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @SerializedName("Url")
    @Expose
    private String  Url;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
