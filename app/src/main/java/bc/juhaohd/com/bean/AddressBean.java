package bc.juhaohd.com.bean;

import java.util.List;

/**
 * Created by DEMON on 2018/1/22.
 */
public class AddressBean {

    /**
     * id : 193
     * zip_code :
     * address : 创意产业园
     * name : adminlee
     * tel :
     * province : 6
     * is_default : true
     * regions : [{"id":1,"more":1,"name":"中国"},{"id":6,"more":1,"name":"广东"},{"id":80,"more":1,"name":"佛山"},{"id":746,"more":0,"name":"禅城区"}]
     * mobile : 18566097950
     * city : 80
     * country : 1
     */
    private int id;
    private String zip_code;
    private String address;
    private String name;
    private String tel;
    private int province;
    private boolean is_default;
    private List<RegionsEntity> regions;
    private String mobile;
    private int city;
    private int country;

    public void setId(int id) {
        this.id = id;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public void setRegions(List<RegionsEntity> regions) {
        this.regions = regions;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public int getProvince() {
        return province;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public List<RegionsEntity> getRegions() {
        return regions;
    }

    public String getMobile() {
        return mobile;
    }

    public int getCity() {
        return city;
    }

    public int getCountry() {
        return country;
    }

    public class RegionsEntity {
        /**
         * id : 1
         * more : 1
         * name : 中国
         */
        private int id;
        private int more;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setMore(int more) {
            this.more = more;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getMore() {
            return more;
        }

        public String getName() {
            return name;
        }
    }
}
