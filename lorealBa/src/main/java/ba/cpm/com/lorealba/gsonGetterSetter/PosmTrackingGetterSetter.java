package ba.cpm.com.lorealba.gsonGetterSetter;

public class PosmTrackingGetterSetter {
    String posm;
    String posm_exists_state="";
    String posm_img="";
    String posm_qr_data="";

    public String getPosm() {
        return posm;
    }

    public void setPosm(String posm) {
        this.posm = posm;
    }

    public String getPosm_exists_state() {
        return posm_exists_state;
    }

    public void setPosm_exists_state(String posm_exists_state) {
        this.posm_exists_state = posm_exists_state;
    }

    public String getPosm_img() {
        return posm_img;
    }

    public void setPosm_img(String posm_img) {
        this.posm_img = posm_img;
    }

    public String getPosm_qr_data() {
        return posm_qr_data;
    }

    public void setPosm_qr_data(String posm_qr_data) {
        this.posm_qr_data = posm_qr_data;
    }

    public String getPosm_currect_ans() {
        return posm_currect_ans;
    }

    public void setPosm_currect_ans(String posm_currect_ans) {
        this.posm_currect_ans = posm_currect_ans;
    }

    public String getReason_name() {
        return reason_name;
    }

    public void setReason_name(String reason_name) {
        this.reason_name = reason_name;
    }

    public String getReason_Id() {
        return reason_Id;
    }

    public void setReason_Id(String reason_Id) {
        this.reason_Id = reason_Id;
    }

    String posm_currect_ans="";
    String reason_name="";
    String reason_Id="0";
}
