package com.margin.competencyframework.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Martha on 1/8/2017.
 */
public class Skill {

    @SerializedName("SkillID")
    private int mId;
    @SerializedName("SkillLanguage")
    private String mActionCategory;

    public Skill() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

}
