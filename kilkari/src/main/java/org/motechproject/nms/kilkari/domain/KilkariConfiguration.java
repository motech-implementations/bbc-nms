package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
@Entity
public class KilkariConfiguration {
	
	@Field(required=true)
	private int nms_kk_72weekspack_msgs_per_week;
	
	@Field(required=true)
	private int nms_kk_48weekspack_msgs_per_week;
	
	@Field(required=true)
	private int nms_kk_max_allowed_active_beneficiary_count;
	
	@Field(required=true)
	private String nms_kk_fresh_obd_service_id;
	
	@Field(required=true)
	private String nms_kk_retry_day1_obd_service_id;
	
	@Field(required=true)
	private String nms_kk_retry_day2_obd_service_id;
	
	@Field(required=true)
	private String nms_kk_retry_day3_obd_service_id;
	
	@Field(required=true)
	private int nms_kk_fresh_obd_priority;
	
	@Field(required=true)
	private String nms_kk_retry_day1_obd_priority;
	
	@Field(required=true)
	private String nms_kk_retry_day2_obd_priority;
	
	@Field(required=true)
	private String nms_kk_retry_day3_obd_priority;
	
	@Field(required=true)
	private String nms_kk_obd_target_file_name;
	
	@Field(required=true)
	private String nms_kk_obd_files_server_ip;
	
	@Field(required=true)
	private String nms_kk_obd_files_path;
}
