package org.motechproject.nms.masterdata.event.mapper;

import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.constants.ErrorCodeConstants;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            } catch (NumberFormatException e) {

            } catch (Exception ex) {

            }
            return newRecord;
    }

    public static Circle mapCircleFrom(CircleCsv record) {
        Circle newRecord = new Circle();
        newRecord.setCode(record.getCode());
        newRecord.setName(record.getName());
        return newRecord;
    }

    public static Operator mapOperatorFrom(OperatorCsv record) {
        Operator newRecord = new Operator();
        newRecord.setName(record.getName());
        newRecord.setCode(record.getCode());
        return newRecord;
    }

    public static BulkUploadErrRecordDetails validateLanguageLocationCodeCsv(LanguageLocationCodeCsv record) {
        BulkUploadErrRecordDetails errorRecord = new BulkUploadErrRecordDetails();

        if (isEmptyOrNull(record.getCircleId())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("circleId"));
            return errorRecord;
        }
        if (isEmptyOrNull(record.getDistrictId())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("districtId"));
            return errorRecord;
        }

        if (isEmptyOrNull(record.getStateId())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("stateId"));
            return errorRecord;
        }

        if(record.getIsDeployedKK().equals("true")) {
            if (isEmptyOrNull(record.getLanguageLocationCodeKK()) ||
                    isEmptyOrNull(record.getIsDefaultLanguageLocationCodeKK()) ||
                    isEmptyOrNull(record.getLanguageKK())) {
                errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
                errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC
                        .format("LanguageLocationCodeKK or DefaultLanguageLocationCodeKK or LanguageKK"));
                return errorRecord;
            }
        }

        if(record.getIsDeployedMA().equals("true")) {
            if (isEmptyOrNull(record.getLanguageLocationCodeMA()) ||
                    isEmptyOrNull(record.getIsDefaultLanguageLocationCodeMA()) ||
                    isEmptyOrNull(record.getLanguageMA())) {
                errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
                errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC
                        .format("LanguageLocationCodeMA or DefaultLanguageLocationCodeMA or LanguageMA"));
                return errorRecord;
            }
        }

        if(record.getIsDeployedMK().equals("true")) {
            if (isEmptyOrNull(record.getLanguageLocationCodeMK()) ||
                    isEmptyOrNull(record.getIsDefaultLanguageLocationCodeMK()) ||
                    isEmptyOrNull(record.getLanguageMK())) {
                errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
                errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC
                        .format("LanguageLocationCodeMK or DefaultLanguageLocationCodeMK or LanguageMK"));
                return errorRecord;
            }
        }
        return null;
    }

    public static BulkUploadErrRecordDetails validateCircleCsv(CircleCsv record) {
        BulkUploadErrRecordDetails errorRecord = new BulkUploadErrRecordDetails();

        if (isEmptyOrNull(record.getCode())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("code"));
            return errorRecord;
        }

        if (isEmptyOrNull(record.getName())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("name"));
            return errorRecord;
        }
        return null;
    }

    public static BulkUploadErrRecordDetails validateOperatorCsv(OperatorCsv record) {
        BulkUploadErrRecordDetails errorRecord = new BulkUploadErrRecordDetails();

        if (isEmptyOrNull(record.getCode())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("code"));
            return errorRecord;
        }

        if (isEmptyOrNull(record.getName())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstant.RECORD_UPLOAD__ERROR_DESC.format("name"));
            return errorRecord;
        }
        return null;
    }



    public static boolean isEmptyOrNull(String value) {
        return !(value != null && !"".equals(value.trim()));
    }
}
