package org.motechproject.nms.masterdata.domain;

import java.util.List;

/**
 * This class is used for hash code
 */
public class HashCode {

    /**
     * Finds the hash code of Long data
     * @param list
     * @param result
     * @return int
     */
    static public int hashCode(List<Long> list,int result)
    {
        for(Long l: list)
        {
            result = 31 * result + l.hashCode();
        }
        return result;
    }
}
