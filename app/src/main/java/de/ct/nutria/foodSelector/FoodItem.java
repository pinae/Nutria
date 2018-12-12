package de.ct.nutria.foodSelector;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.ct.nutria.R;

public class FoodItem implements Parcelable {
    private int type;
    private byte[] id;
    private String nameAddition;
    private String authorName;
    private int categoryId;
    private String categoryString;
    private Date creationDate;
    private byte[] ean;
    private float referenceAmount = Float.NaN;
    private float calories = Float.NaN;
    private String manufacturer;
    private float total_fat = Float.NaN;
    private float saturated_fat = Float.NaN;
    private float cholesterol = Float.NaN;
    private float protein = Float.NaN;
    private float total_carbs = Float.NaN;
    private float sugar = Float.NaN;
    private float dietary_fiber = Float.NaN;
    private float salt = Float.NaN;
    private float sodium = Float.NaN;
    private float potassium = Float.NaN;
    private float copper = Float.NaN;
    private float iron = Float.NaN;
    private float magnesium = Float.NaN;
    private float manganese = Float.NaN;
    private float zinc = Float.NaN;
    private float phosphorous = Float.NaN;
    private float sulphur = Float.NaN;
    private float chloro = Float.NaN;
    private float fluoric = Float.NaN;
    private float vitaminB1 = Float.NaN;
    private float vitaminB12 = Float.NaN;
    private float vitaminB6 = Float.NaN;
    private float vitaminC = Float.NaN;
    private float vitaminD = Float.NaN;
    private float vitaminE = Float.NaN;

    FoodItem(String nameAddition) {
        this.nameAddition = nameAddition;
        this.creationDate = Calendar.getInstance().getTime();
        this.categoryId = -1;
        this.type = 0;
        this.setReferenceAmount(100);
    }

