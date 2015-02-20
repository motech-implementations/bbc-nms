package org.motechproject.nms.mobileacademy.domain;


import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;import java.lang.String;

@Entity
public class Score extends MdsEntity {

    @Field
    private String name;

    @Field
    private String audioFile;

    public Score() {
    }

    public Score(String name, String audioFile) {
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
