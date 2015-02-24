package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Score content object to refer scores of a chapter.
 *
 */
@Entity
public class ScoreContent extends MdsEntity {

    @Field
    private String name;

    @Field
    private String audioFile;

    /**
     * constructor with 0 arguments.
     */
    public ScoreContent() {
    }

    /**
     * constructor with all arguments.
     * 
     * @param name name of the score i.e score01, score02, score03, score04
     * @param audioFile name of the audio file.
     */
    public ScoreContent(String name, String audioFile) {
        this.name = name;
        this.audioFile = audioFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

}