    private FoodItem(Parcel p) {
        type = p.readInt();
        p.readByteArray(id);
        nameAddition = p.readString();
        authorName = p.readString();
        categoryId = p.readInt();
        categoryString = p.readString();
        creationDate = new Date();
        creationDate.setTime(p.readLong());
        p.readByteArray(ean);
        referenceAmount = p.readFloat();
        calories = p.readFloat();
        manufacturer = p.readString();
        total_fat = p.readFloat();
        saturated_fat = p.readFloat();
        cholesterol = p.readFloat();
        protein = p.readFloat();
        total_carbs = p.readFloat();
        sugar = p.readFloat();
        dietary_fiber = p.readFloat();
        salt = p.readFloat();
        sodium = p.readFloat();
        potassium = p.readFloat();
        copper = p.readFloat();
        iron = p.readFloat();
        magnesium = p.readFloat();
        manganese = p.readFloat();
        zinc = p.readFloat();
        phosphorous = p.readFloat();
        sulphur = p.readFloat();
        chloro = p.readFloat();
        fluoric = p.readFloat();
        vitaminB1 = p.readFloat();
        vitaminB12 = p.readFloat();
        vitaminB6 = p.readFloat();
        vitaminC = p.readFloat();
        vitaminD = p.readFloat();
        vitaminE = p.readFloat();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getName() {
        return categoryString + ": " + nameAddition;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryString;
    }

    public void setCategory(int id, String categoryName) {
        this.categoryId = id;
        this.categoryString = categoryName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String author) {
        this.authorName = author;
    }

    public Date getDate() {
        return creationDate;
    }

    public void setCreationDate(Date newDate) {
        this.creationDate = newDate;
    }

    public float getReferenceAmount() {
        return referenceAmount;
    }

    public void setReferenceAmount(float referenceAmount) {
        this.referenceAmount = referenceAmount;
    }

    public float getCalories() {
        return calories;
    }

    public String getCaloriesString() {
        if (Float.isNaN(calories)) {
            return "";
        } else {
            return String.format(Locale.getDefault(), "%.0f", calories);
        }
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public byte[] getEan() {
        return ean;
    }

    public void setEan(byte[] ean) {
        this.ean = ean;
    }

    public String describeManufacturer(String recipeBy, String selfmade, String harvested) {
        if(type == 1) {
            if(authorName != null && authorName.length() > 0) {
                return recipeBy + authorName;
            } else {
                return selfmade;
            }
        }
        if(type == 0) {
            if(manufacturer != null && manufacturer.length() > 0) {
                return manufacturer;
            } else {
                return harvested;
            }
        }
        return "";
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(float total_fat) {
        this.total_fat = total_fat;
    }

    public float getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(float saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getTotal_carbs() {
        return total_carbs;
    }

    public void setTotal_carbs(float total_carbs) {
        this.total_carbs = total_carbs;
    }

    public float getSugar() {
        return sugar;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getDietary_fiber() {
        return dietary_fiber;
    }

    public void setDietary_fiber(float dietary_fiber) {
        this.dietary_fiber = dietary_fiber;
    }

    public float getSalt() {
        return salt;
    }

    public void setSalt(float salt) {
        this.salt = salt;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getPotassium() {
        return potassium;
    }

    public void setPotassium(float potassium) {
        this.potassium = potassium;
    }

    public float getCopper() {
        return copper;
    }

    public void setCopper(float copper) {
        this.copper = copper;
    }

    public float getIron() {
        return iron;
    }

    public void setIron(float iron) {
        this.iron = iron;
    }

    public float getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(float magnesium) {
        this.magnesium = magnesium;
    }

    public float getManganese() {
        return manganese;
    }

    public void setManganese(float manganese) {
        this.manganese = manganese;
    }

    public float getZinc() {
        return zinc;
    }

    public void setZinc(float zinc) {
        this.zinc = zinc;
    }

    public float getPhosphorous() {
        return phosphorous;
    }

    public void setPhosphorous(float phosphorous) {
        this.phosphorous = phosphorous;
    }

    public float getSulphur() {
        return sulphur;
    }

    public void setSulphur(float sulphur) {
        this.sulphur = sulphur;
    }

    public float getChloro() {
        return chloro;
    }

    public void setChloro(float chloro) {
        this.chloro = chloro;
    }

    public float getFluoric() {
        return fluoric;
    }

    public void setFluoric(float fluoric) {
        this.fluoric = fluoric;
    }

    public float getVitaminB1() {
        return vitaminB1;
    }

    public void setVitaminB1(float vitaminB1) {
        this.vitaminB1 = vitaminB1;
    }

    public float getVitaminB12() {
        return vitaminB12;
    }

    public void setVitaminB12(float vitaminB12) {
        this.vitaminB12 = vitaminB12;
    }

    public float getVitaminB6() {
        return vitaminB6;
    }

    public void setVitaminB6(float vitaminB6) {
        this.vitaminB6 = vitaminB6;
    }

    public float getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(float vitaminC) {
        this.vitaminC = vitaminC;
    }

    public float getVitaminD() {
        return vitaminD;
    }

    public void setVitaminD(float vitaminD) {
        this.vitaminD = vitaminD;
    }

    public float getVitaminE() {
        return vitaminE;
    }

    public void setVitaminE(float vitaminE) {
        this.vitaminE = vitaminE;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(type);
        out.writeByteArray(id);
        out.writeString(nameAddition);
        out.writeString(authorName);
        out.writeInt(categoryId);
        out.writeString(categoryString);
        out.writeLong(creationDate.getTime());
        out.writeByteArray(ean);
        out.writeFloat(referenceAmount);
        out.writeFloat(calories);
        out.writeString(manufacturer);
        out.writeFloat(total_fat);
        out.writeFloat(saturated_fat);
        out.writeFloat(cholesterol);
        out.writeFloat(protein);
        out.writeFloat(total_carbs);
        out.writeFloat(sugar);
        out.writeFloat(dietary_fiber);
        out.writeFloat(salt);
        out.writeFloat(sodium);
        out.writeFloat(potassium);
        out.writeFloat(copper);
        out.writeFloat(iron);
        out.writeFloat(magnesium);
        out.writeFloat(manganese);
        out.writeFloat(zinc);
        out.writeFloat(phosphorous);
        out.writeFloat(sulphur);
        out.writeFloat(chloro);
        out.writeFloat(fluoric);
        out.writeFloat(vitaminB1);
        out.writeFloat(vitaminB12);
        out.writeFloat(vitaminB6);
        out.writeFloat(vitaminC);
        out.writeFloat(vitaminD);
        out.writeFloat(vitaminE);
    }

    public static final Parcelable.Creator<FoodItem> CREATOR
            = new Parcelable.Creator<FoodItem>() {
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
