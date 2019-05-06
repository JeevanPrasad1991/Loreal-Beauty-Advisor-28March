
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockDataGetterSetter {

    @SerializedName("Stock_Data")
    @Expose
    private List<StockDatum> stockData = null;

    public List<StockDatum> getStockData() {
        return stockData;
    }

    public void setStockData(List<StockDatum> stockData) {
        this.stockData = stockData;
    }

}
