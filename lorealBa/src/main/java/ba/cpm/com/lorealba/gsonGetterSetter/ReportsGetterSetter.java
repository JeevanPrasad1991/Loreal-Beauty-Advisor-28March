package ba.cpm.com.lorealba.gsonGetterSetter;

public class ReportsGetterSetter {
String report_date;
    String sale_value;
    String selected_attendance;
    String intime_outtime;
    String incentive;

    public String getSelected_attendance() {
        return selected_attendance;
    }

    public void setSelected_attendance(String selected_attendance) {
        this.selected_attendance = selected_attendance;
    }

    public String getIntime_outtime() {
        return intime_outtime;
    }

    public void setIntime_outtime(String intime_outtime) {
        this.intime_outtime = intime_outtime;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public String getIncentive_value_name() {
        return incentive_value_name;
    }

    public void setIncentive_value_name(String incentive_value_name) {
        this.incentive_value_name = incentive_value_name;
    }

    String incentive_value_name;
int color_code;

    public int getColor_code() {
        return color_code;
    }

    public void setColor_code(int color_code) {
        this.color_code = color_code;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getSale_value() {
        return sale_value;
    }

    public void setSale_value(String sale_value) {
        this.sale_value = sale_value;
    }

}
