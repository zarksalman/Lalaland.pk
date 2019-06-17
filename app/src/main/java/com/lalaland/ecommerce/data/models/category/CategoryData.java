package com.lalaland.ecommerce.data.models.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryData {

    @SerializedName("cartCount")
    @Expose
    private Integer cartCount;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;

    @SerializedName("brands")
    @Expose
    private List<CategoryBrand> brands = null;

    @SerializedName("about_us")
    @Expose
    private String aboutUs;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("returns")
    @Expose
    private String returns;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("faq")
    @Expose
    private String faq;

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<CategoryBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<CategoryBrand> brands) {
        this.brands = brands;
    }
    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }
}