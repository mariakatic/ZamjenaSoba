package com.piccologrupa.zamjenasoba.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LajkaniOglasKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "studentId")
	 Long studentId;

	 @Column(name = "oglasId")
	 Long oglasId;

	public LajkaniOglasKey(Long studentId, Long oglasId) {
		super();
		this.studentId = studentId;
		this.oglasId = oglasId;
	}

	public LajkaniOglasKey() {
		super();
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getOglasId() {
		return oglasId;
	}

	public void setOglasId(Long oglasId) {
		this.oglasId = oglasId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oglasId == null) ? 0 : oglasId.hashCode());
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LajkaniOglasKey other = (LajkaniOglasKey) obj;
		if (oglasId == null) {
			if (other.oglasId != null)
				return false;
		} else if (!oglasId.equals(other.oglasId))
			return false;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		return true;
	}
	
	
	
	 
}
