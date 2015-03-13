package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.service.ContentUploadCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("contentUploadCsvService")
public class ContentUploadCsvServiceImpl implements ContentUploadCsvService {

    @Autowired
    ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    @Override
    public ContentUploadCsv createContentUploadCsv(ContentUploadCsv contentUploadCsv) {
        return contentUploadCsvRecordDataService.create(contentUploadCsv);
    }

    @Override
    public ContentUploadCsv findByIdInCsv(Long id) {
        return contentUploadCsvRecordDataService.findById(id);
    }

    @Override
    public void deleteFromCsv(ContentUploadCsv contentUploadCsv) {
        contentUploadCsvRecordDataService.delete(contentUploadCsv);
    }

    @Override
    public List<ContentUploadCsv> retrieveAllFromCsv() {
        return contentUploadCsvRecordDataService.retrieveAll();
    }

}
