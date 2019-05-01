package ba.cpm.com.lorealba.gsonGetterSetter;

public class StockGetterSetter {
    String category_Id;
    String category;
    String sku_Id;
    String sku;
    String po_stock_qty="";

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    String validity;

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku_Id() {
        return sku_Id;
    }

    public void setSku_Id(String sku_Id) {
        this.sku_Id = sku_Id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPo_stock_qty() {
        return po_stock_qty;
    }

    public void setPo_stock_qty(String po_stock_qty) {
        this.po_stock_qty = po_stock_qty;
    }

    public String getStock_entry_qty() {
        return stock_entry_qty;
    }

    public void setStock_entry_qty(String stock_entry_qty) {
        this.stock_entry_qty = stock_entry_qty;
    }

    String stock_entry_qty="";
}
