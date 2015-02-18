package org.motechproject.nms.masterdata.event.mapper;

import org.motechproject.nms.masterdata.domain.*;

import java.lang.Exception;

public class EntityMapper {

    public static LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record) {
        LanguageLocationCode newRecord = new LanguageLocationCode();
        try {
            newRecord.setCircleId(record.getCircleId());
            newRecord.setDistrictId(Integer.parseInt(record.getDistrictId()));
            newRecord.setStateId(Integer.parseInt(record.getStateId()));
            newRecord.setDefaultLanguageLocationCodeKK(Boolean.parseBoolean(record.getIsDefaultLanguageLocationCodeKK()));
            newRecord.setDefaultLanguageLocationCodeMA(Boolean.parseBoolean(record.getIsDefaultLanguageLocationCodeMA()));
            newRecord.setDefaultLanguageLocationCodeMK(Boolean.parseBoolean(record.getIsDefaultLanguageLocationCodeMK()));
            newRecord.setLanguageKK(record.getLanguageKK());
            newRecord.setLanguageLocationCodeKK(Integer.parseInt(record.getLanguageLocationCodeKK()));
            newRecord.setLanguageMA(record.getLanguageMA());
            newRecord.setLanguageLocationCodeMA(Integer.parseInt(record.getLanguageLocationCodeMA()));
            newRecord.setLanguageMK(record.getLanguageMK());
            newRecord.setLanguageLocationCodeMK(Integer.parseInt(record.getLanguageLocationCodeMK()));
            return newRecord;
        }catch(Exception ex) {

        }

        return newRecord;
    }

    public static Circle mapCircleFrom(CircleCsv record) {
        Circle newRecord = new Circle();
        try {
            newRecord.setCode(record.getCode());
            newRecord.setName(record.getName());

        }catch (Exception ex) {

        }
        return newRecord;
    }

    public static Operator mapOperatorFrom(OperatorCsv record) {
        Operator newRecord = new Operator();
        try {
            newRecord.setName(record.getName());
            newRecord.setCode(record.getCode());

        }catch (Exception ex) {

        }
      return newRecord;
    }
}
