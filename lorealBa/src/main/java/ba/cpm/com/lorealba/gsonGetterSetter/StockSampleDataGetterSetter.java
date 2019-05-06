
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockSampleDataGetterSetter {

    @SerializedName("Stock_Sample_Data")
    @Expose
    private List<StockSampleDatum> stockSampleData = null;

    public List<StockSampleDatum> getStockSampleData() {
        return stockSampleData;
    }

    public void setStockSampleData(List<StockSampleDatum> stockSampleData) {
        this.stockSampleData = stockSampleData;
    }

}
