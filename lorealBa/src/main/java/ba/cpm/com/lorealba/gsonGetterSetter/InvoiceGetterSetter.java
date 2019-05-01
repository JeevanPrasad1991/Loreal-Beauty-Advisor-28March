package ba.cpm.com.lorealba.gsonGetterSetter;

import java.io.Serializable;

public class InvoiceGetterSetter implements Serializable {
    String product;

    public String getScan_code_or_enterd_code() {
        return scan_code_or_enterd_code;
    }

    public void setScan_code_or_enterd_code(String scan_code_or_enterd_code) {
        this.scan_code_or_enterd_code = scan_code_or_enterd_code;
    }

    String scan_code_or_enterd_code;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.product_Id = product_Id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    String product_Id;
    String quantity;
    String amount;
    String total_amount;
    String customer_name;
    String mobile_no;
}
