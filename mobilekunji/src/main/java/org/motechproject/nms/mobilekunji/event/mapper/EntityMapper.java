package org.motechproject.nms.mobilekunji.event.mapper;

import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.mobilekunji.domain.StateCapMapping;
import org.motechproject.nms.mobilekunji.domain.StateCapMappingCsv;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.constants.ErrorCodeConstants;

public class EntityMapper {



    public static StateCapMapping mapStateCapMappingFrom(StateCapMappingCsv record) {
        StateCapMapping newRecord = new StateCapMapping();
        newRecord.setCode(record.getCode());
        newRecord.setName(record.getName());
        return newRecord;
    }





    public static BulkUploadErrRecordDetails validateStateCapMappingCsv(StateCapMappingCsv record) {
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
