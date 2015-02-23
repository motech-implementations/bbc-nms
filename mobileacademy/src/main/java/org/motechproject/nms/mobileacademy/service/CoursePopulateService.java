package org.motechproject.nms.mobileacademy.service;

import org.motechproject.mtraining.domain.CourseUnitState;

/**
 * Service interface contains APIs to perform course populate operations in
 * mtraining.
 *
 */
public interface CoursePopulateService {

    /**
     *
     * populate course static Data in mtraining.
     *
     */
    public void populateMtrainingCourseData();

    /**
     * find Course State
     * 
     * @return Course state enum contain course state
     */
    public CourseUnitState findCourseState();

    /**
     * update Course State
     * 
     * @param courseUnitState Course state enum contain course state
     */
    public void updateCourseState(CourseUnitState courseUnitState);

    /**
     * update Correct Answer in mtraining
     * 
     * @param chapterName refer to chapterIdentifier i.e Chapter01,Chapter02
     * @param questionName refer to question identifier i.e
     *            Question01,Question02
     * @param answer refer to answer identifier i.e 1,2
     */
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer);
}
