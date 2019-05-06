package ba.cpm.com.lorealba.gsonGetterSetter;

import java.io.Serializable;

public class InvoiceGetterSetter implements Serializable {
    String product;

    public boolean isPos_sale_flag() {
        return pos_sale_flag;
    }

    public void setPos_sale_flag(boolean pos_sale_flag) {
        this.pos_sale_flag = pos_sale_flag;
    }

    boolean pos_sale_flag=false;
    public String getScan_ean_code_or_enterd_ean_code() {
        return scan_ean_code_or_enterd_ean_code;
    }

    public void setScan_ean_code_or_enterd_ean_code(String scan_ean_code_or_enterd_ean_code) {
        this.scan_ean_code_or_enterd_ean_code = scan_ean_code_or_enterd_ean_code;
    }

    String scan_ean_code_or_enterd_ean_code;

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

    public String getProduct_rate() {
        return product_rate;
    }

    public void setProduct_rate(String product_rate) {
        this.product_rate = product_rate;
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
    String product_rate;
    String total_amount;
    String customer_name;
    String mobile_no;
    String store_name;

    public String getCustomer_gender() {
        return customer_gender;
    }

    public void setCustomer_gender(String customer_gender) {
        this.customer_gender = customer_gender;
    }

    String customer_gender;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    String store_address;

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    String visit_date;
}
