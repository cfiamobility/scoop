package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;

import java.util.HashMap;

import ca.gc.inspection.scoop.postcomment.LikeState;

public enum CertifiedType {
    /**
     * Enum to map database values to custom values for readability.
     * The interactor should deal with converting between CertifiedType enum and corresponding
     * database String values.
     */

    BCP("bcp"),
    NONE("none");   // Value if CertifiedType does not exist in database json response

    private String databaseValue;
    private static HashMap<String, CertifiedType> map = new HashMap<>();

    CertifiedType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    /*
      Set up the map of database values to CertifiedTypes
      Cannot be called directly.
     */
    static {
        for (CertifiedType certifiedType : CertifiedType.values()) {
            map.put(certifiedType.databaseValue, certifiedType);
        }
        map.put(null, NONE);
    }

    /**
     * Get the CertifiedType from a databaseValue
     * @param databaseValue String certifiedType obtained from database Json response
     * @return
     */
    @NonNull
    public static CertifiedType getCertifiedTypeFrom(String databaseValue) {
        return map.get(databaseValue);
    }

    /**
     * Method used in interactor to make the purpose of the value explicit
     * @return
     */
    public String getDatabaseValue() {
        return this.databaseValue;
    }

    public String valueOf() {
        return getDatabaseValue();
    }
}
