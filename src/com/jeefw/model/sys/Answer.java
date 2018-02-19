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
@Table(name = "answer")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount", "sortColumns", "cmd", "queryDynamicConditions", "sortedConditions", "dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class Answer extends SysUserParameter {

	private static final long serialVersionUID = 822330369002149147L;
	@Id
	@GeneratedValue
	@Column(name = "answerId")
	private Long answerId;
	@Column(name = "anwser")
	private String anwser;
	@Column(name = "author")
	private String author;
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public Answer() {

	}

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public String getAnswer() {
		return anwser;
	}

	public void setAnswer(String answer) {
		this.anwser = answer;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}