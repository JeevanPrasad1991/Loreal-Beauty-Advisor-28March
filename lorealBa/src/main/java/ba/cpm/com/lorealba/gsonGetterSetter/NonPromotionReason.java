package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonPromotionReason {
    @SerializedName("PReasonId")
    @Expose
    private Integer pReasonId;
    @SerializedName("PReason")
    @Expose
    private String pReason;

    public Integer getPReasonId() {
        return pReasonId;
    }

    public void setPReasonId(Integer pReasonId) {
        this.pReasonId = pReasonId;
    }

    public String getPReason() {
        return pReason;
    }

    public void setPReason(String pReason) {
        this.pReason = pReason;
    }
}
