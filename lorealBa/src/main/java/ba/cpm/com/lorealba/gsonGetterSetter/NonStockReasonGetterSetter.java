
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonStockReasonGetterSetter {

    @SerializedName("Non_Stock_Reason")
    @Expose
    private List<NonStockReason> nonStockReason = null;

    public List<NonStockReason> getNonStockReason() {
        return nonStockReason;
    }

    public void setNonStockReason(List<NonStockReason> nonStockReason) {
        this.nonStockReason = nonStockReason;
    }

}
