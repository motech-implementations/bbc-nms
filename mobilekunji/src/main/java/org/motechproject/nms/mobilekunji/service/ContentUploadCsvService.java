package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;

import java.util.List;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ContentUploadCsvService {

    public ContentUploadCsv createContentUploadCsv(ContentUploadCsv contentUploadCsv);

    public ContentUploadCsv findByIdInCsv(Long id);

    public void deleteFromCsv(ContentUploadCsv contentUploadCsv);

    public List<ContentUploadCsv> retrieveAllFromCsv();
}
