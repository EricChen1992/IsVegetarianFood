package thisis.vegetarian.question.mark.model;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;

public class InfoProduct{
    private long id;
    private String barcode;
    private String name;
    private int category;
    private String origin;
    private int vegetarian;
    private String remark;
    private int status;
    private boolean type;
    private Integer error;

    public InfoProduct(long product_id,
                       String product_barcode,
                       String product_name,
                       int product_category,
                       String product_origin,
                       int product_vegetarian,
                       String product_remark,
                       int product_status){
        this.id = product_id;
        this.barcode = product_barcode;
        this.name = product_name;
        this.category = product_category;
        this.origin = product_origin;
        this.vegetarian = product_vegetarian;
        this.remark = product_remark;
        this.status = product_status;
        this.type = true;
        this.error = null;

    }

    public InfoProduct(IVF_ProductDataEntity p){
        this.id = p.getId();
        this.barcode = p.getBarcode();
        this.name = p.getName();
        this.category = p.getCategory();
        this.origin = p.getOrigin();
        this.vegetarian = p.getVegetarian();
        this.remark = p.getRemark();
        this.status = p.getStatus();
        this.type = true;
        this.error = null;

    }

    public InfoProduct(String product_barcode, boolean product_type){
        this.id = -1;
        this.barcode = product_barcode;
        this.name = null;
        this.category = -1;
        this.origin = null;
        this.vegetarian = -1;
        this.remark = null;
        this.status = -1;
        this.type = product_type;
        this.error = null;
    }

    public InfoProduct(Integer error){
        this.id = -1;
        this.barcode = null;
        this.name = null;
        this.category = -1;
        this.origin = null;
        this.vegetarian = -1;
        this.remark = null;
        this.status = -1;
        this.type = false;
        this.error = error;
    }

    public long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    public String getOrigin() {
        return origin;
    }

    public int getVegetarian() {
        return vegetarian;
    }

    public String getRemark() {
        return remark;
    }

    public int getStatus() {
        return status;
    }

    public boolean getType() {
        return type;
    }

    public Integer getError() {
        return error;
    }
}
