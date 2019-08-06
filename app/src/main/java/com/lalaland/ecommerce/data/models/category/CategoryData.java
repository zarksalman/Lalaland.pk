package com.lalaland.ecommerce.data.models.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryData {

    @SerializedName("cartCount")
    @Expose
    private Integer cartCount;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;
    @SerializedName("products_url")
    @Expose
    private String productsUrl;
    @SerializedName("products_small_url")
    @Expose
    private String productsSmallUrl;
    @SerializedName("products_medium_url")
    @Expose
    private String productsMediumUrl;
    @SerializedName("products_thumb_url")
    @Expose
    private String productsThumbUrl;
    @SerializedName("brands_url")
    @Expose
    private String brandsUrl;
    @SerializedName("home_banners_url")
    @Expose
    private String homeBannersUrl;
    @SerializedName("featured_brands_url")
    @Expose
    private String featuredBrandsUrl;
    @SerializedName("featured_categories_url")
    @Expose
    private String featuredCategoriesUrl;
    @SerializedName("mobile_actions_url")
    @Expose
    private String mobileActionsUrl;
    @SerializedName("custom_products_url")
    @Expose
    private String customProductsUrl;
    @SerializedName("users_url")
    @Expose
    private String usersUrl;
    @SerializedName("blog_url")
    @Expose
    private String blogUrl;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("brands")
    @Expose
    private List<CategoryBrand> brands = null;
    @SerializedName("blogs")
    @Expose
    private String blogs;
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getProductsUrl() {
        return productsUrl;
    }

    public void setProductsUrl(String productsUrl) {
        this.productsUrl = productsUrl;
    }

    public String getProductsSmallUrl() {
        return productsSmallUrl;
    }

    public void setProductsSmallUrl(String productsSmallUrl) {
        this.productsSmallUrl = productsSmallUrl;
    }

    public String getProductsMediumUrl() {
        return productsMediumUrl;
    }

    public void setProductsMediumUrl(String productsMediumUrl) {
        this.productsMediumUrl = productsMediumUrl;
    }

    public String getProductsThumbUrl() {
        return productsThumbUrl;
    }

    public void setProductsThumbUrl(String productsThumbUrl) {
        this.productsThumbUrl = productsThumbUrl;
    }

    public String getBrandsUrl() {
        return brandsUrl;
    }

    public void setBrandsUrl(String brandsUrl) {
        this.brandsUrl = brandsUrl;
    }

    public String getHomeBannersUrl() {
        return homeBannersUrl;
    }

    public void setHomeBannersUrl(String homeBannersUrl) {
        this.homeBannersUrl = homeBannersUrl;
    }

    public String getFeaturedBrandsUrl() {
        return featuredBrandsUrl;
    }

    public void setFeaturedBrandsUrl(String featuredBrandsUrl) {
        this.featuredBrandsUrl = featuredBrandsUrl;
    }

    public String getFeaturedCategoriesUrl() {
        return featuredCategoriesUrl;
    }

    public void setFeaturedCategoriesUrl(String featuredCategoriesUrl) {
        this.featuredCategoriesUrl = featuredCategoriesUrl;
    }

    public String getMobileActionsUrl() {
        return mobileActionsUrl;
    }

    public void setMobileActionsUrl(String mobileActionsUrl) {
        this.mobileActionsUrl = mobileActionsUrl;
    }

    public String getCustomProductsUrl() {
        return customProductsUrl;
    }

    public void setCustomProductsUrl(String customProductsUrl) {
        this.customProductsUrl = customProductsUrl;
    }

    public String getUsersUrl() {
        return usersUrl;
    }

    public void setUsersUrl(String usersUrl) {
        this.usersUrl = usersUrl;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
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

    public String getBlogs() {
        return blogs;
    }

    public void setBlogs(String blogs) {
        this.blogs = blogs;
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