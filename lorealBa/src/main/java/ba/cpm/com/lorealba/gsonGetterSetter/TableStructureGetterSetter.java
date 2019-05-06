
package ba.cpm.com.lorealba.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TableStructureGetterSetter {

    @SerializedName("Table_Structure")
    @Expose
    private List<TableStructure> tableStructure = null;

    public List<TableStructure> getTableStructure() {
        return tableStructure;
    }

    @SerializedName("Product_Master")
    @Expose
    private List<ProductMaster> productMaster = null;

    public List<ProductMaster> getProductMaster() {
        return productMaster;
    }

    @SerializedName("InwardSales_PO")
    @Expose
    private List<InwardSalesPO> inwardSalesPO = null;

    public List<InwardSalesPO> getInwardSalesPO() {
        return inwardSalesPO;
    }

    public void setInwardSalesPO(List<InwardSalesPO> inwardSalesPO) {
        this.inwardSalesPO = inwardSalesPO;
    }


}
