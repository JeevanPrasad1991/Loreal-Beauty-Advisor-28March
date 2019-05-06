
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JCPGetterSetter {

    @SerializedName("Journey_Plan")
    @Expose
    private List<JourneyPlan> journeyPlan = null;

    public List<JourneyPlan> getJourneyPlan() {
        return journeyPlan;
    }
}
