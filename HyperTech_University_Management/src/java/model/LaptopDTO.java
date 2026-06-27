package model;

public class LaptopDTO {

    private int id;
    private String name;
    private String cpu;
    private String gpu;
    private String ram;
    private String ssd;
    private String screen;
    private String refresh_rate;
    private float old_price;
    private float new_price;
    private String image_url;

    public LaptopDTO() {}

    public LaptopDTO(int id,String name,String cpu,String gpu,String ram,String ssd,
                     String screen,String refresh_rate,float old_price,float new_price,String image_url) {

        this.id = id;
        this.name = name;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.ssd = ssd;
        this.screen = screen;
        this.refresh_rate = refresh_rate;
        this.old_price = old_price;
        this.new_price = new_price;
        this.image_url = image_url;
    }
    

    // getter setter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpu() {
        return cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public String getRam() {
        return ram;
    }

    public String getSsd() {
        return ssd;
    }

    public String getScreen() {
        return screen;
    }

    public String getRefresh_rate() {
        return refresh_rate;
    }

    public float getOld_price() {
        return old_price;
    }

    public float getNew_price() {
        return new_price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public void setRefresh_rate(String refresh_rate) {
        this.refresh_rate = refresh_rate;
    }

    public void setOld_price(float old_price) {
        this.old_price = old_price;
    }

    public void setNew_price(float new_price) {
        this.new_price = new_price;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    
}