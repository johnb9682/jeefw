package com.jeefw.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;
import com.jeefw.model.sys.param.SysUserParameter;

import core.support.DateSerializer;
import core.support.DateTimeSerializer;

/**
 * @author John
 */
@Entity
@Table(name = "question_answer")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount", "sortColumns", "cmd", "queryDynamicConditions", "sortedConditions", "dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class QuestionAndAnswer {

	private static final long serialVersionUID = 822330369002149147L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long questionAnswerId;
	@Column(name = "answerId")
	private Long answerId;
	@Column(name = "questionId")
	private Long questionId;

	public QuestionAndAnswer() {

	}

	public Long getQuestionAnswerId() {
		return questionAnswerId;
	}

	public void setQuestionAnswerId(Long questionAnswerId) {
		this.questionAnswerId = questionAnswerId;
	}
	
	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

}