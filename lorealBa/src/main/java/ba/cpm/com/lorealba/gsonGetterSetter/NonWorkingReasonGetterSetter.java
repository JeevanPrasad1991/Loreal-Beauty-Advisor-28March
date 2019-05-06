
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonWorkingReasonGetterSetter {

    @SerializedName("Non_Working_Reason")
    @Expose
    private List<NonWorkingReason> nonWorkingReason = null;

    public List<NonWorkingReason> getNonWorkingReason() {
        return nonWorkingReason;
    }

    @SerializedName("Dashboard_Data")
    @Expose
    private List<DashboardDataGetter> dashboardData = null;

    public List<DashboardDataGetter> getDashboardData() {
        return dashboardData;
    }

    @SerializedName("Promotion_Master")
    @Expose
    private List<PromotionMaster> promotionMaster = null;

    public List<PromotionMaster> getPromotionMaster() {
        return promotionMaster;
    }

    @SerializedName("Non_Promotion_Reason")
    @Expose
    private List<NonPromotionReason> nonPromotionReason = null;

    public List<NonPromotionReason> getNonPromotionReason() {
        return nonPromotionReason;
    }

}
