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

	public int getNms_kk_72weekspack_msgs_per_week() {
		return nms_kk_72weekspack_msgs_per_week;
	}

	public void setNms_kk_72weekspack_msgs_per_week(
			int nms_kk_72weekspack_msgs_per_week) {
		this.nms_kk_72weekspack_msgs_per_week = nms_kk_72weekspack_msgs_per_week;
	}

	public int getNms_kk_48weekspack_msgs_per_week() {
		return nms_kk_48weekspack_msgs_per_week;
	}

	public void setNms_kk_48weekspack_msgs_per_week(
			int nms_kk_48weekspack_msgs_per_week) {
		this.nms_kk_48weekspack_msgs_per_week = nms_kk_48weekspack_msgs_per_week;
	}

	public int getNms_kk_max_allowed_active_beneficiary_count() {
		return nms_kk_max_allowed_active_beneficiary_count;
	}

	public void setNms_kk_max_allowed_active_beneficiary_count(
			int nms_kk_max_allowed_active_beneficiary_count) {
		this.nms_kk_max_allowed_active_beneficiary_count = nms_kk_max_allowed_active_beneficiary_count;
	}

	public String getNms_kk_fresh_obd_service_id() {
		return nms_kk_fresh_obd_service_id;
	}

	public void setNms_kk_fresh_obd_service_id(String nms_kk_fresh_obd_service_id) {
		this.nms_kk_fresh_obd_service_id = nms_kk_fresh_obd_service_id;
	}

	public String getNms_kk_retry_day1_obd_service_id() {
		return nms_kk_retry_day1_obd_service_id;
	}

	public void setNms_kk_retry_day1_obd_service_id(
			String nms_kk_retry_day1_obd_service_id) {
		this.nms_kk_retry_day1_obd_service_id = nms_kk_retry_day1_obd_service_id;
	}

	public String getNms_kk_retry_day2_obd_service_id() {
		return nms_kk_retry_day2_obd_service_id;
	}

	public void setNms_kk_retry_day2_obd_service_id(
			String nms_kk_retry_day2_obd_service_id) {
		this.nms_kk_retry_day2_obd_service_id = nms_kk_retry_day2_obd_service_id;
	}

	public String getNms_kk_retry_day3_obd_service_id() {
		return nms_kk_retry_day3_obd_service_id;
	}

	public void setNms_kk_retry_day3_obd_service_id(
			String nms_kk_retry_day3_obd_service_id) {
		this.nms_kk_retry_day3_obd_service_id = nms_kk_retry_day3_obd_service_id;
	}

	public int getNms_kk_fresh_obd_priority() {
		return nms_kk_fresh_obd_priority;
	}

	public void setNms_kk_fresh_obd_priority(int nms_kk_fresh_obd_priority) {
		this.nms_kk_fresh_obd_priority = nms_kk_fresh_obd_priority;
	}

	public String getNms_kk_retry_day1_obd_priority() {
		return nms_kk_retry_day1_obd_priority;
	}

	public void setNms_kk_retry_day1_obd_priority(
			String nms_kk_retry_day1_obd_priority) {
		this.nms_kk_retry_day1_obd_priority = nms_kk_retry_day1_obd_priority;
	}

	public String getNms_kk_retry_day2_obd_priority() {
		return nms_kk_retry_day2_obd_priority;
	}

	public void setNms_kk_retry_day2_obd_priority(
			String nms_kk_retry_day2_obd_priority) {
		this.nms_kk_retry_day2_obd_priority = nms_kk_retry_day2_obd_priority;
	}

	public String getNms_kk_retry_day3_obd_priority() {
		return nms_kk_retry_day3_obd_priority;
	}

	public void setNms_kk_retry_day3_obd_priority(
			String nms_kk_retry_day3_obd_priority) {
		this.nms_kk_retry_day3_obd_priority = nms_kk_retry_day3_obd_priority;
	}

	public String getNms_kk_obd_target_file_name() {
		return nms_kk_obd_target_file_name;
	}

	public void setNms_kk_obd_target_file_name(String nms_kk_obd_target_file_name) {
		this.nms_kk_obd_target_file_name = nms_kk_obd_target_file_name;
	}

	public String getNms_kk_obd_files_server_ip() {
		return nms_kk_obd_files_server_ip;
	}

	public void setNms_kk_obd_files_server_ip(String nms_kk_obd_files_server_ip) {
		this.nms_kk_obd_files_server_ip = nms_kk_obd_files_server_ip;
	}

	public String getNms_kk_obd_files_path() {
		return nms_kk_obd_files_path;
	}

	public void setNms_kk_obd_files_path(String nms_kk_obd_files_path) {
		this.nms_kk_obd_files_path = nms_kk_obd_files_path;
	}
	
	
}
