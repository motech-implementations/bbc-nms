package org.motechproject.nms.mobileacademy.service;

import org.motechproject.mtraining.domain.CourseUnitState;

/**
 * Service interface contains APIs to perform course populate operations.
 *
 */
public interface CoursePopulateService {

    /**
     * populate course Structure
     */
    public CourseUnitState findCourseState();

    public void updateCourseState(CourseUnitState courseUnitState);

    public void updateCorrectAnswer(int chapterId, int questionId, int answerId);
}
