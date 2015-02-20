package org.motechproject.nms.kilkari.domain;

interface ContentUploadKKCsvService {

     CircleCsv findById(Long id);

     void delete(CircleCsv record);

     void deleteAll();

}
