package thisis.vegetarian.question.mark.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IVF_Product")
public class IVF_ProductDataEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String barcode;

    private String name;

    private int category;

    private String origin;

    private int vegetarian;

    private String remark;

    private int status;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String created_at;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String update_at;

    /**
     * Create Data
     * @param barcode
     * @param name
     * @param category
     * @param origin
     * @param vegetarian
     * @param remark
     * @param status
     * */
    public IVF_ProductDataEntity(String barcode, String name, int category, String origin, int vegetarian, String remark, int status) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.category = category;
        this.origin = origin;
        this.vegetarian = vegetarian;
        this.remark = remark;
        this.status = status;
    }

    /**
     *  BarCode
     * @param barcode
     * @see #getBarcode() 
     * */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    /**
     *  BarCode
     * @return  barcode
     * @see #setBarcode(String)
     * */
    public String getBarcode() {
        return barcode;
    }
    
    /**
     * Name
     * @param name 
     * @see #getName() 
     * */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Name
     * @return name
     * @see #setName(String) 
     * */
    public String getName() {
        return name;
    }
    
    /**
     * Category
     * @param category 
     * @see #getCategory() 
     * */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Category
     * @return category
     * @see #setCategory(int) 
     * */
    public int getCategory() {
        return category;
    }

    /**
     * Origin
     * @param origin
     * @see #getOrigin() 
     * */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Origin
     * @return origin
     * @see #setOrigin(String)
     * */
    public String getOrigin() {
        return origin;
    }

    /**
     * Vegetarian
     * @param vegetarian 
     * @see #getVegetarian() 
     * */
    public void setVegetarian(int vegetarian) {
        this.vegetarian = vegetarian;
    }
    
    /**
     * Vegetarian
     * @return vegetarian
     * @see #setVegetarian(int) 
     * */
    public int getVegetarian() {
        return vegetarian;
    }

    /**
     * Remark
     * @param remark
     * @see #getRemark() 
     * */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Remark
     * @return remark
     * @see #setRemark(String) 
     * */
    public String getRemark() {
        return remark;
    }

    /**
     * Status
     * @param status
     * @see #getStatus() 
     * */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Status
     * @return status
     * @see #setStatus(int)
     * */
    public int getStatus() {
        return status;
    }

    /**
     * Id
     * PrimaryKey
     * */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Id
     * PrimaryKey
     * */
    public long getId() {
        return id;
    }

    /**
     * Create Time
     * Auto Create
     * */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * Update Time
     * Auto Create
     * */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Create Time
     * Auto Create
     * */
    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    /**
     * Update Time
     * Auto Create
     * */
    public String getUpdate_at() {
        return update_at;
    }
}
