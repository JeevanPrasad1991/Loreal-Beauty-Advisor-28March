
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockTesterDataGetterSetter {

    @SerializedName("Stock_Tester_Data")
    @Expose
    private List<StockTesterDatum> stockTesterData = null;

    public List<StockTesterDatum> getStockTesterData() {
        return stockTesterData;
    }

    public void setStockTesterData(List<StockTesterDatum> stockTesterData) {
        this.stockTesterData = stockTesterData;
    }

}
