
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockPwpGwpDataGetterSetter {

    @SerializedName("Stock_PwpGwp_Data")
    @Expose
    private List<StockPwpGwpDatum> stockPwpGwpData = null;

    public List<StockPwpGwpDatum> getStockPwpGwpData() {
        return stockPwpGwpData;
    }

    public void setStockPwpGwpData(List<StockPwpGwpDatum> stockPwpGwpData) {
        this.stockPwpGwpData = stockPwpGwpData;
    }

}
