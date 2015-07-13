package data;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Type {

	private final String mId;

    private Translation mName;

    private String mVar;

    private int mVarSize;

    private String mUnit;

    private String mTransformFrom;

    private String mTransformTo;

	public Type(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public Translation getName() {
		return mName;
	}

	public void setName(Translation name) {
		mName = name;
	}

    public String getVar() {
        return mVar;
    }

    public void setVar(String var) {
        mVar = var;
    }

    public int getVarSize() {
        return mVarSize;
    }

    public void setVarSize(int varSize) {
        mVarSize = varSize;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public String getTransformFrom() {
        return mTransformFrom;
    }

    public void setTransformFrom(String transformFrom) {
        mTransformFrom = transformFrom;
    }

    public String getTransformTo() {
        return mTransformTo;
    }

    public void setTransformTo(String transformTo) {
        mTransformTo = transformTo;
    }
}
