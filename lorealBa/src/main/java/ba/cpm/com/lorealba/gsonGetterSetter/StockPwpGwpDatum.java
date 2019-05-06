
package ba.cpm.com.lorealba.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockPwpGwpDatum {

    @SerializedName("StoreId")
    @Expose
    private Integer storeId;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("Stock")
    @Expose
    private Integer stock;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
